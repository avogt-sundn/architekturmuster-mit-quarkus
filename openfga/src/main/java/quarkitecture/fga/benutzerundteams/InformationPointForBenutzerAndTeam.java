package quarkitecture.fga.benutzerundteams;

import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.components.PolicyInformationPoint;
import quarkitecture.fga.payloads.Update;
import quarkitecture.fga.shareddomain.BenutzerData;

public class InformationPointForBenutzerAndTeam extends PolicyInformationPoint {


    public InformationPointForBenutzerAndTeam(PolicyDecisionPoint pdp) {
        super(pdp);
    }

    public void neuerBenutzer(BenutzerData benutzerData) {
        pdp.acceptInformation(this, Update.builder().payload(benutzerData.toUpdate()).build());
    }

}
