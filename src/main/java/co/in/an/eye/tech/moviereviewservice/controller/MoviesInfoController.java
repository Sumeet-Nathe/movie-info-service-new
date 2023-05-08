package co.in.an.eye.tech.moviereviewservice.controller;


import co.in.an.eye.tech.moviereviewservice.domain.MovieInfo;
import co.in.an.eye.tech.moviereviewservice.repository.MovieInfoRepository;
import co.in.an.eye.tech.moviereviewservice.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
public class MoviesInfoController {

    @Autowired
    MovieInfoService movieInfoService;

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieInfo addMovieInfo(@RequestBody MovieInfo movieInfo){

        MovieInfo savedMovieInfo = movieInfoService.addMovieInfo(movieInfo);
        return savedMovieInfo;

    }

    @GetMapping("/movieInfos")
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getAllMovies(){
        return movieInfoService.getAllMovies();
    }

    @GetMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> getMovieInfoById(@PathVariable String id){
        return movieInfoService.getMovieInfoById(id);
    }

    @PutMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<MovieInfo> updateMovieInfoById(@PathVariable String id,@RequestBody MovieInfo movieInfo){
        return movieInfoService.updateMovieInfo(id,movieInfo);
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id){
        return movieInfoService.deleteMovieInfoById(id);
    }
}
