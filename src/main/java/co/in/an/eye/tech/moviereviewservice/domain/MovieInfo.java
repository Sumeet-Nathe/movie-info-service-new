package co.in.an.eye.tech.moviereviewservice.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class MovieInfo {
    @Id
    private String movieInfoId;
    @NotBlank(message = "movieInfo.name must not be null or empty!")
    private String name;
    @NotNull(message = "movieInfo.year must not be null or empty!")
    @Positive(message = "movieInfo.year must not be negative!")
    private Integer year;
    private List<String>cast;
    private LocalDate releaseDate;
}
