package net.mikus.assignment.sizeextractor.dao.cassandra;


import com.datastax.driver.core.*;
import net.mikus.assignment.sizeextractor.dao.ProductReader;
import net.mikus.assignment.sizeextractor.model.Product;

import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CassandraProductReader implements ProductReader {

    private final Cluster cluster;
    private final Session session;
    private final Statement statement;

    public CassandraProductReader(String keyspaceName, String tableName) {
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect(keyspaceName);
        statement = new SimpleStatement("SELECT * FROM " + tableName);
    }

    @Override
    public Stream<Product> read() throws IOException {
        ResultSet rs = session.execute(statement);
        return StreamSupport.stream(rs.spliterator(), false)
                .map(r -> new Product(r.getString("id"), r.getString("name")));
    }
}
