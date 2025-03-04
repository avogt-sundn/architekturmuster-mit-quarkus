package quarkitecture.de.deutsche.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import io.quarkus.test.Mock;
import quarkitecture.de.deutsche.domain.Bestellung;

class BestellServiceTest {

    @Mock
    private BestellungenRepositoryPO bestellungenRepository;

    @InjectMocks
    private BestellService bestellService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAufgeben() {
        Bestellung bestellung = new Bestellung();
        when(bestellungenRepository.save(bestellung)).thenReturn(bestellung);

        Bestellung result = bestellService.aufgeben(bestellung);

        assertThat(result).isEqualTo(bestellung);
        verify(bestellungenRepository, times(1)).save(bestellung);
    }

    @Test
    void testStornieren() {
        Bestellung bestellung = new Bestellung();

        bestellService.stornieren(bestellung);

        verify(bestellungenRepository, times(1)).delete(bestellung);
    }
}