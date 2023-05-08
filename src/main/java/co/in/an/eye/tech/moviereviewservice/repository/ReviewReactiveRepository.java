package co.in.an.eye.tech.moviereviewservice.repository;

import co.in.an.eye.tech.moviereviewservice.domain.Review;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {

    //Flux<Review> findReviewsByMovieInfoId(String reviewId);

    Flux<Review> findReviewsByMovieInfoId(Long movieInfoId);
}
