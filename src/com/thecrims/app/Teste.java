package com.thecrims.app;

import static org.junit.Assert.assertEquals;

public class Teste {

	public static void main(String[] args) {
		testStaminaDoRoubo();
	}

	public static void testStaminaDoRoubo() {
		String option = "Shoplift - 5% SP: 100%";
		Integer indice = option.indexOf("-");
		System.out.println(option.substring(indice, indice + 4));
		String stmn = option.substring(indice, indice + 4).trim();
		stmn = stmn.replace("-", "");
		stmn = stmn.replace("%", "");
		stmn = stmn.replace(":", "").trim();
		assertEquals("5", stmn);
	}
}
