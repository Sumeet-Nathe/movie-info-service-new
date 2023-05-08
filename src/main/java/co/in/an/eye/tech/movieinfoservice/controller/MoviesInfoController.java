package co.in.an.eye.tech.movieinfoservice.controller;


import co.in.an.eye.tech.movieinfoservice.domain.MovieInfo;
import co.in.an.eye.tech.movieinfoservice.service.MovieInfoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@Slf4j
public class MoviesInfoController {

    @Autowired
    MovieInfoService movieInfoService;

    @PostMapping("/movieInfos")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieInfo addMovieInfo(@Valid @RequestBody MovieInfo movieInfo){

        MovieInfo savedMovieInfo = movieInfoService.addMovieInfo(movieInfo);
        return savedMovieInfo;

    }

    @GetMapping("/movieInfos")
    @ResponseStatus(HttpStatus.OK)
    public Flux<MovieInfo> getAllMovies(@RequestParam(value = "year", required = false) Integer year){
        log.info("Year is : {}",year);
        if(year != null){
            return movieInfoService.getMoviesByYear(year);
        }
        return movieInfoService.getAllMovies();
    }

    @GetMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id){
        return movieInfoService.getMovieInfoById(id)
                .map(movieInfo -> ResponseEntity.ok().body(movieInfo))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @PutMapping("/movieInfos/{id}")
    public Mono<ResponseEntity<MovieInfo>> updateMovieInfoById(@PathVariable String id, @RequestBody MovieInfo movieInfo){
        return movieInfoService.updateMovieInfo(id,movieInfo)
                .map(ResponseEntity.ok()::body)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()))
                .log();
    }

    @DeleteMapping("/movieInfos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteMovieInfoById(@PathVariable String id){
        return movieInfoService.deleteMovieInfoById(id);
    }
}
