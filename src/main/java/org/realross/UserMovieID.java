package org.realross;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode
public class UserMovieID implements Serializable {
    private Integer UserID;
    private Integer MovieID;
}