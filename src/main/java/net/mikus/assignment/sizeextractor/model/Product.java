package net.mikus.assignment.sizeextractor.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Product {

    private String id;
    private String name;
    private List<Attribute> attributes;

    public Product(String id) {
        this(id, null);
    }

    public Product(String id, String name) {
        this.id = id;
        this.name = name;
        attributes = new ArrayList<>();
    }
}
