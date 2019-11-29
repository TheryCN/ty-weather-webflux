package com.github.therycn.tyweatherwebflux;

import com.github.therycn.tyweatherwebflux.tuto.entity.Product;
import com.github.therycn.tyweatherwebflux.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

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
}
