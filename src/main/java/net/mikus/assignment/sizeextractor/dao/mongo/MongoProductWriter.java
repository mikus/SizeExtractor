package net.mikus.assignment.sizeextractor.dao.mongo;


import com.google.common.collect.Iterators;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.mikus.assignment.sizeextractor.dao.ProductWriter;
import net.mikus.assignment.sizeextractor.model.Product;
import org.bson.Document;

import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MongoProductWriter implements ProductWriter {

    private static final int BATCH_SIZE = 100;

    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Document> collection;

    public MongoProductWriter(String dbName, String collectionName) {
        this(new MongoClient(), dbName, collectionName);
    }

    public MongoProductWriter(String connectionString, String dbName, String collectionName) {
        this(new MongoClient(new MongoClientURI(connectionString)), dbName, collectionName);
    }

    private MongoProductWriter(MongoClient client, String dbName, String collectionName) {
        this.client = client;
        db = client.getDatabase(dbName);
        collection = db.getCollection(collectionName);
    }

    @Override
    public void write(Stream<Product> productStream) throws IOException {
        Stream<Document> mappedStream = productStream.map(this::documentMapper);
        Iterators.partition(mappedStream.iterator(), BATCH_SIZE)
                .forEachRemaining(collection::insertMany);
    }

    private Document documentMapper(Product product) {
        return new Document("id", product.getId()) // id instead of _id is intentionally - there are duplicated ids in the test file
                .append("name", product.getName())
                .append("attributes", product.getAttributes().stream().map(a ->
                    new Document("name", a.getName()).append("value", a.getValue())
                ).collect(Collectors.toList()));
    }
}
