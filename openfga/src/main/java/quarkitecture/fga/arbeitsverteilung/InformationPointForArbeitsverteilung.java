package quarkitecture.fga.arbeitsverteilung;

import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.components.PolicyInformationPoint;
import quarkitecture.fga.shareddomain.Team;

public class InformationPointForArbeitsverteilung extends PolicyInformationPoint {

    public InformationPointForArbeitsverteilung(PolicyDecisionPoint pip) {
        super(pip);
    }

    public void neueArbeitsverteilung(Team team, Aufgabenart aufgabenart) {
        pdp.acceptInformation(this, NeueTeamZuweisung.builder().team(team).aufgabenart(aufgabenart).build());
    }
}
