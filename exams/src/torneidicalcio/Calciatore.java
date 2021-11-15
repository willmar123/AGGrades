package torneidicalcio;

public class Calciatore extends Tesserato{
	Squadra squadra;
	String ruolo;
	int numeroMaglia;
	int numeroTessera;

	public Calciatore(String nome, String cognome, Squadra squadra, String ruolo, int numeroMaglia, int numeroTessera) {
		super(nome, cognome);
		this.squadra=squadra;
		this.ruolo=ruolo;
		this.numeroMaglia= numeroMaglia;
		this.numeroTessera=numeroTessera;
		// TODO Auto-generated constructor stub
	}

	public Squadra getSquadra() {
		return squadra;
	}

	public String getRuolo() {
		return ruolo;
	}

	public int getNumeroMaglia() {
		return numeroMaglia;
	}
	
}

