package co.in.an.eye.tech.movieinfoservice.service;

import co.in.an.eye.tech.movieinfoservice.domain.MovieInfo;
import co.in.an.eye.tech.movieinfoservice.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    public MovieInfo addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo).log().block();
    }

    public Flux<MovieInfo> getAllMovies() {
        return movieInfoRepository.findAll();
    }


    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Mono<Void> deleteMovieInfoById(String id) {
        return movieInfoRepository.deleteById(id);
    }

    public Mono<MovieInfo> updateMovieInfo(String id, MovieInfo movieInfo) {
        return movieInfoRepository.findById(id)
                .flatMap(movieInfo1 -> {
                    movieInfo1.setName(movieInfo.getName());
                    movieInfo1.setCast(movieInfo.getCast());
                    movieInfo1.setYear(movieInfo.getYear());
                    movieInfo1.setReleaseDate(movieInfo.getReleaseDate());
                    return movieInfoRepository.save(movieInfo1);
                });
    }

    public Flux<MovieInfo> getMoviesByYear(Integer year) {
        return movieInfoRepository.findMovieByYear(year);
    }
}
