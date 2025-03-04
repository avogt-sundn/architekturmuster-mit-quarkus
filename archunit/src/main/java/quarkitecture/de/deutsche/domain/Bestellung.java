package quarkitecture.de.deutsche.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bestellung {
    public UUID uid;
    public LocalDateTime datum;
    public List<Posten> posten;

    public int numberOfItems() {

        return Optional.ofNullable(posten).stream().flatMap(List::stream).mapToInt(Posten::anzahl).sum();

    }
}
