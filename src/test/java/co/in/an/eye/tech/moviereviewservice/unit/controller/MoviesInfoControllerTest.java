package co.in.an.eye.tech.moviereviewservice.unit.controller;


import co.in.an.eye.tech.moviereviewservice.controller.MoviesInfoController;
import co.in.an.eye.tech.moviereviewservice.domain.MovieInfo;
import co.in.an.eye.tech.moviereviewservice.service.MovieInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerTest {

    private final String MOVIE_INFO_URL = "/v1/movieInfos";

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    MovieInfoService movieInfoService;

    @Test
    public void getAllMovieInfos(){

        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        when(movieInfoService.getAllMovies()).thenReturn(Flux.fromIterable(movieinfos));

        webTestClient.get()
                .uri("/v1/movieInfos")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);
    }

    @Test
    public void getMovieInfosById(){

        var movieId = "abc";

        var movieinfos = new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

        when(movieInfoService.getMovieInfoById(movieId)).thenReturn(Mono.just(movieinfos));

        webTestClient.get()
                .uri("/v1/movieInfos/"+movieId)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);
                /*.consumeWith(listEntityExchangeResult -> {
                    List<MovieInfo> responseBody = listEntityExchangeResult.getResponseBody();
                });*/
    }

    @Test
    void addMovieInfo() {

        var movie = new MovieInfo("movieId", "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"));

        when(movieInfoService.addMovieInfo(isA(MovieInfo.class))).thenReturn(movie);

        webTestClient.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfo -> {
                    assert movieInfo.getResponseBody().getMovieInfoId() != null;
                    assertEquals(movieInfo.getResponseBody().getMovieInfoId(),"movieId");
                });
    }

    @Test
    void updateMovieInfoById() {

        var movie = new MovieInfo("movieId", "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"));

        when(movieInfoService.updateMovieInfo(isA(String.class),isA(MovieInfo.class))).thenReturn(Mono.just(movie));

        webTestClient.put()
                .uri(MOVIE_INFO_URL+"/abc")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfo -> {
                    var response = movieInfo.getResponseBody();
                    assert response.getMovieInfoId() != null;
                    assertEquals("The Dark Knight",response.getName());
                });
    }


    @Test
    void deleteMovieInfoById() {
        var movieId = "abc";

        when(movieInfoService.deleteMovieInfoById(movieId)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(MOVIE_INFO_URL+"/"+movieId)
                .exchange()
                .expectStatus()
                .isOk();
    }

}
