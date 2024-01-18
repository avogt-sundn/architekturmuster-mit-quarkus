package organisationen.versioning;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * In order to define the composite primary keys, we should follow some rules:
 *
 * The composite primary key class must be public.
 * It must have a no-arg constructor.
 * It must define the equals() and hashCode() methods.
 * It must be Serializable.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KatalogId implements Serializable {
    public Long id;
    public Boolean isArbeitsversion;
}