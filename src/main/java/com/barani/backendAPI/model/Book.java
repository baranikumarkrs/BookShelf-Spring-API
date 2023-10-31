package com.barani.backendAPI.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Book {

    @Id
    private String id;
    private String name;
    private String author;
    private Binary thumbnailImage;

    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }
}
