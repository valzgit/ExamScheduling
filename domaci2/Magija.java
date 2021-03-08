package domaci2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Magija {
	public static ArrayList<Ispit> ispiti = new ArrayList<Ispit>();
	public static ArrayList<Sala> sale = new ArrayList<Sala>();
	public static ArrayList<Dan> dani = new ArrayList<Dan>();
	public static int trajanjeudanima = 0;
	public static StringBuilder opis = new StringBuilder();
	static String rokurl = "rok";
	static String saleurl = "sale";
	static String csvurl = "D:\\ISdom2\\tabela";
	static String csvdeburl = "D:\\ISdom2\\debugtabela";
	static String txturl = "D:\\ISdom2\\koraci";
	static String dbgurl = "D:\\ISdom2\\debug";
	static boolean wildwildwest=false;
	//////////// menjanje ovog parametra menja sufiks fajlova iznad/////////////////
	static String filenum = "5";
	////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) {
		// dohvataju se svi ispiti zajedno sa trajanjem u danima
		ispiti = IspitHandle.dohvatiIspite(rokurl + filenum + ".json");
		trajanjeudanima = IspitHandle.dohvatiTrajanjeUDanima(rokurl + filenum + ".json");

		for (int i = 0; i < trajanjeudanima; i++) {
			dani.add(new Dan());
		}
		// dohvataju se sve sale
		sale = SalaHandler.dohvatiIspite(saleurl + filenum + ".json", trajanjeudanima);

		Pomocnik.sortiraj(ispiti, sale);

		int sakompovima = Pomocnik.brojIspitaSaKompovima(ispiti);
		int kraj = ispiti.size() - sakompovima;
		boolean kompovi = false;
		int zapamtikraj=kraj;
		while ((sakompovima + kraj) != 0) {
			int kojijedan = 0;
			if (sakompovima > 0)
				kompovi = true;
			else
				kompovi = false;
			int pozicijatrenutnogispita=Pomocnik.najveciNerasporedjeniIspit(ispiti, kompovi);
			Ispit is = ispiti.get(pozicijatrenutnogispita);
			opis.append(is.getSifra()+" nonofor: "+is.nonofor+"\n");
			opis.append(is.getSifra()+" nonotermin: "+is.nonotermin+"\n");
			opis.append(is.getSifra()+" nonodan: "+is.nonodan+"\n");
	
			////////////////////////////////////////////////// DAL_SMEM_TAJ_DAN//////////////////////////////////////////////////////////
			boolean moze = true;
			while (kojijedan < trajanjeudanima) {
				moze = true;
				for (int i = 0; i < dani.get(kojijedan).zauzetostpodanima.size(); i++) {
					if (dani.get(kojijedan).zauzetostpodanima.get(i).charAt(5) == is.getSifra().charAt(5)) {
						boolean istiodsek = false;
						Ispit pomocniispit = Pomocnik.dohvatiIspit(dani.get(kojijedan).zauzetostpodanima.get(i));
						for (int brojac1 = 0; brojac1 < is.getOdseci().size(); brojac1++) {
							for (int brojac2 = 0; brojac2 < pomocniispit.getOdseci().size(); brojac2++) {
								if (is.getOdseci().get(brojac1).compareTo(pomocniispit.getOdseci().get(brojac2)) == 0) {
									istiodsek = true;
								}
							}
						}
						if (istiodsek == true) {
							moze = false;
							break;
						}
					}
				}
				if (moze == false) {
					kojijedan++;
				} else
					break;
			}
			boolean vratinazad=false;
			if (kojijedan == trajanjeudanima) {
				try {
					if(pozicijatrenutnogispita!=0 && wildwildwest==false) {
						System.out.println("Ispit sa sifrom: " + is.getSifra() + " nije moguce odrzati ......idemo unazad...... (1)");						
						opis.append("\n Korak unazad zbog neuspesnog zauzimanja "+is.getSifra()+"\n");
						////////////////////////
							//opis.setLength(0);
						//////////////////////
						is.rasporedjen=false;
						is.pickeddan=-1;
						is.pickedfor=-1;
						is.pickedtermin=-1;
						is.nonodan=new ArrayList<Integer>();
						is.nonotermin=new ArrayList<Integer>();
						is.nonofor=new ArrayList<Integer>();
						Ispit prethodniispit=ispiti.get(pozicijatrenutnogispita-1);
						if(prethodniispit.nonodan.size()==0) {
							System.out.println("Broj nonogrupe od prethodnog: "+is.nonodan.size());
						}
						prethodniispit.rasporedjen=false;
						for (int i = 0; i < sale.size(); i++) {
							Sala sala = sale.get(i);
							if (sala.termin[prethodniispit.pickeddan][prethodniispit.pickedtermin].compareTo(prethodniispit.getSifra()) == 0) {
								prethodniispit.gde.remove(sala.getNaziv());
								sala.termin[prethodniispit.pickeddan][prethodniispit.pickedtermin] = "";
							}
						}
						prethodniispit.nonofor.add(prethodniispit.pickedfor);
						prethodniispit.nonotermin.add(prethodniispit.pickedtermin);
						prethodniispit.nonodan.add(prethodniispit.pickeddan);
						dani.get(prethodniispit.pickeddan).zauzetostpodanima.remove(prethodniispit.getSifra());
						prethodniispit.pickedfor=-1;
						prethodniispit.pickeddan=-1;
						prethodniispit.pickedtermin=-1;
						if (sakompovima > 0 || zapamtikraj==kraj)//dodao
							sakompovima++;
						else
							kraj++;
						continue;
					}
					else {
						System.out.println("ISPIT SA SIFROM: " + is.getSifra() + " NIJE MOGUCE ODRZATI!");
						PrintWriter debugg = new PrintWriter(new File(dbgurl + filenum + ".txt"));
						opis.append("\nIspit sa sifrom: " + is.getSifra() + " NIJE MOGUCE odrzati!");
						opis.append("\nPROBLEM: 2 ispita iz iste godine istog smera u istom danu");
						Pomocnik.generisiCSV(csvdeburl + filenum + ".csv");
						debugg.write(opis.toString());
						debugg.close();
						System.exit(1);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			////////////////////////////////////////////////// DAL_SMEM_TAJ_DAN//////////////////////////////////////////////////////////
			while (Pomocnik.najboljiRasporedPoSalama(is, sale, kojijedan) == false) {				
				if (kojijedan == trajanjeudanima-1) {
					try {
						if(pozicijatrenutnogispita!=0 && wildwildwest==false) {
							System.out.println("Ispit sa sifrom: " + is.getSifra() + " nije moguce odrzati ......idemo unazad......(2)");
							opis.append("\n Korak unazad zbog neuspesnog zauzimanja "+is.getSifra()+"\n");
							////////////////////////
							//opis.setLength(0);
							//////////////////////
							is.rasporedjen=false;
							is.pickeddan=-1;
							is.pickedfor=-1;
							is.pickedtermin=-1;
							is.nonodan=new ArrayList<Integer>();
							is.nonotermin=new ArrayList<Integer>();
							is.nonofor=new ArrayList<Integer>();
							Ispit prethodniispit=ispiti.get(pozicijatrenutnogispita-1);
							prethodniispit.rasporedjen=false;
							for (int i = 0; i < sale.size(); i++) {
								Sala sala = sale.get(i);
								if (sala.termin[prethodniispit.pickeddan][prethodniispit.pickedtermin].compareTo(prethodniispit.getSifra()) == 0) {
									prethodniispit.gde.remove(sala.getNaziv());
									sala.termin[prethodniispit.pickeddan][prethodniispit.pickedtermin] = "";
								}
							}
							prethodniispit.nonofor.add(prethodniispit.pickedfor);
							prethodniispit.nonotermin.add(prethodniispit.pickedtermin);
							prethodniispit.nonodan.add(prethodniispit.pickeddan);
							dani.get(prethodniispit.pickeddan).zauzetostpodanima.remove(prethodniispit.getSifra());
							prethodniispit.pickedfor=-1;
							prethodniispit.pickeddan=-1;
							prethodniispit.pickedtermin=-1;
							vratinazad=true;
							if (sakompovima > 0 || zapamtikraj==kraj)//dodao
								sakompovima++;
							else
								kraj++;
							break;
						}
						else {
							System.out.println("ISPIT SA SIFROM: " + is.getSifra() + " NIJE MOGUCE ODRZATI!");
							PrintWriter debugg = new PrintWriter(new File(dbgurl + filenum + ".txt"));
							opis.append("\nIspit sa sifrom: " + is.getSifra() + " NIJE MOGUCE odrzati!");
							opis.append("\nPROBLEM: 2 ispita iz iste godine istog smera u istom danu");
							Pomocnik.generisiCSV(csvdeburl + filenum + ".csv");
							debugg.write(opis.toString());
							debugg.close();
							System.exit(1);
						}
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				kojijedan++;
			}
			if(vratinazad==true)continue;
			if(pozicijatrenutnogispita==0 && is.pickedtermin>0 && is.nonodan.size()>0 && wildwildwest==false) {
				wildwildwest=true;
				System.out.println("-------------------WILD_WILD_WEST---------------------");
				opis.append("\n-------------------WILD_WILD_WEST---------------------\n");
				for (int i = 0; i < sale.size(); i++) {
					Sala sala = sale.get(i);
					if (sala.termin[is.pickeddan][is.pickedtermin].compareTo(is.getSifra()) == 0) {
						is.gde.remove(sala.getNaziv());
						sala.termin[is.pickeddan][is.pickedtermin] = "";
					}
				}
				is.rasporedjen=false;
				is.pickeddan=-1;
				is.pickedfor=-1;
				is.pickedtermin=-1;
				is.nonodan=new ArrayList<Integer>();
				is.nonotermin=new ArrayList<Integer>();
				is.nonofor=new ArrayList<Integer>();
				continue;
			}
			else if(pozicijatrenutnogispita==0 && is.pickedtermin>0 && is.nonodan.size()>0 && wildwildwest==true) {
				System.out.println("ISPIT SA SIFROM: " + is.getSifra() + " NIJE MOGUCE ODRZATI NI NAKON WILDWILDWESTA!");
				PrintWriter debugg;
				try {
					debugg = new PrintWriter(new File(dbgurl + filenum + ".txt"));
					opis.append("\nIspit sa sifrom: " + is.getSifra() + " NIJE MOGUCE odrzati!");
					opis.append("\nPROBLEM: Ne postoji adekvatna kombinacija...");
					Pomocnik.generisiCSV(csvdeburl + filenum + ".csv");
					debugg.write(opis.toString());
					debugg.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
				System.exit(1);
			}
			dani.get(kojijedan).zauzetostpodanima.add(is.getSifra());
			if (sakompovima > 0)
				sakompovima--;
			else
				kraj--;
		}
		
		try {
			PrintWriter printvrajter = new PrintWriter(new File(txturl + filenum + ".txt"));
			Pomocnik.generisiCSV(csvurl + filenum + ".csv");
			printvrajter.write(opis.toString());
			printvrajter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
