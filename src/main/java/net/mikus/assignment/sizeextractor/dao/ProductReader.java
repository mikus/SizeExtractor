package net.mikus.assignment.sizeextractor.dao;

import java.io.IOException;
import java.util.stream.Stream;

import net.mikus.assignment.sizeextractor.model.Product;


public interface ProductReader {

    Stream<Product> read() throws IOException;
}
