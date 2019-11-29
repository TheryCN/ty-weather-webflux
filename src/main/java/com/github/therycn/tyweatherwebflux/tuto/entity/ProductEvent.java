package com.github.therycn.tyweatherwebflux.tuto.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class ProductEvent {

    private Long eventId;

    private String eventType;
}
