package net.mikus.assignment.sizeextractor.processor;

import com.google.common.base.CharMatcher;
import net.mikus.assignment.sizeextractor.model.Attribute;
import net.mikus.assignment.sizeextractor.model.Product;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class SizeProcessor implements ProductProcessor {

    private final static String[] sizeKeywordModifiers = new String[] { "manufacturer", "uk", "us", "usa",
            "eu", "european", "women(?:\\s+\\w+)?", "men(?:\\s+\\w+)?", "frame", "picture", "fr" };
    private final static String[] sizeKeywordNegativeModifiers = new String[] { "king", "queen", "snack", "all", "various", "special", "&" };
    private final static String[] sizeAttributeInternals = new String[] { "\\s", "\\d", "\\.", ",", "\\-", "/", ";", "x", "+" };
    private final static String[] sizeAttributeUnits = new String[] { "'", "''", "\"", "inch(?:es)?", "ins?", "years?", "months?",
            "km", "cm", "mm", "m", "kg", "g", "lbs?", "length", "width", "and", "or", "old", "A\\d", "B\\d",
            "for\\s+ladies", "for\\s+men", "free" };

    private final Pattern extractorPattern;
    private final Pattern filterPattern;

    public SizeProcessor() {
        extractorPattern = Pattern.compile(buildExtractorPatternExpression());
        filterPattern = Pattern.compile(buildFilterPatternExpression());
    }

    private String buildExtractorPatternExpression() {
        StringBuilder patternExpression = new StringBuilder("(?i)(?:(?:\\s*[\\(\\[])|(?<=[\\s+/]))(");
        Arrays.stream(sizeKeywordModifiers).forEach(modifier -> {
            patternExpression.append(modifier).append("\\s+sizes?|");
            patternExpression.append("sizes?\\s+").append(modifier).append("|");
        });
        Arrays.stream(sizeKeywordNegativeModifiers).forEach(modifier ->
            patternExpression.append("(?<!").append(modifier).append("\\s?)")
        );
        patternExpression.append("sizes?)[:\\s]+([^\\s\\)\\]/]+(?:");

        StringBuilder innerExpression = new StringBuilder("[");
        Arrays.stream(sizeAttributeInternals).forEach(innerExpression::append);
        innerExpression.append("]");
        Arrays.stream(sizeAttributeUnits).forEach(unit -> innerExpression.append("|").append(unit).append("(?=\\W|$)"));

        patternExpression.append(innerExpression.toString()).append("|\\((?:").append(innerExpression.toString()).append(")+\\)");
        patternExpression.append(")*)(?:\\s*[\\)\\]])?");
        return patternExpression.toString();
    }

    private String buildFilterPatternExpression() {
        StringBuilder patternExpression = new StringBuilder("(?i).*");
        Arrays.stream(sizeKeywordNegativeModifiers).forEach(modifier ->
                patternExpression.append("(?<!").append(modifier).append("\\s?)")
        );
        patternExpression.append("\\s+sizes?\\s+.*");
        return patternExpression.toString();
    }

    @Override
    public Stream<Product> process(Stream<Product> productStream) {
        return productStream
                .filter(p -> filterPattern.matcher(p.getName()).matches())
                .map(this::process);
    }

    public Product process(Product input) {
        Product output = new Product(input.getId());
        processName(input, output);
        return output;
    }

    private void processName(Product oldProduct, Product newProduct) {
        StringBuilder newName = new StringBuilder(oldProduct.getName());
        Matcher matcher = extractorPattern.matcher(oldProduct.getName());
        int deleted = 0;
        while (matcher.find()) {
            Attribute attribute = new Attribute(trim(matcher.group(1)), trim(matcher.group(2)));
            newProduct.getAttributes().add(attribute);
            newName.delete(matcher.start() - deleted, matcher.end() - deleted);
            deleted += matcher.end() - matcher.start();
        }
        newProduct.setName(trim(newName.toString()));
    }

    private static String trim(String text) {
        return CharMatcher.anyOf(" ,;/-+").trimFrom(text);
    }

    public String[] splitName(String name) {
        return name.split("(\\s*,\\s+|\\s*;\\s+|\\s*//\\s*|\\s+\\-\\s+)(?!\\d)");
    }

}
