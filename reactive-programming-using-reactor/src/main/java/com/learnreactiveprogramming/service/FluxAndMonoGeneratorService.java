package com.learnreactiveprogramming.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
        generatorService.namesFlux_flatMap(STRING_LENGTH).subscribe(System.out::println);
    }


    private static boolean isLengthFine(String str) {
        return str.length() > 5;
    }

    //ALEX -> Flux(A,L,E,X)
    public static Flux<String> splitString(String name) {
        var splittedName = name.split("");
        return Flux.fromArray(splittedName);
    }

    //Not working?
    public static Flux<String> splitStringWithDelay(String name) {
        var splittedName = name.split("");
        return Flux
                .fromArray(splittedName)
                .delayElements(Duration.ofMillis(1000));
    }

    //ALEX -> Flux(A,L,E,X)
    public static Mono<List<String>> splitStringMono(String name) {
        String[] charArray = name.split("");
        return Mono.just(List.of(charArray));
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

    public Flux<String> namesFlux_flatMap_async(int stringLength) {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                //We need A,L,E,X,C,H,L,O,E
                .flatMap(FluxAndMonoGeneratorService::splitStringWithDelay)
                .log();
    }

    //concatMap (preserves the ordering, unlike flatMap)
    public Flux<String> namesFlux_concatMap(int stringLength) {
        return Flux.fromIterable(List.of("Alex", "Ben", "Chloe"))
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                //We need A,L,E,X,C,H,L,O,E
                //.flatMap(FluxAndMonoGeneratorService::splitStringWithDelay)
                .concatMap(FluxAndMonoGeneratorService::splitStringWithDelay)
                .log();
    }

    public Mono<List<String>> namesMono_flatMap(int stringLength) {
        return Mono.just("alex")
                .filter(name -> name.length() > stringLength)
                .map(String::toUpperCase)
                .flatMap(FluxAndMonoGeneratorService::splitStringMono)
                .log();
    }

    // Key differences:
    // 1. map applies a one-to-one transformation to each element of the source Flux,
    //    resulting in a Flux of Fluxes (in this case, Flux<String>).
    // 2. flatMap, on the other hand, applies a one-to-many transformation. It flattens
    //    the inner Fluxes and produces a single Flux of the transformed elements.
    // 3. Use map when you want to transform each element individually and maintain
    //    a one-to-one relationship between source and transformed elements.
    // 4. Use flatMap when you want to transform each element into multiple elements,
    //    and you want to flatten the result into a single Flux.
    // 5. map is used for sync transformations, flatMap is used for async transformations.
    // 6. map does not support transformations that return Publisher.
    //    flatMap supports that.
}
