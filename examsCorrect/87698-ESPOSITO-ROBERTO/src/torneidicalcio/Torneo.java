package torneidicalcio;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class Torneo {
	String nomeTorneo;
	int numMaxSquadre;
	Map<String, Squadra> squadreIscritte = new TreeMap <String, Squadra>();
	
	public Torneo(String nomeTorneo, int numMaxSquadre) {
		
		this.nomeTorneo = nomeTorneo;
		this.numMaxSquadre = numMaxSquadre;
	}


	public void setNomeTorneo(String nomeTorneo) {
		this.nomeTorneo = nomeTorneo;
	}

	public void setNumMaxSquadre(int numMaxSquadre) {
		this.numMaxSquadre = numMaxSquadre;
	}

	public String getNome() {
		return nomeTorneo;
	}

	public int getNumeroSquadre() {
		return numMaxSquadre;
	}
	public void addSquadra (String nomeSquadraIscritta, Squadra s){
		squadreIscritte.put(nomeSquadraIscritta, s);
			}
	public Collection<Squadra> elencoSquadreIscritteTorneo(){
		return squadreIscritte.values();
	}

}
