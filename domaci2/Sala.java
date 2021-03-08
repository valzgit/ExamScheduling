package domaci2;

public class Sala {

	private String naziv;
	private int kapacitet;
	private boolean racunari;
	private int dezurni;
	private boolean etf;
	public String[][] termin;

	public Sala(String naziv, int kapacitet, boolean racunari, int dezurni, boolean etf, int trajanjeudanima) {
		super();
		this.naziv = naziv;
		this.kapacitet = kapacitet;
		this.racunari = racunari;
		this.dezurni = dezurni;
		this.etf = etf;
		termin = new String[trajanjeudanima][4];
		for (int i = 0; i < trajanjeudanima; i++) {
			termin[i][0] = "";
			termin[i][1] = "";
			termin[i][2] = "";
			termin[i][3] = "";
		}

	}

	public String getNaziv() {
		return naziv;
	}

	public int getKapacitet() {
		return kapacitet;
	}

	public boolean isRacunari() {
		return racunari;
	}

	public int getDezurni() {
		return dezurni;
	}

	public boolean isEtf() {
		return etf;
	}

}
