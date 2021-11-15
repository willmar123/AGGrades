package torneidicalcio;

public class Dirigente extends Tesserato {
	Squadra squadra;
	String ruolo;
	 int numeroTessera;
	
	public Dirigente(String nome, String cognome, Squadra squadra, String ruolo, int numeroTessera) {
		super(nome, cognome);
		this.squadra=squadra;
		this.ruolo=ruolo;
		this.numeroTessera=numeroTessera;
		
		// TODO Auto-generated constructor stub
	}

	public Squadra getSquadra() {
		return squadra;
	}

	public String getRuolo() {
		return ruolo;
	}
	
}
