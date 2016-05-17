package net.mikus.assignment.sizeextractor.processor;


import net.mikus.assignment.sizeextractor.model.Product;

import java.util.stream.Stream;

public class IdentityProcessor implements ProductProcessor {

    @Override
    public Stream<Product> process(Stream<Product> productStream) {
        return productStream;
    }
}
