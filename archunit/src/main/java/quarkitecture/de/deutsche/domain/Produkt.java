package quarkitecture.de.deutsche.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Produkt {
    public ProduktId id;
    public String name;
    public int preis;
    public int anzahlImLager;
}
