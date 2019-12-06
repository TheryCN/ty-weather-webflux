package com.github.therycn.tyweatherwebflux.tuto.controller;

import com.github.therycn.tyweatherwebflux.tuto.entity.Product;
import com.github.therycn.tyweatherwebflux.tuto.entity.ProductEvent;
import com.github.therycn.tyweatherwebflux.tuto.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
class ProductControllerIT {

    private WebTestClient webTestClient;

    @Autowired
    private ProductRepository productRepository;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(new ProductController(productRepository))
                .configureClient()
                .baseUrl("/products")
                .build();

        productList = productRepository.findAll().collectList().block();
    }

    @Test
    void whenGetAllProducts_thenReturnAllProducts() {
        // Given
        // When + Then
        webTestClient.get()
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(Product.class)
                .isEqualTo(productList);
    }

    @Test
    void whenGetProduct_thenNotFound() {
        // Given
        // When + Then
        webTestClient.get().uri("/aaa")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void whenGetProduct_thenReturnProduct() {
        // Given
        // When + Then
        Product product = productList.get(0);
        webTestClient.get().uri("/{id}", product.getId())
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Product.class)
                .isEqualTo(product);
    }

    @Test
    void whenGetProductEvents_thenReturnSendEventStream() {
        // Given
        ProductEvent expectedProductEvent = new ProductEvent(0L, "Product Event");

        // When
        FluxExchangeResult<ProductEvent> productEventFluxExchangeResult = webTestClient.get().uri("/events")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(ProductEvent.class);

        StepVerifier.create(productEventFluxExchangeResult.getResponseBody())
                .expectNext(expectedProductEvent)
                .expectNextCount(2)
                .consumeNextWith(event -> Assertions.assertThat(event.getEventId()).isEqualTo(Long.valueOf(3)))
                .thenCancel()
                .verify();
    }
}