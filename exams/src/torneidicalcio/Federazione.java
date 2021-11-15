package torneidicalcio;

import java.util.*;

public class Federazione {
	private String sigla;
	private String denominazione;
	private String sitoWeb;
	private int numeroTessera = 1000;
	Map<String, Torneo> tornei = new HashMap <String, Torneo>(); 
	Map<String, Squadra> squadre = new TreeMap <String, Squadra>(); 
	Map<String, Tesserato> tesserati = new TreeMap <String, Tesserato>();
	Map<Integer, Tesserato> tesseratiPerTessera = new TreeMap <Integer, Tesserato>();
	
	
	public Federazione(String sigla, String denominazione, String sitoWeb){
	this.sigla= sigla;
	this.denominazione =denominazione;
	this.sitoWeb = sitoWeb;
	}
	
	
	public String getSigla() {
		return sigla;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public String getSitoWeb() {
		return sitoWeb;
	}

	public Torneo nuovoTorneo(String nomeTorneo, int numeroSquadre){
	Torneo t = new Torneo (nomeTorneo, numeroSquadre);
	tornei.put(nomeTorneo, t);
	return t;
	}

	public void nuovaSquadra(String nome, String citta, int anno, String stadio){
	Squadra squadra =squadre.get(nome);
	if (squadra!= null) return;
	else 
	{Squadra s = new Squadra (nome, citta, anno, stadio);
     squadre.put(nome, s);	
     }
	
	}

	public Squadra cercaSquadra(String nomeSquadra){
		Squadra s =squadre.get(nomeSquadra);
		if (s==null) return null;
		return squadre.get(nomeSquadra);
	}

	public Collection<Torneo> elencoTornei(){
		return tornei.values();
	}

	public Collection<Squadra> elencoSquadre(){
		return squadre.values();
	}
	
	public void iscriviSquadraTorneo(String nomeTorneo, String nomeSquadra){
	Squadra s = squadre.get(nomeSquadra);
		Torneo t = tornei.get(nomeTorneo);
		t.addSquadra(nomeSquadra,s);
		s.setTorneo(t);
			
	}

	public Collection<Squadra> elencoSquadreTorneo(String nomeTorneo){
		Torneo t = tornei.get(nomeTorneo);
		return t.elencoSquadreIscritteTorneo();
	}
	
	public int tesseramento(String nome, String cognome, String nomeSquadra, String ruolo) throws EccezioneErroreDatiTesseramento{
		if ((nome!=null) && (cognome!=null) && (nomeSquadra!=null) && (ruolo!=null))
				{Tesserato t;
				Squadra s = squadre.get(nomeSquadra);
			    t = new Dirigente (nome, cognome, s, ruolo, numeroTessera);
				tesserati.put(nome+cognome, t);
				tesseratiPerTessera.put(numeroTessera, t);
				s.addTesserato(t);
				numeroTessera++;
	return numeroTessera-1;}
		else throw new EccezioneErroreDatiTesseramento();
		
	}

	public int tesseramento(String nome, String cognome, String nomeSquadra, String ruolo, int numeroMaglia) throws EccezioneErroreDatiTesseramento{
		if ((nome!=null) && (cognome!=null) && (nomeSquadra!=null) && (ruolo!=null) && (numeroMaglia>0))
		{Tesserato t;
		Squadra s = squadre.get(nomeSquadra);
	    t = new Calciatore (nome, cognome, s, ruolo, numeroMaglia, numeroTessera);
		tesserati.put(nome+cognome, t);	
		tesseratiPerTessera.put(numeroTessera, t);
		s.addTesserato(t);
		numeroTessera++;
		return numeroTessera-1;}
	else throw new EccezioneErroreDatiTesseramento();
	
}
	
	public int tesseramento(String nome, String cognome, String sezione) throws EccezioneErroreDatiTesseramento{
		if ((nome!=null) && (cognome!=null) && (sezione!=null))
		{Tesserato t;
		  t = new Arbitro (nome, cognome, sezione, numeroTessera);
		tesserati.put(nome+cognome, t);
		tesseratiPerTessera.put(numeroTessera, t);
		numeroTessera++;
		return numeroTessera-1;}
	else throw new EccezioneErroreDatiTesseramento();
	
}

	public Tesserato cercaTesseratoPerNumeroTessera(int numeroTessera) throws EccezioneTesseratoInesistente{
		Tesserato t =tesseratiPerTessera.get(numeroTessera);
		if (t!=null)
		return t;
		else throw new EccezioneTesseratoInesistente();
	}

	public Tesserato cercaTesseratoPerNomeCognome(String nome, String cognome) throws EccezioneTesseratoInesistente{
		Tesserato t= tesserati.get(nome+cognome);
		if (t!=null)
			return t;
			else throw new EccezioneTesseratoInesistente();
		}
	
	public Collection<Tesserato> elencoTesseratiSquadra(String nomeSquadra){
		
		return null;
	}
	
	public Incontro nuovoIncontro(String nomeTorneo, int giornata, String nomeSquadraCasa, String nomeSquadraOspite, String risultato, String nomeArbitro, String cognomeArbitro){
		Squadra squadraCasa=squadre.get(nomeSquadraCasa);
		Squadra squadraOspite=squadre.get(nomeSquadraOspite);
		Incontro i = new Incontro(nomeTorneo, giornata, squadraCasa, squadraOspite, risultato);
		return null;
	}

	public Collection<Incontro> elencoIncontriPerGiornata(String nomeTorneo){
		return null;
	}

	public Collection<Incontro> elencoIncontriPerDifferenzaReti(String nomeTorneo){
		return null;
	}

	public int puntiSquadra(String nomeSquadra){
			Squadra s = squadre.get(nomeSquadra);
			return s.getPunti();
	}
	
	public String classificaTorneo(String nomeTorneo){
		return null;
	}
}
