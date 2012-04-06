function g_getMoneyHtml(b, h, f, c, o) {
	var m = 0, e = "";
	if (h == 1 || h == "alliance") {
		h = 1
	} else {
		if (h == 2 || h == "horde") {
			h = 2
		} else {
			h = 3
		}
	}

	if (b >= 10000) {
		m = 1;
		var k = Math.floor(b / 10000);
		e += '<span class="moneygold">' + $WH.number_format(k) + "</span>";
		b %= 10000
	}
	if (b >= 100) {
		if (m) {
			e += " "
		} else {
			m = 1
		}
		var k = Math.floor(b / 100);
		e += '<span class="moneysilver">' + k + "</span>";
		b %= 100
	}
	if (b >= 1) {
		if (m) {
			e += " "
		} else {
			m = 1
		}
		e += '<span class="moneycopper">' + b + "</span>"
	}

};