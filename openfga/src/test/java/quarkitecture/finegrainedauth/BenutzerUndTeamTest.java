package quarkitecture.finegrainedauth;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import quarkitecture.fga.arbeitsverteilung.Aufgabenart;
import quarkitecture.fga.arbeitsverteilung.InformationPointForArbeitsverteilung;
import quarkitecture.fga.benutzerundteams.InformationPointForBenutzerAndTeam;
import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.prozesse.InformationPointForProzesse;
import quarkitecture.fga.shareddomain.Anfrage;
import quarkitecture.fga.shareddomain.Aufgabe;
import quarkitecture.fga.shareddomain.BenutzerData;
import quarkitecture.fga.shareddomain.BenutzerFromToken;
import quarkitecture.fga.shareddomain.Team;
import quarkitecture.fga.shareddomain.Vorgang;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class BenutzerUndTeamTest {

    PolicyDecisionPoint pdp = new PolicyDecisionPoint();
    InformationPointForBenutzerAndTeam pipForBenutzer = new InformationPointForBenutzerAndTeam(pdp);
    InformationPointForArbeitsverteilung pipForArbv = new InformationPointForArbeitsverteilung(
            pdp);
    InformationPointForProzesse pipForProz = new InformationPointForProzesse(pdp);

    BenutzerFromToken benutzerFromToken = BenutzerFromToken.builder()
            .mandantId("1").name("Rolf").build();

    @Test
    @Order(1)
    void IstBenutzerImTeam() {

        // Arrange
        pipForBenutzer.neuerBenutzer(
                BenutzerData.builder().name("Rolf")
                        .istTeilVonTeam(Team.builder().bezeichnung("Helden").build()).build());

        // Assert
        assertThat(pdp.benutzerMitName("Rolf").getIstTeilVonTeam().getBezeichnung()).isEqualTo("Helden");
    }

    @Test
    @Order(2)
    void DarfAufgabeSehen() {
        // Arrange
        Aufgabenart wischen = Aufgabenart.builder().art("wischen").build();
        pipForArbv.neueArbeitsverteilung(
                Team.builder().bezeichnung("Helden").build(),
                wischen);

        Vorgang vorgang = Vorgang.builder().nummer(7).build();
        pipForProz.neuerVorgang(vorgang);
        Aufgabe aufgabe = Aufgabe.builder().vorgang(vorgang).aufgabenart(wischen).build();
        vorgang.getAufgaben().add(aufgabe);
        pipForProz.neueAufgabe(aufgabe);

        // Act

        assertThat(
                pdp.erfrageZugriff(
                        Anfrage.builder()
                                .wer(pdp.benutzerMitName("Rolf"))
                                .was(wischen)
                                .wo(pdp.vorgangZuNummer(7))
                                .build())).isTrue();

    }
}
