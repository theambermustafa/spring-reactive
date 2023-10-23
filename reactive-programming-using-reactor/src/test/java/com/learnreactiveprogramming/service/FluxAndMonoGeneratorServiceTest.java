package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

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
}