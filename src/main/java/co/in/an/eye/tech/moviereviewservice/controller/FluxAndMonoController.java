package co.in.an.eye.tech.moviereviewservice.controller;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class FluxAndMonoController {

    @GetMapping("/flux")
    public Flux<String> getFlux() {
        return Flux.just("Sumeet", "Nathe")
                .log();
    }

    @GetMapping("/mono")
    public Mono<String> getHelloWorldfromMono() {
        return Mono.just("Hello-world")
                .log();
    }

    @GetMapping(value = "/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> stream() {
        return Flux.interval(Duration.ofSeconds(1))
                .log();
    }
}
