package torneidicalcio;
import java.util.*;

public class Squadra {
	String nomeSquadra;
	String citta;
	int anno;
	String stadio;
	Torneo torneo;
	int punti;
	List <Tesserato> tesserati = new LinkedList <Tesserato>();

	public String getNome() {
		return nomeSquadra;
	}

	public String getCitta() {
		return citta;
	}
	public int getAnno() {
		return anno;
	}
	public int getPunti() {
		return punti;
	}
	public String getStadio() {
		return stadio;
	}
	
	public void setNomeSquadra(String nomeSquadra) {
		this.nomeSquadra = nomeSquadra;
	}

	public void setCitta(String citta) {
		this.citta = citta;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public void setStadio(String stadio) {
		this.stadio = stadio;
	}
	public void setTorneo(Torneo torneo) {
		this.torneo = torneo;
	}
	public Squadra(String nomeSquadra, String citta, int anno, String stadio) {
		super();
		this.nomeSquadra = nomeSquadra;
		this.citta = citta;
		this.anno = anno;
		this.stadio = stadio;
	}
	public void addTesserato(Tesserato t) {
		tesserati.add(t);
	}
	
}
