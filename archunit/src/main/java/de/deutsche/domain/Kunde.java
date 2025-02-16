package de.deutsche.domain;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Kunde {
    public Kundennummer id;
    public String name;
}

class Kundennummer {
    UUID id;
}
