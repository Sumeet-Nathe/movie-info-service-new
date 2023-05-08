package co.in.an.eye.tech.moviereviewservice.repository;

import co.in.an.eye.tech.moviereviewservice.domain.MovieInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo,String> {
}
