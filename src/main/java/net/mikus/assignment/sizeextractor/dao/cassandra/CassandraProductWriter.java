package net.mikus.assignment.sizeextractor.dao.cassandra;


import com.datastax.driver.core.*;
import net.mikus.assignment.sizeextractor.dao.ProductWriter;
import net.mikus.assignment.sizeextractor.model.Product;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CassandraProductWriter implements ProductWriter {

    private final Cluster cluster;
    private final Session session;
    private final PreparedStatement statement;
    private final UserType attributeType;

    public CassandraProductWriter(String keyspaceName, String tableName) {
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + keyspaceName + " WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 }");
        session.execute("CREATE TYPE IF NOT EXISTS " + keyspaceName + ".attribute ( name text, value text )");
        session.execute("CREATE TABLE IF NOT EXISTS " + keyspaceName + "." + tableName + "( id text PRIMARY KEY, name text, attributes set<frozen<attribute>> )");
        statement = session.prepare("INSERT INTO " + keyspaceName + "." + tableName + "(id, name, attributes) VALUES (?,?,?)");
        attributeType = cluster.getMetadata().getKeyspace(keyspaceName).getUserType("attribute");
    }

    @Override
    public void write(Stream<Product> productStream) throws IOException {
        productStream.map(this::statementMapper).forEach(session::execute);
    }

    private BoundStatement statementMapper(Product product) {
        Set<UDTValue> mappedAttributes = product.getAttributes().stream()
                .map(a -> attributeType.newValue().setString("name", a.getName()).setString("value", a.getValue()))
                .collect(Collectors.toSet());
        BoundStatement boundStatement = new BoundStatement(statement);
        return boundStatement.bind(product.getId(), product.getName(), mappedAttributes);
    }
}
