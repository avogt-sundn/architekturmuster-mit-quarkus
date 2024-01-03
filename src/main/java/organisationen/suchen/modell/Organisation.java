package organisationen.suchen.modell;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Organisation {
    Long id;

    @NotBlank
    String name;
    String nummer;

    @NotBlank
    String beschreibung;
    Set<Adresse> adressen;
}
