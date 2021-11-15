package torneidicalcio;

public class Arbitro extends Tesserato{
	String sezione;
	int numeroTessera;

	public Arbitro(String nome, String cognome, String sezione, int numeroTessera) {
		super(nome, cognome);
		this.sezione=sezione;
		this.numeroTessera=numeroTessera;
	}

	public String getSezione(){
		return sezione;
	}

}
