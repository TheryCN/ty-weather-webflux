package com.github.therycn.tyweatherwebflux.tuto.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Product {

    @Id
    private String id;

    private String name;

    private Double price;

}
