package domaci2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class IspitHandle {
	public static List<Ispit> lista = new ArrayList<Ispit>();

	public static ArrayList<Ispit> dohvatiIspite(String filepath) {
		JSONObject obj = JSONUtils.getJSONObjectFromFile(filepath);
		JSONArray ja = obj.getJSONArray("ispiti");
		for (int i = 0; i < ja.length(); i++) {
			JSONObject objekat = ja.getJSONObject(i);
			List<String> odseci = new ArrayList<String>();
			JSONArray nizodseka = objekat.getJSONArray("odseci");

			for (int j = 0; j < nizodseka.length(); j++)
				odseci.add(nizodseka.getString(j));
			Ispit ispit = new Ispit(objekat.getString("sifra"), objekat.getInt("prijavljeni"),
					((objekat.getInt("racunari") == 1) ? true : false), odseci);
			lista.add(ispit);
		}

		return (ArrayList<Ispit>) lista;
	}

	public static int dohvatiTrajanjeUDanima(String filepath) {
		JSONObject obj = JSONUtils.getJSONObjectFromFile(filepath);
		return obj.getInt("trajanje_u_danima");
	}
}
