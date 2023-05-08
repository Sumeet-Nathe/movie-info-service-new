package co.in.an.eye.tech.movieinfoservice.intg.controller;

import co.in.an.eye.tech.movieinfoservice.domain.MovieInfo;
import co.in.an.eye.tech.movieinfoservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class MoviesInfoControllerIntgTest {

    private final String MOVIE_INFO_URL = "/v1/movieInfos";
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void addMovieInfo() {

        var movie = new MovieInfo(null, "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"));
        webTestClient.post()
                .uri(MOVIE_INFO_URL)
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(MovieInfo.class)
                .consumeWith(movieInfo -> {
                    assert movieInfo.getResponseBody().getMovieInfoId() != null;
                });
    }

    @Test
    void getAllMovies() {

        webTestClient.get()
                .uri(MOVIE_INFO_URL)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(3);


    }

   /* @Test
    void getMoviesByYear() {
        var year = UriComponentsBuilder.fromUriString(MOVIE_INFO_URL)
                .queryParam("year", 2005)
                .buildAndExpand().toUri();

        System.out.printf("Formed URL :"+year);

        webTestClient.get()
                .uri(year)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(MovieInfo.class)
                .hasSize(1);


    }*/

    @Test
    void getMovieInfoById() {

        webTestClient.get()
                .uri(MOVIE_INFO_URL+"/abc")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(MovieInfo.class)
                .isEqualTo(new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));
    }

    @Test
    void updateMovieInfoById() {

        var movie = new MovieInfo(null, "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"));
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
    void updateMovieInfoByIdValidation() {

        var movie = new MovieInfo("movieId", "The Dark Knight",
                2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"));

        webTestClient.put()
                .uri(MOVIE_INFO_URL+"/def")
                .bodyValue(movie)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void deleteMovieInfoById() {
        webTestClient.delete()
                .uri(MOVIE_INFO_URL+"/abc")
                .exchange()
                .expectStatus()
                .isOk();
    }
}