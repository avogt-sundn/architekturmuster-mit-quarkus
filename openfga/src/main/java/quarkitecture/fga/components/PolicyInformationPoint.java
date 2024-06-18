package quarkitecture.fga.components;

public abstract class PolicyInformationPoint {

    protected PolicyDecisionPoint pdp;

    protected PolicyInformationPoint(PolicyDecisionPoint pdp) {
        this.pdp = pdp;
    }
}
