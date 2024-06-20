package quarkitecture.fga.shareddomain;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vorgang {

    public Integer nummer;
    VersichertePerson versichertePerson;
    @Builder.Default
    List<Aufgabe> aufgaben = new ArrayList<>();
}
