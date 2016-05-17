package net.mikus.assignment.sizeextractor.dao.csv;


import com.google.common.base.Joiner;
import net.mikus.assignment.sizeextractor.dao.ProductWriter;
import net.mikus.assignment.sizeextractor.model.Product;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVProductWriter implements ProductWriter {

    private final String fileName;

    public CSVProductWriter(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void write(Stream<Product> productStream) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        CSVPrinter printer = CSVFormat.DEFAULT.withFirstRecordAsHeader().print(writer);
        productStream.map(this::recordMapper).forEach(r -> printRecord(printer, r));
        printer.close();
    }

    private Object[] recordMapper(Product product) {
        Object[] result = new Object[] { product.getId(), product.getName(), "" };
        List<String> mappedAttributes = product.getAttributes().stream()
                .map(a -> String.format("\"%s\"=\"%s\"", a.getName(), a.getValue()))
                .collect(Collectors.toList());
        result[2] = Joiner.on(';').join(mappedAttributes);
        return result;
    }

    private void printRecord(CSVPrinter printer, Object[] record) {
        try {
            printer.printRecord(record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
