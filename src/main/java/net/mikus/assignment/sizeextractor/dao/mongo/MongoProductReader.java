package net.mikus.assignment.sizeextractor.dao.mongo;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.mikus.assignment.sizeextractor.dao.ProductReader;
import net.mikus.assignment.sizeextractor.model.Product;
import org.bson.Document;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MongoProductReader implements ProductReader {

    private final MongoClient client;
    private final MongoDatabase db;
    private final MongoCollection<Document> collection;

    public MongoProductReader(String dbName, String collectionName) {
        this(new MongoClient(), dbName, collectionName);
    }

    public MongoProductReader(String connectionString, String dbName, String collectionName) {
        this(new MongoClient(new MongoClientURI(connectionString)), dbName, collectionName);
    }

    private MongoProductReader(MongoClient client, String dbName, String collectionName) {
        this.client = client;
        db = client.getDatabase(dbName);
        collection = db.getCollection(collectionName);
    }

    @Override
    public Stream<Product> read() throws IOException {
        return StreamSupport.stream(collection.find().spliterator(), false)
                .map(d -> new Product(d.getString("id"), d.getString("name")));
    }
}
