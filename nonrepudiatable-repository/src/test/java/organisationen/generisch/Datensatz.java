package organisationen.generisch;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import organisationen.suchen.modell.Organisation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class Datensatz2 {

    public Long id;
    public UUID fachschluessel;
    public Organisation organisation;
}