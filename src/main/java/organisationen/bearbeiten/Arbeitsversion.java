package organisationen.bearbeiten;

import java.time.LocalDate;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lombok.Builder;
import lombok.Data;
import organisationen.suchen.modell.Organisation;

@Data
@Builder
public class Arbeitsversion {

    Long id;

    Long organisationId;

    Organisation organisation;

    @Builder.Default
    @JsonbDateFormat("dd-MM-yyyy")
    private LocalDate createdAt = LocalDate.now();

}
