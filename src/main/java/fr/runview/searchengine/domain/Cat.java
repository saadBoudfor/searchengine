package fr.runview.searchengine.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@ToString
@Data
@Entity
public class Cat {
    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("sexe")
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @JsonProperty("race")
    private String race;

    @JsonProperty("prix")
    private Double price;

    @JsonProperty("commentaires")
    private String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("neLe")
    private LocalDate birthDay;
}
