package com.github.therycn.tyweatherwebflux.tuto.handler;

import com.github.therycn.tyweatherwebflux.tuto.entity.Product;
import com.github.therycn.tyweatherwebflux.tuto.repository.ProductRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    private ProductRepository productRepository;

    public ProductHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ServerResponse> getAllProducts(ServerRequest serverRequest) {
        Flux<Product> products = productRepository.findAll();
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }

    public Mono<ServerResponse> getProduct(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Product> productMono = productRepository.findById(id);
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return productMono
                .flatMap(product -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(product)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> getAllProductsWebClient(ServerRequest serverRequest) {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        Flux<Product> products = webClient.get().uri("/products").accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Product.class);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(products, Product.class);
    }
}
