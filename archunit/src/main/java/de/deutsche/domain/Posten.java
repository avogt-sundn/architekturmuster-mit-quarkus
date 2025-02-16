package de.deutsche.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(fluent = true)
public class Posten {
    public Produkt produkt;
    public int anzahl;
}
