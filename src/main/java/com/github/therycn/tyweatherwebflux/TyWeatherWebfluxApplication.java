package com.github.therycn.tyweatherwebflux;

import com.github.therycn.tyweatherwebflux.tuto.entity.Product;
import com.github.therycn.tyweatherwebflux.tuto.handler.ProductHandler;
import com.github.therycn.tyweatherwebflux.tuto.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class TyWeatherWebfluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(TyWeatherWebfluxApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ProductRepository productRepository) {
        return args -> {
            Flux<Product> productFlux = Flux.just(
                    new Product(null, "Big Latte", 2.99),
                    new Product(null, "Big Decaf", 2.49),
                    new Product(null, "Green Tea", 1.99)
            ).flatMap(productRepository::save);
            productFlux.thenMany(productRepository.findAll()).subscribe(System.out::println);
        };
    }

    @Bean
    RouterFunction<ServerResponse> routes(ProductHandler handler) {
        return nest(path("/fproducts"), nest(accept(MediaType.APPLICATION_JSON),
                        route(GET("/"), handler::getAllProducts)
                                .andRoute(GET("/webClient"), handler::getAllProductsWebClient)
                                .andRoute(GET("/{id}"), handler::getProduct)));
    }
}
