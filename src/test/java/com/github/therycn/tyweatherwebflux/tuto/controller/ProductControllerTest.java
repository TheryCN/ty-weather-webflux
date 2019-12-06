package com.github.therycn.tyweatherwebflux.tuto.controller;

import com.github.therycn.tyweatherwebflux.tuto.entity.Product;
import com.github.therycn.tyweatherwebflux.tuto.entity.ProductEvent;
import com.github.therycn.tyweatherwebflux.tuto.handler.ProductHandler;
import com.github.therycn.tyweatherwebflux.tuto.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebFluxTest(value = {ProductController.class, ProductHandler.class})
class ProductControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CommandLineRunner commandLineRunner;

    private List<Product> productList;

    @BeforeEach
    void setUp() {
        webTestClient = webTestClient.mutate().baseUrl("/products").build();
        productList = Arrays.asList(new Product("1", "Big Latte", 2.99));
    }

    @Test
    void whenGetAllProducts_thenReturnAllProducts() {
        // Given
        Mockito.when(productRepository.findAll()).thenReturn(Flux.fromIterable(productList));
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
        Mockito.when(productRepository.findById("aaa")).thenReturn(Mono.empty());
        // When + Then
        webTestClient.get().uri("/aaa")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void whenGetProduct_thenReturnProduct() {
        // Given
        Product product = productList.get(0);
        Mockito.when(productRepository.findById(product.getId())).thenReturn(Mono.just(product));

        // When + Then
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