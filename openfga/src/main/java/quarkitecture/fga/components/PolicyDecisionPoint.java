package quarkitecture.fga.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.resteasy.spi.NotImplementedYetException;

import quarkitecture.fga.arbeitsverteilung.InformationPointForArbeitsverteilung;
import quarkitecture.fga.arbeitsverteilung.NeueTeamZuweisung;
import quarkitecture.fga.benutzerundteams.InformationPointForBenutzerAndTeam;
import quarkitecture.fga.payloads.Update;
import quarkitecture.fga.prozesse.InformationPointForProzesse;
import quarkitecture.fga.prozesse.NeuerVorgang;
import quarkitecture.fga.shareddomain.Anfrage;
import quarkitecture.fga.shareddomain.BenutzerData;
import quarkitecture.fga.shareddomain.VorgangData;

public class PolicyDecisionPoint {
    List<PolicyInformationPoint> registriertePIPs;
    private List<String> events = new ArrayList<>();

    public void acceptInformation(PolicyInformationPoint sender, Update update) {
        throw new NotImplementedYetException();
    }

    public void acceptInformation(InformationPointForBenutzerAndTeam sender, Update update) {
        events.add("from: " + sender + " got: " + update);
    }

    public void acceptInformation(InformationPointForArbeitsverteilung sender, NeueTeamZuweisung update) {
        events.add("from: " + sender + " got: " + update);
    }

    public String lastReceivedEvent() {
        return events.get(events.size() - 1);
    }

    HashMap<String, BenutzerData> meineBenutzer = new HashMap<>();

    public BenutzerData benutzerMitName(String name) {

        return meineBenutzer.computeIfAbsent(name, error -> {
            throw new UnbekanntException("Benutzer mit Name: " + name);
        });
    }

    public boolean verarbeiteAnfrage(Anfrage anfrage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stelleAnfrage'");
    }

    HashMap<Integer, VorgangData> meineVorgaenge = new HashMap<>();

    public void acceptInformation(InformationPointForProzesse sender, NeuerVorgang neuerVorgang) {
        meineVorgaenge.put(neuerVorgang.vorgang.nummer, neuerVorgang.vorgang);
    }

    public VorgangData vorgangZuNummer(Integer nummer) {
        return meineVorgaenge.computeIfAbsent(nummer, error -> {
            throw new UnbekanntException("Vorgang mit Nummer: " + nummer);
        });
    }
}
