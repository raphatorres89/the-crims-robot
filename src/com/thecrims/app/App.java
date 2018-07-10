package com.thecrims.app;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class App {

	static WebDriver driver;
	static int stamina;
	static final String SITE = "https://beta.thecrims.com/";
	static final String FACEAUTH = "beta.thecrims.com/login/facebook";
	static final String ROBBERY = "https://beta.thecrims.com/newspaper#/robberies";
	static final String NIGHTLIFE = "https://beta.thecrims.com/newspaper#/nightlife/whorehouses";

	public static void main(String[] args) {

		System.setProperty("webdriver.gecko.driver", "C:\\Users\\dev02\\Downloads\\Libs\\geckodriver.exe");

		driver = new FirefoxDriver();

		// ABRE O SITE
		driver.get(SITE);

		// FAZ LOGIN
		if (isTelaLogin()) {
			logar();
		}
		
		// SE ESTIVER NA TELA PRINCIPAL
		if (isTelaPrincipal()) {
			// ROUBA ATÉ ACABAR A STAMINA
			while(isStaminaOk()) {
				roubar();
				
				recuperarStamina();
			}
			
			System.out.println("Sai do loop de roubo");
			esperar();
			
			// RECUPERA STAMINA
			/*while(isLowStamina()) {
				recuperarStamina();
			}*/
			
			
		}

		// Check the title of the page
		System.out.println("Page title is: " + driver.getTitle());

		// driver.quit();
	}
	
	private static void recuperarStamina() {
		if (!isTelaNightLife()) {
			driver.get(NIGHTLIFE);
		}
		
		esperar("The Crims");

		if(isTelaNightLife()) {
			boolean form = driver.findElement(By.tagName("table")).getText().startsWith("Caribbean Club");
			System.out.println(form);
		}
		
		
		System.out.println("Entrando no whorehouse");
		WebElement listaDeWhoreHouses = driver.findElement(By.className("nightclubs"));
		WebElement corpoDaTabela = listaDeWhoreHouses.findElement(By.tagName("tbody"));
		WebElement btnWhorehouses = corpoDaTabela.findElement(By.tagName("button"));
		btnWhorehouses.click();
		
		esperar();
		
		System.out.println("Selecionando");
		WebElement tabelaDaWhorehouse = driver.findElement(By.className("table-top-spacing"));
		WebElement corpoDaWhorehouse = tabelaDaWhorehouse.findElement(By.tagName("tbody"));
		WebElement btnWhorehouse = corpoDaWhorehouse.findElement(By.tagName("button"));
		while (isLowStamina()) {
			System.out.println("Recuperando Stamina");
			btnWhorehouse.click();
			esperar();
		}
	}

	private static void roubar() {
		if(!isTelaRoubo()) {
			System.out.println("Acessar tela de roubo");
			
			driver.get(ROBBERY);

			esperar("The Crims");
			esperar();
		}
		
		// STAMINA NECESSÁRIA
		String staminaNecessaria = null;
		
		if(isTelaRoubo()) {
			
			// PEGA O SELECT
			Select soloCrimes = new Select(driver.findElement(By.tagName("select")));
			
			for (WebElement solo: soloCrimes.getOptions()) {
				
				String indice = solo.getAttribute("value");
				
				// PULA O PRIMEIRO OPTION
				if (!indice.equals("")) {
					
					// LIMPA O TEXTO E PEGA A PORCENTAGEM DE SUCESSO
					String option = solo.getAttribute("innerHTML").trim(); // Shoplift - 5% SP: 100%
					String sp = filtrarOption(option, ":"); // 100%
					
					// STAMINA NECESSÁRIA
					String stmn = filtrarOption(option, "- "); // 5%
										
					//SE PORCENTAGEM DE SUCESSO FOR MAIOR QUE 75% E STAMINA ATUAL É MAIOR QUE A NECESSÁRIA
					
					System.out.println("SP: " + Integer.parseInt(sp) + " >= " + 80 + " " + (Integer.parseInt(sp) >= 80));
					System.out.println("Stmn: " +  getStamina() + " >= " + Integer.parseInt(filtrarOption(option, "- ")) + " " + (getStamina() >= Integer.parseInt(filtrarOption(option, "- "))));
					if ((Integer.parseInt(sp) >= 80) && (getStamina() >= Integer.parseInt(filtrarOption(option, "- ")))) {
						
						staminaNecessaria = stmn;
						
						// SETA A OPTION
						System.out.println("Selecionando botão " + Integer.parseInt(indice) + " " + solo.getAttribute("innerHTML").trim());
						soloCrimes.selectByValue(indice);
					}
				}
			}
			// EXECUTA O ROUBO
			while(getStamina() >= Integer.parseInt(staminaNecessaria)) {
				System.out.println("Clicado no botão ");
				driver.findElement(By.xpath("//button[.='Rob the bastard!']")).click();
			}
		}
	}
	
	public static void logar() {
		while (isTelaLogin()) {
			
			try {
				
				// Clica no login do Facebook
				WebElement facebook = driver.findElement(By.className("zocial"));
				
				facebook.click();
				
				esperar("entrar no facebook");
				
				// se o face não estiver logado
				if (driver.getTitle().toLowerCase().startsWith("entrar no facebook")) {
					
					WebElement email = driver.findElement(By.id("email"));
					WebElement pwd = driver.findElement(By.id("pass"));
					
					email.sendKeys("metal_torres@hotmail.com");
					pwd.sendKeys("r4ph43l#");
					
					pwd.submit();
					
					esperar(FACEAUTH);
					
					System.out.println(driver.getTitle());
					
					driver.get(SITE);
					
				}
				
				if (isTelaPrincipal()) {
					break;
				}
				
			} catch (NoSuchElementException ex) {
				break;
			}
		}
	}
	
	public static String filtrarOption(String texto, String separador) {
		Integer indice = texto.indexOf(separador);
		return limpaString(texto.substring(indice, indice + 5).trim());
	}
	
	public static String limpaString(String texto) {
		texto = texto.replace("-", "");
		texto = texto.replace("%", "");
		return texto.replace(":", "").trim();
	}
	
	public static Integer getStamina() {
		WebElement cssStamina = driver.findElement(By.id("stamina-progressbar"));
		String valor = cssStamina.getAttribute("style");// width: 100%;
		valor = valor.replace("width: ", "");
		valor = valor.replace("%;", "").trim();
		stamina = Integer.parseInt(valor);
		return stamina;
	}
	
	private static boolean isStaminaOk() {
		return getStamina() >= 5;
	}

	private static boolean isLowStamina() {
		return getStamina() <= 90;
	}
	
	private static boolean isTelaPrincipal() {
		return driver.findElement(By.id("newspaper")).isDisplayed();
	}

	private static boolean isTelaLogin() {
		return driver.findElement(By.id("loginform")).isDisplayed();
	}
	
	private static boolean isTelaRoubo() {
		return driver.findElement(By.tagName("p")).getText().startsWith("Rumor says");
	}
	
	private static boolean isTelaNightLife() {
		return driver.findElement(By.tagName("p")).getText().startsWith("You can feel the pulsating bass");
	}
	
	public static void esperar(String title) {
		try {
			System.out.println(title);
			(new WebDriverWait(driver, 5)).until(new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver d) {
					return d.getTitle().toLowerCase().startsWith(title.toLowerCase());
				}
			});
		} catch(TimeoutException e) {
			e.printStackTrace();
		}
	}

	public static void esperar() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
