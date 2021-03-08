package domaci2;

import java.util.ArrayList;
import java.util.List;

public class Ispit {

	private String sifra;
	private int prijavljeni;
	private boolean racunari;
	private List<String> odseci;
	public boolean rasporedjen=false;
	public int pickedfor=-1;
	public int pickedtermin=-1;
	public int pickeddan=-1;
	public List<Integer> nonofor=new ArrayList<Integer>();
	public List<Integer> nonotermin=new ArrayList<Integer>();
	public List<Integer> nonodan=new ArrayList<Integer>();
	//mozda je nepotrebno
	public List<String> gde = new ArrayList<String>();

	public Ispit(String sifra, int prijavljeni, boolean racunari, List<String> odseci) {
		super();
		this.sifra = sifra;
		this.prijavljeni = prijavljeni;
		this.racunari = racunari;
		this.odseci = odseci;
	}

	public String getSifra() {
		return sifra;
	}

	public int getPrijavljeni() {
		return prijavljeni;
	}

	public boolean isRacunari() {
		return racunari;
	}

	public List<String> getOdseci() {
		return odseci;
	}

}
