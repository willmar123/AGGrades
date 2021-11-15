package torneidicalcio;

public class Incontro {
	String nomeTorneo;
	int giornata;
	Squadra SquadraCasa;
	Squadra SquadraOspite;
	String risultato;
	String nomeArbitro;
	String cognomeArbitro;
	int numeroGolNumeroGolSquadraCasa;
	int NumeroGolSquadraOspite;
	
	public Incontro(String nomeTorneo, int giornata, Squadra squadraCasa,
			Squadra squadraOspite, String risultato) {
		
		this.nomeTorneo = nomeTorneo;
		this.giornata = giornata;
		SquadraCasa = squadraCasa;
		SquadraOspite = squadraOspite;
		this.risultato = risultato;
		
	}

	public int getGiornata() {
		return giornata;
	}

	public Squadra getSquadraCasa() {
		return SquadraCasa;
	}

	public Squadra getSquadraOspite() {
		return SquadraOspite;
	}

	public int getNumeroGolSquadraCasa() {
		String risultato;
		return -1;
	}

	public int getNumeroGolSquadraOspite() {
		String risultato;
		return +1;
	}

	public String getArbitro() {
		return nomeArbitro+" "+cognomeArbitro;
	}

}
