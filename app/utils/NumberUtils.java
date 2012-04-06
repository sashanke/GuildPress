package utils;

import java.text.DecimalFormat;

public class NumberUtils {

	public static String formatGold(float avgbuyout) {
		String html = "";
		DecimalFormat df = new DecimalFormat(",##0");
		double copper = avgbuyout;
		if (copper >= 10000) {
			double gold = Math.floor(copper / 10000);
			html += "<span class=\"moneygold\">" + df.format(gold) + "</span>";
			copper %= 10000;
		}
		if (copper >= 100) {
			double silver = Math.floor(copper / 100);
			html += "<span class=\"moneysilver\">" + df.format(silver) + "</span>";
			copper %= 100;
		}
		if (copper >= 1) {
			html += "<span class=\"moneycopper\">" + df.format(copper) + "</span>";
		}
		return html;
	}

}
