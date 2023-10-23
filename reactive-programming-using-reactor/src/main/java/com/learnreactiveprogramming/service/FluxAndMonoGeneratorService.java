package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static void main(String[] args) {
        FluxAndMonoGeneratorService fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
        fluxAndMonoGeneratorService.namesFlux().subscribe(System.out::println);
        //or we could have also used: .subscribe(name -> {
        // System.out.println(name);
        // });

        fluxAndMonoGeneratorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("The name is: " + name);
                });

        //NOTE: Nothing happens until you subscribe
    }

    public Flux<String> namesFlux() {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe")); //comes either from a remote service or db eventually
    }
}
