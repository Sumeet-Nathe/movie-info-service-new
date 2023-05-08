package co.in.an.eye.tech.moviereviewservice.intg.repository;

import co.in.an.eye.tech.moviereviewservice.domain.MovieInfo;
import co.in.an.eye.tech.moviereviewservice.repository.MovieInfoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.AssertionErrors;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryTest {

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
    void findAll() {
        var movieInfoFluxAll = movieInfoRepository.findAll().log();

        StepVerifier.create(movieInfoFluxAll)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findById() {
        var movieInfoMono = movieInfoRepository.findById("abc").log();

        StepVerifier.create(movieInfoMono)
                //.expectNextCount(1)
                .assertNext(movieInfo -> {
                    Assertions.assertEquals("Dark Knight Rises", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void saveMovieInfo() {

        var chapter1 = new MovieInfo(null, "Batman Begins-chapter1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        var movieInfoMono = movieInfoRepository.save(chapter1).log();

        StepVerifier.create(movieInfoMono)
                //.expectNextCount(1)
                .assertNext(movieInfo -> {
                    Assertions.assertNotNull(movieInfo.getMovieInfoId());
                    Assertions.assertEquals("Batman Begins-chapter1", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void updateMovieInfo() {

        var chapter2 = movieInfoRepository.findById("abc").block();
        chapter2.setName("Batman Begins-chapter2");

        var movieInfoMono = movieInfoRepository.save(chapter2).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    Assertions.assertNotNull(movieInfo.getMovieInfoId());
                    Assertions.assertEquals("Batman Begins-chapter2", movieInfo.getName());
                })
                .verifyComplete();
    }

    @Test
    void deleteById() {
        var movieInfoabc = movieInfoRepository.deleteById("abc").log().block();

        Flux<MovieInfo> log = movieInfoRepository.findAll().log();

        StepVerifier.create(log)
                .expectNextCount(2)
                .verifyComplete();
    }

}