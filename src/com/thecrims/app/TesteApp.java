package com.thecrims.app;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TesteApp {

	@Test
	public void testStaminaDoRoubo() {
		String option = "Shoplift - 5% SP: 100%";
		Integer indice = option.indexOf("- ");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("5", stmn);
	}
	
	@Test
	public void testStaminaDoRoubo2() {
		String option = "Grocery store - 10% SP: 14%";
		Integer indice = option.indexOf("- ");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("10", stmn);
	}
	
	@Test
	public void testStaminaDoRoubo3() {
		String option = "Car break-in - 10% SP: 100%";
		Integer indice = option.indexOf("- ");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("10", stmn);
	}
	
	@Test
	public void testStaminaDoRoubo4() {
		String option = "Jewellery - 20% SP: 84%";
		Integer indice = option.indexOf("- ");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("20", stmn);
	}
	
	@Test
	public void testSpDoRoubo() {
		String option = "Shoplift - 5% SP: 100%";
		Integer indice = option.indexOf(":");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("100", stmn);
	}
	
	@Test
	public void testSpDoRoubo2() {
		String option = "Grocery store - 10% SP: 14%";
		Integer indice = option.indexOf(":");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("14", stmn);
	}
	
	@Test
	public void testSpDoRoubo3() {
		String option = "Jewellery - 20% SP: 84%";
		Integer indice = option.indexOf(":");
		String stmn = limpaString(option.substring(indice, indice + 5).trim());
		assertEquals("84", stmn);
	}
	
	public static String limpaString(String texto) {
		texto = texto.replace("-", "");
		texto = texto.replace("%", "");
		return texto.replace(":", "").trim();
	}
}
