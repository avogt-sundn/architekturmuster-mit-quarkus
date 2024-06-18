import quarkitecture.fga.components.PolicyDecisionPoint;
import quarkitecture.fga.components.PolicyInformationPoint;
import quarkitecture.fga.shareddomain.VorgangData;

public class InformationPointForProzesse extends PolicyInformationPoint {

    public InformationPointForProzesse(PolicyDecisionPoint pip) {
        super(pip);
    }

    public void neuerVorgang(VorgangData vorgang) {
        pdp.acceptInformation(this, NeuerVorgang.builder().vorgang(vorgang).build());
    }
}
