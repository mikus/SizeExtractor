package net.mikus.assignment.sizeextractor.dao.csv;


import net.mikus.assignment.sizeextractor.dao.ProductReader;
import net.mikus.assignment.sizeextractor.model.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CSVProductReader implements ProductReader {

    private final String fileName;

    public CSVProductReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Stream<Product> read() throws IOException {
        FileReader reader = new FileReader(fileName);
        CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
        return StreamSupport.stream(parser.spliterator(), false)
                .map(r -> new Product(r.get("productId"), r.get("productName")));
    }
}
