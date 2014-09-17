package com.ajdeguzman.debtnote;


public class ClassCurrency {
	int pos;
	public String[] currencySymbols = new String[]{
			"AED",
			"AFN",
			"ARS",
			"AUD",
			"BAM",
			"BGN",
			"BHD",
			"BOB",
			"BSD",
			"BWP",
			"BZD",
			"CAD",
			"CHF",
			"CLP",
			"CNY",
			"COP",
			"CRC",
			"CZK",
			"DKK",
			"DOP",
			"DZD",
			"EGP",
			"\u20AC",
			"\u00A3",
			"GTQ",
			"HKD",
			"HNL",
			"HRK",
			"HUF",
			"IDR",
			"\u20AA",
			"\u20B9",
			"IQD",
			"IRR",
			"JMD",
			"JOD",
			"\u00A5",
			"\u20A9",
			"KWD",
			"LBP",
			"LTL",
			"LVL",
			"LYD",
			"MAD",
			"MOP",
			"MXN",
			"NAD",
			"NIO",
			"NOK",
			"NZD",
			"OMR",
			"PAB",
			"PEN",
			"\u20B1",
			"PKR",
			"PLN",
			"PYG",
			"QAR",
			"RON",
			"RSD",
			"RUB",
			"\uFDFC",
			"SDG",
			"SEK",
			"SGD",
			"SYP",
			"\u0E3F",
			"TND",
			"TRY",
			"TTD",
			"TWD",
			"\u20B4",
			"$",
			"UYU",
			"VEF",
			"\u20AB",
			"YER",
			"ZAR"
	};
	public String[]  currencyName = new String[]{
			"UAE Dirham",
			"Afghanistan Afghani",
			"Argentina Peso",
			"Australia Dollar",
			"Bosnian Convertible Marka",
			"Bulgaria Lev",
			"Bahrain Dinar",
			"Bolivia Boliviano",
			"Bahamas Dollar",
			"Botswana Pula",
			"Belize Dollar",
			"Canada Dollar",
			"Switzerland Franc",
			"Chile Peso",
			"China Yuan Renminbi",
			"Colombia Peso",
			"Costa Rica Colon",
			"Czech Republic Koruna",
			"Denmark Krone",
			"Dominican Republic Peso",
			"Algeria Dinar",
			"Egypt Pound",
			"Euro Member Countries",
			"United Kingdom Pound",
			"Guatemala Quetzal",
			"Hong Kong Dollar",
			"Honduras Lempira",
			"Croatia Kuna",
			"Hungary Forint",
			"Indonesia Rupiah",
			"Israel Shekel",
			"India Rupee",
			"Iraq Dinar",
			"Iran Rial",
			"Jamaica Dollar",
			"Jordan Dinar",
			"Japan Yen",
			"Korea Won",
			"Kuwait Dinar",
			"Lebanon Pound",
			"Lithuania Litas",
			"Latvia Lat",
			"Libya Dinar",
			"Morocco Dirham",
			"Macau Pataca",
			"Mexico Peso",
			"Namibia Dollar",
			"Nicaragua Cordoba",
			"Norway Krone",
			"New Zealand Dollar",
			"Oman Rial",
			"Panama Balboa",
			"Peru Nuevo Sol",
			"Philippine Peso",
			"Pakistan Rupee",
			"Poland Zloty",
			"Paraguay Guarani",
			"Qatar Riyal",
			"Romania New Leu",
			"Serbia Dinar",
			"Russia Ruble",
			"Saudi Arabia Riyal",
			"Sudan Pound",
			"Sweden Krona",
			"Singapore Dollar",
			"Syria Pound",
			"Thailand Baht",
			"Tunisia Dinar",
			"Turkey Lira",
			"Trinidad and Tobago Dollar",
			"Taiwan New Dollar",
			"Ukraine Hryvna",
			"United States Dollar",
			"Uruguay Peso",
			"Venezuela Bolivar",
			"Viet Nam Dong",
			"Yemen Rial",
			"South Africa Rand"
	 };
	public String getSymbols(int pos){
		String a = currencySymbols[pos];
		return a;
	}
	public ClassCurrency(String pos) {
		this.pos = Integer.parseInt(pos);
	}
	public ClassCurrency() {
	}

	public String toString() {
		return currencyName[this.pos];
		
	}
}