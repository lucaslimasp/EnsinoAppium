/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ensinoappium;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;

public class EnsinoAppium {

    //nome do aplicativo a ser automatizado:
    public static String APK = "android-debug.apk";
    //Caminho até o aplicativo:
    public static String CAMINHO_APP = "C:/Users/l.mauricio.de.sousa/Documents/teste";
    //Contexto de webview do aplicativo, caso seja necessário:
    public static String CONTEXT = "WEBVIEW_com.accenture.portalcliente";
    //url de conexão local com o Appium
    public static String URL = "http://127.0.0.1:4723/wd/hub";
    //Forma de comunicação com o S.O a ser usado
    public static AndroidDriver<AndroidElement> driver;

    //ou //public static IOSDriver<IOSElement> driver_ios;
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        //Define arquivo a ser automatizado: Fazer import java.io.File;
        File arquivoAplicacao = new File(CAMINHO_APP, APK);
        //fazer import org.openqa.selenium.remote.DesiredCapabilities;
        DesiredCapabilities capacidade = new DesiredCapabilities();
        // Comfigura o tipo de plataforma em que sera automatizado: // MobilePlatform.IOS
        capacidade.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
        //define a versão do S.O, não precisa ser definido na maioria dos casos:
        capacidade.setCapability("platformVersion", "6.0");
        //define nome do device:
        capacidade.setCapability(MobileCapabilityType.DEVICE_NAME, "Android");
        //Passa para o Appium a localização do App a ser instalado:
        capacidade.setCapability(MobileCapabilityType.APP, arquivoAplicacao.getAbsolutePath());
        //import io.appium.java_client.android.AndroidDriver;
        //import io.appium.java_client.android.AndroidElement;
        //import java.net.URL;
        //import java.net.MalformedURLException;
        //add throws MalformedURLException para o metodo
        driver = new AndroidDriver<AndroidElement>(new URL(URL), capacidade);
        // ou //driver_ios = new IOSDriver<IOSElement>(new URL(URL), capacidade);
        //define o contexto do driver// Nativo ou Webview por exemplo
        driver.context(CONTEXT);
        //thread sleep adicionado para dar tempo dos elementos do app carregarem:
        Thread.sleep(10000);
        /*encontrar elemento por xpath é uma das formas de se encontrar 
     os elementos a serem interagidos na automação      */
        driver.findElement(By.xpath("//*[@id=\"menuPainelMobile\"]/span")).click();
        //driver.findElement(By.id("menuPainelMobile")).click();
        Thread.sleep(10000);
        driver.findElement(By.id("cpfCnpj")).sendKeys("12345678910");
        //Diretorio onde será salvo as screenshots:
        String DIRETORIO_SCREENSHOT = "C:\\screenshot_appium";
        /* é necessário passar o contexto para o método, pois ele 
     precisará mudar o contexto a fim de tirar a screenshot*/
        takeScreenshot(DIRETORIO_SCREENSHOT, CONTEXT);
    }

    public static void takeScreenshot(String DIRETORIO_SCREENSHOT, String contextDriver) {
        /*Necessário mudar o contexto para Nativo a fim de permitir 
          que o S.O capte a tela*/
        driver.context("NATIVE_APP");
        DateFormat dateFormat;
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        // Transforma a data e hora atual como nome do arquivo:
        dateFormat = new SimpleDateFormat("dd-MM-yyyy__hh_mm_ss");
        new File(DIRETORIO_SCREENSHOT).mkdirs();
        String destFile = dateFormat.format(new Date()) + ".png";
        try {
            // Copia arquivo para o local desejado:
            FileUtils.copyFile(scrFile, new File(DIRETORIO_SCREENSHOT + "/" + destFile));
            //Necessário voltar ao contexto anterior(webview)
            driver.context(contextDriver);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
	 * Metodo log
	 * 
	 * @param msg - mensagem para o log
	 * */
    private void log(String msg) {
        System.out.println(msg);
    }

    public void tirarFoto(int tempo, String descricao) {
        this.log(" --> " + descricao);
        try {
            Thread.sleep(tempo);
            driver.pressKeyCode(AndroidKeyCode.KEYCODE_CAMERA);
            Thread.sleep(tempo);
            //driver.startActivity(AppPackage,cameraAppActivity);
            //driver.pressKeyCode(27);
            // Thread.sleep(tempo);
        } catch (Exception e) {
            System.out.println("Erro classe ExecutarComando, metodo tirarFoto: " + e.getMessage());
        }

    }
    /*
	 * Metodo quit
	 * finaliza app 
	 * */
	public void quit() {
		try {
			driver.quit();
		} catch (Exception e) {
			log("Metodo quit. Erro: " + e.getMessage());
		}
		
	}
	
	/*
	 * Metodo click
	 * 
	 * @param by	 	- id ou xpath, utilizado para pesquisar elemento
	 * @param tempo 	- sleep  
	 * @param descricaoElemento - descricao que será exibir no log
	 * */
	public void click(By by, int tempo, String descricaoElemento) {
		String log = " --> REALIZANDO TESTE ELEMENTO: " + descricaoElemento + " [OK]";
		
		try {
			driver.findElement(by).click();
			Thread.sleep(tempo);
		} catch (Exception e) {
			//this.log("Elemento: " + descricaoElemento + ". Classe: ExecutarComando. Metodo: click. Erro: " + e.getMessage());
			log = log.replace("[OK]", "[Erro: " + e.getMessage() + "]");
		}
		
		this.log(log);
	}
	
	/*
	 * Metodo sendKeys
	 * 
	 * @param texto	- informacao/valor para o input 
	 * @param by	- id ou xpath, utilizado para pesquisar elemento
	 * @param tempo - sleep
	 * @param enter - true ou false
	 * */
	public void sendKeys(String texto, By by, int tempo, boolean enter, String descricaoElemento) {
		String log = " --> REALIZANDO TESTE ELEMENTO: " + descricaoElemento + " [OK]";
		
		try {
			driver.findElement(by).sendKeys(texto, (enter) ? Keys.ENTER : Keys.ESCAPE);
			Thread.sleep(tempo);
		} catch (Exception e) {
			//this.log("Elemento: " + descricaoElemento + ". Classe: ExecutarComando. Metodo: sendKeys. Erro: " + e.getMessage());
			log = log.replace("[OK]", "[ERRO: " + e.getMessage() + "]"); 
		}
		
		this.log(log);
	}
	
	/*
	 * Metodo scroll - executa scroll vertical
	 * 
	 * @param valorY - tamanho em pixel
	 * @param tempo  - sleep
	 * */
	public void scroll(int valorX, int valorY, int tempo) {
		try {
			driver.executeScript("window.scrollBy(" + valorX + ", " + valorY + ")", "");
			Thread.sleep(tempo);
		} catch (Exception e) {
			this.log("Erro classe ExecutarComando, metodo scroll: " + e.getMessage());
		}
	}
	
	/*
	 * Metodo clear - limpar elemento
	 * 
	 * @param by	- id ou xpath, utilizado para pesquisar elemento
	 * @param tempo - sleep
	 * */
	public void clear(By by, int tempo) {
		try {
			driver.findElement(by).clear();
			Thread.sleep(tempo);
		} catch (Exception e) {
			this.log("Erro classe ExecutarComando, metodo clear: " + e.getMessage());
		}
	}
	
	/* Metodo isEnabled - verifica se elemento esta habilitado
	 *  
	 * @param by	- id ou xpath, utilizado para pesquisar elemento
	 * */
	public boolean isEnabled(By by) {
		try {
			return driver.findElement(by).isEnabled();
		} catch (Exception e) {
			this.log("Erro classe ExecutarComando, metodo isEnabled: " + e.getMessage());
			return false;
		}
	}
	
	/*
	 * Metodo idDisplayed - ferifica se elemento existe
	 * 
	 * @param by	- id ou xpath, utilizado para pesquisar elemento
	 * */
	public boolean isDisplayed(By by) {
		try {
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			System.out.println("Erro classe ExecutarComando, metodo isDisplayed: " + e.getMessage());
			return false;
		}
	}
	
	/*
	 * Metodo getText
	 * 
	 * @param by	- id ou xpath, utilizado para pesquisar elemento
	 * */
	public String getText(By by) {
		String text = "";
		
		try {
			text = driver.findElement(by).getText();
		} catch (Exception e) {
			text = "";
			System.out.println("Erro classe ExecutarComando, metodo isEnabled: " + e.getMessage());
		}
		
		return text;
	}
	
	/*
	 * Metodo screenshotAwsDeviceFarm
	 * realiza captura no repositorio aws device farm
	 * 
	 * @param name - nome do arquivo
	 * */
	public boolean screenshotAwsDeviceFarm(final String name) {
		try {
	String screenshotDirectory = System.getProperty("appium.screenshots.dir", System.getProperty("java.io.tmpdir", ""));
	File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
	return screenshot.renameTo(new File(screenshotDirectory, String.format("%s.png", name)));
		} catch (Exception e) {
			System.out.println("Erro classe ExecutarComando, metodo screenshotAwsDeviceFarm: " + e.getMessage());
			return false;
		}
	}
	/*
	 * Metodo back
	 * botao voltar android
	 * 
	 * @param tempo - sleep
	 * @param descricao - informacao utilizada para o logs
	 * */
	public void back(int tempo, String descricao) {
		this.log(" --> " + descricao);
		
		try {
			driver.pressKeyCode(AndroidKeyCode.BACK);
		} catch (Exception e) {
			System.out.println("Erro classe ExecutarComando, metodo back: " + e.getMessage());
		}
	}
}
