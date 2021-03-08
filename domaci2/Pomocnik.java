package domaci2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Pomocnik {
	public static int brojIspitaSaKompovima(ArrayList<Ispit> lista) {
		int broj = 0;
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).isRacunari() == true) {
				broj++;
			}
		}
		return broj;

	}

	public static int najveciNerasporedjeniIspit(ArrayList<Ispit> lista, boolean imaKomp) {

		int max = 0;
		int poz = -1;
		for (int i = 0; i < lista.size(); i++) {
			Ispit ispit = lista.get(i);
			if (ispit.rasporedjen == false && imaKomp == ispit.isRacunari()) {
				if (max < ispit.getPrijavljeni()) {
					return i;
				}
			}
		}
		return poz;
	}

	public static Ispit dohvatiIspit(String sifra) {
		for (int i = 0; i < Magija.ispiti.size(); i++) {
			if (Magija.ispiti.get(i).getSifra().compareTo(sifra) == 0) {
				return Magija.ispiti.get(i);
			}
		}
		return null;
	}

	public static void generisiCSV(String path) {
		try {
			PrintWriter pw = new PrintWriter(new File(path));
			StringBuilder sb = new StringBuilder();
			for (int dan = 0; dan < Magija.trajanjeudanima; dan++) {
				if (dan != 0) {
					sb.append("");
					sb.append(",");
					for (int i = 0; i < Magija.sale.size(); i++) {
						sb.append("");
						if (i != Magija.sale.size() - 1)
							sb.append(",");
					}
					sb.append("\n");
				}
				sb.append("Dan" + (dan + 1));
				sb.append(",");
				for (int i = 0; i < Magija.sale.size(); i++) {
					sb.append(Magija.sale.get(i).getNaziv());
					if (i != Magija.sale.size() - 1)
						sb.append(",");
				}
				sb.append("\n");
				sb.append("08:00");
				sb.append(",");
				for (int i = 0; i < Magija.sale.size(); i++) {
					if (Magija.sale.get(i).termin[dan][0].compareTo("") == 0)
						sb.append("X");
					else
						sb.append(Magija.sale.get(i).termin[dan][0]);
					if (i != Magija.sale.size() - 1)
						sb.append(",");
				}
				sb.append("\n");
				sb.append("11:30");
				sb.append(",");
				for (int i = 0; i < Magija.sale.size(); i++) {
					if (Magija.sale.get(i).termin[dan][1].compareTo("") == 0)
						sb.append("X");
					else
						sb.append(Magija.sale.get(i).termin[dan][1]);
					if (i != Magija.sale.size() - 1)
						sb.append(",");
				}
				sb.append("\n");
				sb.append("15:00");
				sb.append(",");
				for (int i = 0; i < Magija.sale.size(); i++) {
					if (Magija.sale.get(i).termin[dan][2].compareTo("") == 0)
						sb.append("X");
					else
						sb.append(Magija.sale.get(i).termin[dan][2]);
					if (i != Magija.sale.size() - 1)
						sb.append(",");
				}
				sb.append("\n");
				sb.append("18:30");
				sb.append(",");
				for (int i = 0; i < Magija.sale.size(); i++) {
					if (Magija.sale.get(i).termin[dan][3].compareTo("") == 0)
						sb.append("X");
					else
						sb.append(Magija.sale.get(i).termin[dan][3]);
					if (i != Magija.sale.size() - 1)
						sb.append(",");
				}
				sb.append("\n");
				// System.out.println(sb.toString());

			}
			pw.write(sb.toString());
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static boolean dodatnipoeniuslov(Sala sala, int kojijedan, int mojtermin, Ispit ispit) {
		if ((sala.termin[kojijedan][mojtermin].charAt(5) == '1' && ispit.getSifra().charAt(5) == '2')
				|| (sala.termin[kojijedan][mojtermin].charAt(5) == '2' && ispit.getSifra().charAt(5) == '1')
				|| (sala.termin[kojijedan][mojtermin].charAt(5) == '2' && ispit.getSifra().charAt(5) == '3')
				|| (sala.termin[kojijedan][mojtermin].charAt(5) == '3' && ispit.getSifra().charAt(5) == '2')
				|| (sala.termin[kojijedan][mojtermin].charAt(5) == '3' && ispit.getSifra().charAt(5) == '4')
				|| (sala.termin[kojijedan][mojtermin].charAt(5) == '4' && ispit.getSifra().charAt(5) == '3')) {
			boolean istiodsek = false;
			Ispit pomocniispit = dohvatiIspit(sala.termin[kojijedan][mojtermin]);
			for (int brojac1 = 0; brojac1 < ispit.getOdseci().size(); brojac1++) {
				if (istiodsek == true)
					break;
				for (int brojac2 = 0; brojac2 < pomocniispit.getOdseci().size(); brojac2++) {
					if (ispit.getOdseci().get(brojac1).compareTo(pomocniispit.getOdseci().get(brojac2)) == 0) {
						istiodsek = true;
						break;
					}
				}
			}
			return istiodsek;

		}
		return false;
	}

	public static boolean najboljiRasporedPoSalama(Ispit ispit, ArrayList<Sala> lista, int kojijedan) {

		ispit.rasporedjen = false;
		int preostalo = ispit.getPrijavljeni();
		int mojtermin = 0;
		boolean probajjos = false;
		List<Integer> forzapreskakanje = new ArrayList<Integer>();
		int min = -1;
		int pocnifor = 0;
		int najboljifor = 0;
		int sadamin = 0;
		int zapamtipoz = -1;
		String dodatakporuci = "";

		while (mojtermin < 4) {
			dodatakporuci = "";
			//////////////////////////////////////////// LAST RESORT
			//////////////////////////////////////////// //////////////////////////////////////////
			if (Magija.wildwildwest == true) {
				if (wildWildwest(ispit, lista, kojijedan, mojtermin) == true)
					return true;
			}
			/////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////// PROVERA_PRESKAKANJA_TERMINA/////////////////////////////////////////////
			forzapreskakanje = new ArrayList<Integer>();
			if (ispit.nonotermin.size() != 0) {// moze bilo koji nono posto su identicni

				for (int grr = 0; grr < ispit.nonotermin.size(); grr++) {
					if (ispit.nonodan.get(grr) == kojijedan && ispit.nonotermin.get(grr) == mojtermin) {
						forzapreskakanje.add(ispit.nonofor.get(grr));
					}
				}

				if (forzapreskakanje.size() != 0) {
					for (int grr = 0; grr < forzapreskakanje.size(); grr++) {
						if (pocnifor == forzapreskakanje.get(grr)) {
							pocnifor++;
							grr = -1;
							if (pocnifor == lista.size()) {
								break;
							}
						}
					}
				}
			}
			//////////////////////////////////////// PROVERA_PRESKAKANJA_TERMINA/////////////////////////////////////////////
			for (int i = pocnifor; i < lista.size(); i++) {// i = pocnifor mozda treba i=0

				Sala sala = lista.get(i);
				// System.out.println("Kojijedan: "+kojijedan+" Mojtermin: "+mojtermin);
				if (sala.termin[kojijedan][mojtermin].compareTo("") == 0) {
					if (ispit.isRacunari() == true && sala.isRacunari() == false) {
						if (probajjos == true && i == (lista.size() - 1))
							i = zapamtipoz - 1;
						continue;

					}
					if (preostalo - sala.getKapacitet() > 0) {
						if (probajjos == true) {
							i = zapamtipoz - 1;
							continue;
						} else {
							preostalo -= sala.getKapacitet();
						}
					} else {
						if ((i < lista.size() - 1) && (i != zapamtipoz)) {
							probajjos = true;
							zapamtipoz = i;
							continue;
						} else {
							probajjos = false;
							zapamtipoz = -1;
							sadamin = sala.getKapacitet() - preostalo;
							preostalo = 0;
							//////////////////////////////// OVDE DODAT CHECKAHEAD
							if (min == -1 && sadamin != 0) {
								min = sadamin;
								najboljifor = pocnifor;
							}

							if (sadamin != 0 && min != -2) {// nije savrseno ubacen termin, min=-2 je kod da je vec
															// nadjen i da ne trazim opet
								preostalo = ispit.getPrijavljeni();
								if (sadamin < min) {
									najboljifor = pocnifor;
								}
								int pom = 0;

								int zapamtipomocnifor = pocnifor;
								pocnifor++;
								if (forzapreskakanje.size() != 0) {
									for (int grr = 0; grr < forzapreskakanje.size(); grr++) {
										if (pocnifor == forzapreskakanje.get(grr)) {
											pocnifor++;
											grr = -1;
											if (pocnifor == lista.size()) {
												break;
											}
										}
									}
								}
								for (int vrti = pocnifor; vrti < lista.size(); vrti++) {
									if (lista.get(vrti).termin[kojijedan][mojtermin].compareTo("") == 0
											|| lista.get(vrti).termin[kojijedan][mojtermin]
													.compareTo(ispit.getSifra()) == 0) {
										if (!(ispit.isRacunari() == true && lista.get(vrti).isRacunari() == false))
											pom += lista.get(vrti).getKapacitet();
									}
								}

								if (pom >= ispit.getPrijavljeni()) {
									i = pocnifor - 1;
									for (int i1 = 0; i1 < lista.size(); i1++) {
										Sala sala1 = lista.get(i1);
										if (sala1.termin[kojijedan][mojtermin].compareTo(ispit.getSifra()) == 0) {
											ispit.gde.remove(sala1.getNaziv());
											sala1.termin[kojijedan][mojtermin] = "";
										}
									}
									continue;
								} else {// ako nema smisla proveravati dalje
									// System.out.println(ispit.getSifra() + " nema smisla");
									if (zapamtipomocnifor != najboljifor) {
										pocnifor = najboljifor;
										i = pocnifor - 1;
										min = -2;
										for (int i1 = 0; i1 < lista.size(); i1++) {
											Sala sala1 = lista.get(i1);
											if (sala1.termin[kojijedan][mojtermin].compareTo(ispit.getSifra()) == 0) {
												ispit.gde.remove(sala1.getNaziv());
												sala1.termin[kojijedan][mojtermin] = "";
											}
										}
										continue;
									} else {
										ispit.pickedfor = zapamtipomocnifor;
										preostalo = 0;
									}
								}
							} else {
								ispit.pickedfor = pocnifor;
								pocnifor = 0;
								najboljifor = 0;
								min = -1;
							}

							/////////////////////////////// DO OVDE DODAT
							/////////////////////////////// CHEACKAHEAD//////////////////////////////////////////
						}
					}
					ispit.gde.add(sala.getNaziv());
					sala.termin[kojijedan][mojtermin] = ispit.getSifra();
					if (preostalo == 0) {
						break;
					}
				}
				//////////////////////////////////////// BEZ OVOGA JE BEZ BONUS POENA
				//////////////////////////////////////// /////////////////////////////////////////////////////
				else if (dodatnipoeniuslov(sala, kojijedan, mojtermin, ispit)) {
					dodatakporuci = " zbog susedne godine istog smera";
					probajjos = false;
					zapamtipoz = -1;
					break;
				}
				//////////////////////////////////////// BEZ OVOGA JE BEZ BONUS POENA
				//////////////////////////////////////// /////////////////////////////////////////////////////
				else if (zapamtipoz != -1 && i == lista.size() - 1) {
					i = zapamtipoz - 1;
				}
			}

			if (preostalo == 0) {
				// ispit.pickedfor = pocnifor;
				ispit.pickeddan = kojijedan;
				ispit.pickedtermin = mojtermin;
				// Magija.dani.get(kojijedan).zauzetostpodanima.add(ispit.getSifra());
				Magija.opis.append("DAN " + (kojijedan + 1) + ". Zauzeo uspesno " + ispit.getSifra() + " prijavljeno: ["
						+ ispit.getPrijavljeni() + "] studenata u ");
				if (mojtermin == 0)
					Magija.opis.append("08:00");
				else if (mojtermin == 1)
					Magija.opis.append("11:30");
				else if (mojtermin == 2)
					Magija.opis.append("15:00");
				else
					Magija.opis.append("18:30");
				Magija.opis.append("\n");
				Magija.opis.append("Zauzeo sale: " + ispit.gde);
				Magija.opis.append("\n");
				break;
			} else {
				// ovde treba unazad srediti sve posto zauzimanje termina u ovom delu nije
				// proslo kako treba
				Magija.opis.append("DAN " + (kojijedan + 1) + ". Nije uspeo da zauzme " + ispit.getSifra() + " u ");
				if (mojtermin == 0)
					Magija.opis.append("08:00");
				else if (mojtermin == 1)
					Magija.opis.append("11:30");
				else if (mojtermin == 2)
					Magija.opis.append("15:00");
				else
					Magija.opis.append("18:30");
				if (dodatakporuci.compareTo("") == 0)
					Magija.opis.append(" zbog nedostatka slobodnih mesta");
				else
					Magija.opis.append(dodatakporuci);
				Magija.opis.append("\n");
				for (int i = 0; i < lista.size(); i++) {
					Sala sala = lista.get(i);
					if (sala.termin[kojijedan][mojtermin].compareTo(ispit.getSifra()) == 0) {
						ispit.gde.remove(sala.getNaziv());
						sala.termin[kojijedan][mojtermin] = "";
					}
				}
				preostalo = ispit.getPrijavljeni();
				mojtermin++;
				min = -1;
				pocnifor = 0;
				najboljifor = 0;
				sadamin = 0;
			}
		}
		ispit.rasporedjen = true;
		return (preostalo == 0);

	}

	private static boolean wildWildwest(Ispit ispit, ArrayList<Sala> lista, int kojijedan, int mojtermin) {
		ispit.rasporedjen = false;
		int preostalo = ispit.getPrijavljeni();
		List<Integer> pozlist = new ArrayList<Integer>();
		int blacklist[] = new int[lista.size()];

		int pom = 0;
		for (int i = 0; i < lista.size(); i++) {
			Sala sala = lista.get(i);
			if (sala.termin[kojijedan][mojtermin].compareTo("") == 0 && sala.getKapacitet() <= preostalo) {
				if (ispit.isRacunari() == true && sala.isRacunari() == false) {
					blacklist[i] = -2;
					continue;
				}
				pom += sala.getKapacitet();
				pozlist.add(i);

			} else {
				blacklist[i] = -2;

			}
		}
		// trazim koga da blacklistujem (odmah obelezim koga ne smem da blacklistujem)
		// blacklist -2 znaci da je izbacen, -1 znaci da je obavezan, 0 znaci da nije
		// diran,bilo sta vise je kolko mesta preostaje ako se izbaci
		int gde = -1;
		if (pom != ispit.getPrijavljeni()) {
			for (int i = 0; i < lista.size(); i++) {
				Sala sala = lista.get(i);
				if (blacklist[i] == 0 && (pom - sala.getKapacitet() < ispit.getPrijavljeni())) {
					blacklist[i] = -1;
				} else if (blacklist[i] == 0 && (pom - sala.getKapacitet() > ispit.getPrijavljeni())) {
					blacklist[i] = pom - sala.getKapacitet() - ispit.getPrijavljeni();
				} else if (blacklist[i] == 0 && (pom - sala.getKapacitet() == ispit.getPrijavljeni())) {
					gde = i;
					pom = ispit.getPrijavljeni();//da bi popunio ove ostale
					blacklist[i]=-2;
					break;
				}
			}
		}
		int block1=-1;
		int block2=-2;
		if(gde==-1 && pom!=ispit.getPrijavljeni()) {
			for(int i=0;i<lista.size();i++) {
				boolean brejk=false;
				if(blacklist[i]>0) {
					for(int j=lista.size()-1;j>=0;j--) {
						Sala sala=lista.get(j);
						if(sala.getKapacitet()==blacklist[i] && blacklist[j]>0 && i!=j) {
							block1=i;
							block2=j;//da bi popunio ove ostale stavljam da je pom jednako ispit getprijavljeni
							pom = ispit.getPrijavljeni();
							brejk=true;
							break;
						}
						else if(sala.getKapacitet()>blacklist[i]) {
							break;
						}
					}					
				}
				if(brejk==true)break;
			}
		}
		if (pom == ispit.getPrijavljeni()) {
			preostalo = 0;
			for (int i = 0; i < pozlist.size(); i++) {
				if (pozlist.get(i) == gde || pozlist.get(i)==block1 || pozlist.get(i)==block2)
					continue;
				Sala sala = lista.get(pozlist.get(i));
				ispit.gde.add(sala.getNaziv());
				sala.termin[kojijedan][mojtermin] = ispit.getSifra();
			}

		}

		if (preostalo == 0) {
			// ispit.pickedfor = pocnifor;
			ispit.pickeddan = kojijedan;
			ispit.pickedtermin = mojtermin;
			ispit.pickedfor = -10;
			// Magija.dani.get(kojijedan).zauzetostpodanima.add(ispit.getSifra());
			Magija.opis.append("[DAN " + (kojijedan + 1) + ". --full kombinatorika--] Zauzeo uspesno "
					+ ispit.getSifra() + " prijavljeno: [" + ispit.getPrijavljeni() + "] studenata u ");
			if (mojtermin == 0)
				Magija.opis.append("08:00");
			else if (mojtermin == 1)
				Magija.opis.append("11:30");
			else if (mojtermin == 2)
				Magija.opis.append("15:00");
			else
				Magija.opis.append("18:30");
			Magija.opis.append("\n");
			Magija.opis.append("Zauzeo sale: " + ispit.gde);
			Magija.opis.append("\n");
		} else {
			Magija.opis.append("[DAN " + (kojijedan + 1) + ". --full kombinatorika--] nije uspeo da zauzme "
					+ ispit.getSifra() + " na ovaj nacin...");
			Magija.opis.append("\n");
		}
		ispit.rasporedjen = true;
		return (preostalo == 0);
	}

	public static void sortiraj(ArrayList<Ispit> ispiti, ArrayList<Sala> sale) {
		Collections.sort(sale, new Comparator<Sala>() {
			@Override
			public int compare(Sala s1, Sala s2) {
				if (s1.getKapacitet() < s2.getKapacitet())
					return 1;// zameni ako prvi ima manji kapacitet od drugog
				else if (s1.getKapacitet() == s2.getKapacitet() && s1.getDezurni() > s2.getDezurni())
					return 1;// zameni ih ako imaju iste kapacitete ali prvi zahteva vise ljudi da dezuraju
				else if (s1.getKapacitet() == s2.getKapacitet() && s1.getDezurni() == s2.getDezurni()
						&& s1.isEtf() == false && s2.isEtf() == true)
					return 1;// zameni ih ako imaju isti kap i broj dezurnih
				// ali prvi je van etfa a drugi nije
				else
					return -1;
			}
		});
		Collections.sort(ispiti, new Comparator<Ispit>() {
			@Override
			public int compare(Ispit s1, Ispit s2) {
				if (s1.getPrijavljeni() < s2.getPrijavljeni())// ako prvi ima manje prijavljenih zameni ih
					return 1;
				// ako imaju isti broj prijavljenih poredjaj ih tako da manje godine budu prve
				else if (s1.getPrijavljeni() == s2.getPrijavljeni()
						&& ((s1.getSifra().charAt(5) == '2' && s2.getSifra().charAt(5) == '1')
								|| (s1.getSifra().charAt(5) == '3' && s2.getSifra().charAt(5) == '2')
								|| (s1.getSifra().charAt(5) == '4' && s2.getSifra().charAt(5) == '3')))
					return 1;
				else
					return -1;
			}
		});
	}
}
