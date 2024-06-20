package quarkitecture.fga.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quarkitecture.fga.arbeitsverteilung.InformationPointForArbeitsverteilung;
import quarkitecture.fga.arbeitsverteilung.NeueTeamZuweisung;
import quarkitecture.fga.benutzerundteams.InformationPointForBenutzerAndTeam;
import quarkitecture.fga.prozesse.InformationPointForProzesse;
import quarkitecture.fga.prozesse.NeueAufgabe;
import quarkitecture.fga.prozesse.NeuerVorgang;
import quarkitecture.fga.shareddomain.Anfrage;
import quarkitecture.fga.shareddomain.Aufgabe;
import quarkitecture.fga.shareddomain.BenutzerData;
import quarkitecture.fga.shareddomain.Vorgang;

public class PolicyDecisionPoint {
    List<PolicyInformationPoint> registriertePIPs;
    private List<String> events = new ArrayList<>();

    HashMap<String, Aufgabe> meineAufgaben = new HashMap<>();

    /**
     * integrate the newly received information into the facts base.
     *
     * @param sender
     * @param neueAufgabe
     */
    public void acceptInformation(PolicyInformationPoint sender, NeueAufgabe neueAufgabe) {
        meineAufgaben.put(neueAufgabe.toString(), neueAufgabe.getAufgabe());
    }

    public void acceptInformation(InformationPointForBenutzerAndTeam sender, NeuerBenutzer update) {
        meineBenutzer.put(update.benutzer.name, update.benutzer);
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

    public boolean erfrageZugriff(Anfrage anfrage) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    HashMap<Integer, Vorgang> meineVorgaenge = new HashMap<>();

    public void acceptInformation(InformationPointForProzesse sender, NeuerVorgang neuerVorgang) {
        meineVorgaenge.put(neuerVorgang.vorgang.nummer, neuerVorgang.vorgang);
    }

    public Vorgang vorgangZuNummer(Integer nummer) {
        return meineVorgaenge.computeIfAbsent(nummer, error -> {
            throw new UnbekanntException("Vorgang mit Nummer: " + nummer);
        });
    }
}
