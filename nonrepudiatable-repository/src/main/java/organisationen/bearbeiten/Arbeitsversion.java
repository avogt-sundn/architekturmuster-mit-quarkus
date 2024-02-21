package organisationen.bearbeiten;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import organisationen.suchen.modell.Organisation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Arbeitsversion {

    Long id;

    UUID fachschluessel;

    Organisation organisation;

    @Builder.Default
    @JsonbDateFormat(JsonbDateFormat.DEFAULT_FORMAT)
    private LocalDateTime createdAt = LocalDateTime.now();

}
