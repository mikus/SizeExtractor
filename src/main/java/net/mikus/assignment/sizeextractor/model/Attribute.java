package net.mikus.assignment.sizeextractor.model;


import lombok.Data;

@Data
public class Attribute {

    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
