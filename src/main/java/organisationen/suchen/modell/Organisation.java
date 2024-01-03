package organisationen.suchen.modell;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Organisation {
    Long id;

    UUID fachschluessel;
    @NotBlank
    String name;
    String nummer;

    @NotBlank
    String beschreibung;
    Set<Adresse> adressen;
}
