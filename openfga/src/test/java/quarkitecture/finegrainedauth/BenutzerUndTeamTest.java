package quarkitecture.finegrainedauth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import quarkitecture.fga.arbeitsverteilung.Aufgabenart;
import quarkitecture.fga.arbeitsverteilung.InformationPointForArbeitsverteilung;
import quarkitecture.fga.benutzerundteams.InformationPointForBenutzerAndTeam;
import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.shareddomain.Anfrage;
import quarkitecture.fga.shareddomain.BenutzerData;
import quarkitecture.fga.shareddomain.BenutzerFromToken;
import quarkitecture.fga.shareddomain.Team;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class BenutzerUndTeamTest {

    PolicyDecisionPoint pdp = new PolicyDecisionPoint();
    InformationPointForBenutzerAndTeam pipForBenutzer = new InformationPointForBenutzerAndTeam(pdp);
    InformationPointForArbeitsverteilung pipForArbv = new InformationPointForArbeitsverteilung(
            pdp);
    BenutzerFromToken benutzerFromToken = BenutzerFromToken.builder().mandantId("1").name("Rolf").build();

    @Test
    void IstBenutzerImTeam() {

        // Arrange
        pipForBenutzer.neuerBenutzer(
                BenutzerData.builder().name("Rolf")
                        .istTeilVonTeam(Team.builder().bezeichnung("Helden").build()).build());

        // Assert
        assertThat(pdp.lastReceivedEvent()).contains("Rolf");
    }

    @Test
    void DarfAufgabeSehen() {
        // Arrange
        Aufgabenart wischen = Aufgabenart.builder().art("wischen").build();
        pipForArbv.neueArbeitsverteilung(
                Team.builder().bezeichnung("Helden").build(),
                wischen);
        // Act
        assertThat(pdp.lastReceivedEvent()).contains("Helden");
        assertThat(pdp.lastReceivedEvent()).contains("wischen");
        assertThat(
                pdp.verarbeiteAnfrage(
                        Anfrage.builder()
                                .wer(pdp.benutzerMitName("Rolf"))
                                .was(wischen)
                                .wo(pdp.vorgangZuNummer(7))
                                .build())).isTrue();

    }
}
