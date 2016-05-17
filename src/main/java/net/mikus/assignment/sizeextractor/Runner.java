package net.mikus.assignment.sizeextractor;


import net.mikus.assignment.sizeextractor.dao.ProductReader;
import net.mikus.assignment.sizeextractor.dao.ProductWriter;
import net.mikus.assignment.sizeextractor.dao.cassandra.CassandraProductReader;
import net.mikus.assignment.sizeextractor.dao.cassandra.CassandraProductWriter;
import net.mikus.assignment.sizeextractor.dao.csv.CSVProductReader;
import net.mikus.assignment.sizeextractor.dao.csv.CSVProductWriter;
import net.mikus.assignment.sizeextractor.dao.mongo.MongoProductReader;
import net.mikus.assignment.sizeextractor.dao.mongo.MongoProductWriter;
import net.mikus.assignment.sizeextractor.processor.IdentityProcessor;
import net.mikus.assignment.sizeextractor.processor.ProductProcessor;
import net.mikus.assignment.sizeextractor.processor.SizeProcessor;

import java.io.IOException;


public class Runner {

    public static void main(String... args) {
        ProductReader reader = new CSVProductReader("TestFile.csv");
        //ProductReader reader = new MongoProductReader("mydb", "products");
        //ProductReader reader = new CassandraProductReader("myks", "products");

        ProductWriter writer = new CSVProductWriter("OutputFile.csv");
        //ProductWriter writer = new MongoProductWriter("mydb", "sizes");
        //ProductWriter writer = new CassandraProductWriter("myks", "sizes");

        ProductProcessor processor = new SizeProcessor();
        //ProductProcessor processor = new IdentityProcessor();

        proceed(reader, processor, writer);
    }

    private static void proceed(ProductReader reader, ProductProcessor processor, ProductWriter writer) {
        try {
            writer.write(
                processor.process(
                    reader.read()
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
