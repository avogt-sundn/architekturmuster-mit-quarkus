package avogt.quarkus.example;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
class Arbeitsversion {

    @Id
    @GeneratedValue
    Long id;

    @JdbcTypeCode(value = SqlTypes.JSON)
    String jsonString;
}