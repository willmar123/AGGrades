import torneidicalcio.*;

import java.util.*;

public class Esempio {

	public static void main(String[] args) throws EccezioneTesseratoInesistente, EccezioneErroreDatiTesseramento {

		Federazione f;
		Torneo t;
		Squadra s;
		LinkedList<Torneo> tornei;
		LinkedList<Squadra> squadre;
		int numeroTessera;
		Tesserato tesserato;
		LinkedList<Tesserato> tesserati;
		LinkedList<Incontro> incontri;
		int punti;
		String classifica;
		
		System.out.println("#######################################");
		System.out.println("#          R1. Tornei e squadre       #");
		System.out.println("#######################################\n");

		System.out.println("Definita federazione:");

		f = new Federazione("FIGC", "Federazione Italiana Giuoco Calcio", "http://www.figc.it/");

		System.out.println(" Sigla:    "+f.getSigla());
		System.out.println(" Nome:     "+f.getDenominazione());
		System.out.println(" Sito Web: "+f.getSitoWeb());
		
		System.out.println("\nDefinito nuovo torneo:");

		t = f.nuovoTorneo("Serie A", 20);
		
		System.out.println(" Nome:    "+t.getNome());
		System.out.println(" Squadre: "+t.getNumeroSquadre());

		System.out.println("\nDefinita nuova squadra.");

		f.nuovaSquadra("Juventus", "Torino", 1897, "Juventus Stadium");
		
		System.out.println("\nCerca squadra:");
		
		s = f.cercaSquadra("Juventus");
		System.out.println(" Nome:   "+s.getNome());
		System.out.println(" Citta': "+s.getCitta());
		System.out.println(" Anno:   "+s.getAnno());
		System.out.println(" Stadio: "+s.getStadio());

		System.out.println("\nDefiniti altri tornei.");

		f.nuovoTorneo("Serie B", 22);
		f.nuovoTorneo("Lega Pro", 54);

		System.out.println("\nElenco tornei (ordine inserimento):");

		tornei = new LinkedList<Torneo>(f.elencoTornei());
		for(Torneo ttemp : tornei)
			System.out.println(" "+ttemp.getNome()+" "+ttemp.getNumeroSquadre());

		System.out.println("\nDefinite altre squadre.");

		f.nuovaSquadra("Inter", "Milano", 1908, "Giuseppe Meazza");
		f.nuovaSquadra("Lazio", "Roma", 1900, "Stadio Olimpico");
		f.nuovaSquadra("Cremonese", "Cremona", 1903, "Giovanni Zini");
		f.nuovaSquadra("Lumezzane", "Brescia", 1946, "Stadio Tullio Saleri");
		
		System.out.println("\nElenco squadre federazione (ordine alfabetico):");

		squadre = new LinkedList<Squadra>(f.elencoSquadre());
		for(Squadra stemp : squadre)
			System.out.println(" "+stemp.getNome());
		
		System.out.println("\nIscrizione 'Juventus' al torneo 'Serie A'");

		f.iscriviSquadraTorneo("Serie A", "Juventus");

		System.out.println("\nIscrizione altre squadre al torneo 'Serie A'.");

		f.iscriviSquadraTorneo("Serie A", "Inter");
		f.iscriviSquadraTorneo("Serie A", "Lazio");
		
		System.out.println("\nElenco squadre iscritte al torneo 'Serie A' (ordine alfabetico):");

		squadre = new LinkedList<Squadra>(f.elencoSquadreTorneo("Serie A"));
		for(Squadra stemp : squadre)
			System.out.println(" "+stemp.getNome());
		
		
		System.out.println("\n#######################################");
		System.out.println("#            R2. Tesserati            #");
		System.out.println("#######################################\n");

		System.out.println("Nuovo tesseramento.");

		numeroTessera = f.tesseramento("Mario", "Mandzukic","Juventus","Attaccante",17);
		
		System.out.println("\nNumero di tessera assegnato: "+numeroTessera);

		System.out.println("\nCerca tesserato con numero di tessera '1000':");

		tesserato = f.cercaTesseratoPerNumeroTessera(1000);

		System.out.println("\nTesserato trovato: ");
		System.out.println(" Nome: "+tesserato.getNome());
		System.out.println(" Cognome: "+tesserato.getCognome());

		System.out.println("\nAggiunti altri tesserati (un dirigente, due calciatori ed un arbitro).");

		f.tesseramento("Andrea", "Agnelli","Juventus","Presidente");

		f.tesseramento("Mario", "Lemina","Juventus","Centrocampista",18);
		f.tesseramento("Paulo", "Dybala","Juventus","Attaccante",21);

		f.tesseramento("Andrea", "Gervasoni","Bari");

		
		System.out.println("\nElenco tesserati 'Juventus' (ordinati per nome e cognome):");

		tesserati = new LinkedList<Tesserato>(f.elencoTesseratiSquadra("Juventus"));
		for(Tesserato ttemp : tesserati)
			if(ttemp instanceof Dirigente)
				System.out.println(" "+ttemp.getNome()+" "+ttemp.getCognome()+ " (Dirigente)");
			else if(ttemp instanceof Calciatore)
				System.out.println(" "+ttemp.getNome()+" "+ttemp.getCognome()+ " (Calciatore)");
				
		System.out.println("\n#######################################");
		System.out.println("#            R3. Incontri             #");
		System.out.println("#######################################\n");

		System.out.println("Definiti incontri 'Serie A'.");

		f.nuovoIncontro("Serie A", 1, "Juventus", "Inter", "5-1", "Andrea", "Gervasoni");
		f.nuovoIncontro("Serie A", 2, "Inter", "Lazio", "1-1", "Andrea", "Gervasoni");

		System.out.println("\nElenco incontri 'Serie A' (ordinati per giornata):");

		incontri = new LinkedList<Incontro>(f.elencoIncontriPerGiornata("Serie A"));

		for(Incontro itemp : incontri)
			System.out.println(" "+itemp.getGiornata()+" "+itemp.getSquadraCasa().getNome()+" vs "+itemp.getSquadraOspite().getNome()+" "+itemp.getNumeroGolSquadraCasa()+"-"+itemp.getNumeroGolSquadraOspite()+ " "+itemp.getArbitro());

		
		System.out.println("\n#######################################");
		System.out.println("#        R4. Punti e classifica       #");
		System.out.println("#######################################\n");

		System.out.println("Punti 'Juventus':");

		punti = f.puntiSquadra("Juventus");
		System.out.println(" "+punti);
	
		System.out.println("\nClassifica 'Serie A':");
	
		classifica = f.classificaTorneo("Serie A");
		System.out.println(""+classifica);
		
	
	}
	
	

}





