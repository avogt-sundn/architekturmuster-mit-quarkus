package quarkitecture.fga.shareddomain;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VorgangData {

    public Integer nummer;
    VersichertePerson versichertePerson;
    List<Aufgabe> aufgaben;
}
