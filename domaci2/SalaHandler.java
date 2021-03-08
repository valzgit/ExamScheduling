package domaci2;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SalaHandler {
	public static List<Sala> lista = new ArrayList<Sala>();

	public static ArrayList<Sala> dohvatiIspite(String filepath, int trajanjeudanima) {
		JSONArray ja = JSONUtils.getJSONArrayFromFile(filepath);

		for (int i = 0; i < ja.length(); i++) {
			JSONObject objekat = ja.getJSONObject(i);

			Sala sala = new Sala(objekat.getString("naziv"), objekat.getInt("kapacitet"),
					((objekat.getInt("racunari") == 1) ? true : false), objekat.getInt("dezurni"),
					((objekat.getInt("etf") == 1) ? true : false), trajanjeudanima);
			lista.add(sala);
		}

		return (ArrayList<Sala>) lista;
	}

}
