package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService generatorService = new FluxAndMonoGeneratorService();
//        generatorService.namesFlux().subscribe(System.out::println);
        //or we could have also used: .subscribe(name -> {
        // System.out.println(name);
        // });

        generatorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("The name is: " + name);
                });

        //NOTE: Nothing happens until you subscribe

//        generatorService.namesMono().subscribe(System.out::println);
        generatorService.namesMono().subscribe(x -> System.out.println("The name is: " + x));


        //useful operations on top of the data from Flux::
        generatorService.otherNamesFlux().subscribe(name -> System.out.println("Transformed name is: " + name));

        //to show that flux instance is immutable (reactive streams are immutable)
        generatorService.namesFluxImmutability().subscribe(name -> System.out.println("Value changed? " + name));
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .log(); //comes either from a remote service or db eventually (every element of the list comes as an event)
    }

    public Mono<String> namesMono() {
        return Mono.just("Amber").log();
    }

    public Flux<String> otherNamesFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() > 3)
                .map(String::toUpperCase)
                .log();
    }

    public Flux<String> namesFluxImmutability() {
        var names = Flux.fromIterable(List.of("Alex", "Ben", "Chloe"));
        names.map(String::toUpperCase);
        return names;
    }
}
