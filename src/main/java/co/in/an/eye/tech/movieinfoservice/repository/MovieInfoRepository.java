package co.in.an.eye.tech.movieinfoservice.repository;

import co.in.an.eye.tech.movieinfoservice.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo,String> {
    Flux<MovieInfo> findMovieByYear(Integer year);
}
