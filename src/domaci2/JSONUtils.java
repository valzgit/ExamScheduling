package domaci2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONUtils {

	// funkcija za dohvatanje JSONStringa iz fajla
	public static String getJSONStringFromFile(String path) {
		Scanner scanner;
		InputStream in = FileHandle.inputStreamFromFile(path);
		scanner = new Scanner(in);
		String json = scanner.useDelimiter("\\Z").next();
		scanner.close();
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;

	}

	// dohvatamo objekat na osnovu naziva fajla
	public static JSONObject getJSONObjectFromFile(String path) {
		return new JSONObject(getJSONStringFromFile(path));
	}

	// dohvatamo niz objekata na osnovu naziva fajla
	public static JSONArray getJSONArrayFromFile(String path) {
		return new JSONArray(getJSONStringFromFile(path));
	}

}
