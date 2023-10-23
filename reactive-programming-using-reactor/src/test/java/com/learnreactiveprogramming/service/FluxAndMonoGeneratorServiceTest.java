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
        StepVerifier.create(namesFlux)
//                .expectNextCount(3)
//                .expectNext("Alex", "Ben", "Chloe")
                .expectNext("Alex")
                .expectNextCount(2) //because one event is already consumed, now we are left with 2
                .verifyComplete();
    }
}