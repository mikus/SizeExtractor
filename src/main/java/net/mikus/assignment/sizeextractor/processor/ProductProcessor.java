package net.mikus.assignment.sizeextractor.processor;

import java.util.stream.Stream;

import net.mikus.assignment.sizeextractor.model.Product;


public interface ProductProcessor {

    Stream<Product> process(Stream<Product> productStream);

}
