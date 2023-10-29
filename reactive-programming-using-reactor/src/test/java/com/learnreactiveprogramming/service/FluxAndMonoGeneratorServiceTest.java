package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.List;

class FluxAndMonoGeneratorServiceTest {

    FluxAndMonoGeneratorService service = new FluxAndMonoGeneratorService();

    @Test
    void namesFlux() {
        //given

        //when
        var namesFlux = service.namesFlux();

        //then
        StepVerifier.create(namesFlux) //create automatically invokes the Publisher to send events
//                .expectNextCount(3)
//                .expectNext("Alex", "Ben", "Chloe")
                .expectNext("Alex")
                .expectNextCount(2) //because one event is already consumed, now we are left with 2
                .verifyComplete();
    }

    @Test
    void otherNamesFlux() {
        var namesFlux = service.otherNamesFlux();
        StepVerifier.create(namesFlux)
                .expectNext("ALEX", "CHLOE")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_flatMap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .verifyComplete();
    }

    @Test
    void namesFlux_flatMap_withDelay() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_flatMap_async(stringLength);
        StepVerifier.create(namesFlux)
//                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_concatMap() {
        int stringLength = 3;
        var namesFlux = service.namesFlux_concatMap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
//                .expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        int stringLength = 3;
        var nameMono = service.namesMono_flatMap(stringLength);
        StepVerifier.create(nameMono)
                .expectNext(List.of("A", "L", "E", "X"))
                .verifyComplete();
    }
}