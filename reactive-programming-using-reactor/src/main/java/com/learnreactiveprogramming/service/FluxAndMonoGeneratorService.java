package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class FluxAndMonoGeneratorService {

    public static final int STRING_LENGTH = 3;

    public static void main(String[] args) {
        FluxAndMonoGeneratorService generatorService = new FluxAndMonoGeneratorService();
        //generatorService.namesFlux()
        //      .subscribe(System.out::println);
        //or we could have also used:
        //      .subscribe(name -> {
        //          System.out.println(name);
        // });

        generatorService.namesFlux()
                .subscribe(name -> {
                    System.out.println("The name is: " + name); //this is consuming the data
                });

        //NOTE: Nothing happens until you subscribe

//        generatorService.namesMono().subscribe(System.out::println);
        generatorService.namesMono().subscribe(x -> System.out.println("The name is: " + x));


        //useful operations on top of the data from Flux::
        generatorService.otherNamesFlux().subscribe(name -> System.out.println("Transformed name is: " + name));

        //to show that flux instance is immutable (reactive streams are immutable)
        generatorService.namesFluxImmutability().subscribe(name -> System.out.println("Value changed? " + name));

        //filter example
        generatorService.requiredNames().subscribe(x -> {
            System.out.println("Required name: " + x);
        });

        //flatMap() example
        generatorService.namesFlux_flatMap(STRING_LENGTH).subscribe(System.out::print);
    }


    private static boolean isLengthFine(String str) {
        return str.length() > 5;
    }

    //ALEX -> Flux(A,L,E,X)
    public static Flux<String> splitString(String name) {
        var splittedName = name.split("");
        return Flux.fromArray(splittedName);
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

    public Flux<String> requiredNames() {
        return Flux.fromIterable(List.of("Amber", "Mustafa", "Hesoyam", "Chloe", "La Martiniere", "Alex"))
                .filter(FluxAndMonoGeneratorService::isLengthFine)
                .log();
    }

    public Flux<String> namesFlux_flatMap(int stringLength) {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                //We need A,L,E,X,C,H,L,O,E
                .flatMap(FluxAndMonoGeneratorService::splitString);
//                .log();
    }
}
