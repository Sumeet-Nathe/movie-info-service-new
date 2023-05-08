package co.in.an.eye.tech.moviereviewservice.unit.controller;

import co.in.an.eye.tech.moviereviewservice.controller.FluxAndMonoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@WebFluxTest(controllers = FluxAndMonoController.class)
@AutoConfigureWebTestClient
class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void getFlux() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(String.class)
                .hasSize(1)
                .contains("SumeetNathe");
    }

    @Test
    void getFlux_approach2() {
        var responseBody = webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(String.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext("SumeetNathe")
                .verifyComplete();
    }

    @Test
    void getFlux_approach3() {
        webTestClient.get()
                .uri("/flux")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(String.class)
                .consumeWith(listEntityExchangeResult -> {
                    List<String> responseBody = listEntityExchangeResult.getResponseBody();
                    assert Objects.requireNonNull(responseBody).size() == 1;
                });
    }

    @Test
    void getHelloWorldfromMono() {
        webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(String.class)
                .hasSize(1)
                .contains("Hello-world");
    }

    @Test
    void getHelloWorldfromMono_Approach2() {
        webTestClient.get()
                .uri("/mono")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(String.class)
                .consumeWith(entityExchangeResult -> {
                    var responseBody = entityExchangeResult.getResponseBody();
                    assertEquals("Hello-world",responseBody);
                });
    }

    @Test
    void stream() {
        var responseBody = webTestClient.get()
                .uri("/stream")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(0L,1L,2L,3L)
                .thenCancel()
                .verify();
    }
}