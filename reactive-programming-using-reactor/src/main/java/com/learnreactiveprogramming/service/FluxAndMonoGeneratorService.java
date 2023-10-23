package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService generatorService = new FluxAndMonoGeneratorService();
        generatorService.namesFlux().subscribe(System.out::println);
        //or we could have also used: .subscribe(name -> {
        // System.out.println(name);
        // });

        generatorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("The name is: " + name);
                });

        //NOTE: Nothing happens until you subscribe

        generatorService.namesMono().subscribe(System.out::println);
        generatorService.namesMono().subscribe(x -> System.out.println("The name is: " + x));

    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); //comes either from a remote service or db eventually
    }

    public Mono<String> namesMono() {
        return Mono.just("Amber");
    }
}
