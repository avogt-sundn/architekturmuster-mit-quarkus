package quarkitecture.fga.prozesse;

import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.components.PolicyInformationPoint;
import quarkitecture.fga.shareddomain.Aufgabe;
import quarkitecture.fga.shareddomain.Vorgang;

public class InformationPointForProzesse extends PolicyInformationPoint {

    public InformationPointForProzesse(PolicyDecisionPoint pip) {
        super(pip);
    }

    public void neueAufgabe(Aufgabe aufgabe) {
        pdp.acceptInformation(this, NeueAufgabe.builder().aufgabe(aufgabe).build());
    }

    public void neuerVorgang(Vorgang vorgang) {
        pdp.acceptInformation(this, NeuerVorgang.builder().vorgang(vorgang).build());
    }
}
