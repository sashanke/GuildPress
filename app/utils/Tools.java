package utils;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Tools {
	public static String implodeArray(String[] inputArray, String glueString) {

		/** Output variable */
		String output = "";

		if (inputArray.length > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputArray[0]);

			for (int i = 1; i < inputArray.length; i++) {
				sb.append(glueString);
				sb.append(inputArray[i]);
			}

			output = sb.toString();
		}

		return output;
	}

	public static String implodeArray(JsonArray inputArray, String glueString) {
		/** Output variable */
		String output = "";
		StringBuilder sb = new StringBuilder();
		for (JsonElement jsonElement : inputArray) {
			sb.append(glueString);
			sb.append(jsonElement.getAsString());

		}
		output = sb.toString().substring(1);
		return output;
	}
	
	public static String implodeList(List<String> inputList, String glueString) {
		/** Output variable */
		String output = "";
		StringBuilder sb = new StringBuilder();
		
		for (String string : inputList) {
			sb.append(glueString);
			sb.append(string);
		}
		if (sb.length() > 0) {
			output = sb.toString().substring(1);
		} else {
			output = sb.toString();
		}
		
		return output;
	}
	
	
}
