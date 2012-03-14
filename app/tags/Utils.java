package tags;

import java.text.DecimalFormat;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import play.templates.JavaExtensions;
import utils.FileUtils;

public class Utils extends JavaExtensions {
	
	public static String getRandomFile(String dir) {
		return FileUtils.getRandomFile(dir);
		
	}
	
	public static String wrap(String in, int len, String link) {
		
		in = Jsoup.clean(in, Whitelist.none());
		
		StringTokenizer st = new StringTokenizer(in, " ");
		
		String newString = "";
		int count = 0;
		while (st.hasMoreTokens()) {
			newString += " " + st.nextToken();
			if (count >= len) {
				break;
			}
		    count++;
		}
		
		
		return newString;
	}
	
	public static String prozentFormat(Number number, String currencySymbol) {
	     String format = "###";
	     return new DecimalFormat(format).format(number);
	  }
	
	public static List subList(List list, int max) {
        int size = Math.min(max, list.size());
        return list.subList(0, size);
    }
}
