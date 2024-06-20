package quarkitecture.fga.benutzerundteams;

import quarkitecture.fga.components.NeuerBenutzer;
import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.components.PolicyInformationPoint;
import quarkitecture.fga.shareddomain.BenutzerData;

public class InformationPointForBenutzerAndTeam extends PolicyInformationPoint {

    public InformationPointForBenutzerAndTeam(PolicyDecisionPoint pdp) {
        super(pdp);
    }

    public void neuerBenutzer(BenutzerData benutzerData) {
        pdp.acceptInformation(this, NeuerBenutzer.builder().benutzer(benutzerData).build());
    }

}
