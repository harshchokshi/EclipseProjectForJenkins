package todo;

import static org.testng.AssertJUnit.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

public class AppUnderTest {

	
	public WebDriver driver;
	
	@BeforeSuite
	public void cleanUpOldFiles() 
	{
		String filePath = System.getProperty("user.dir")+"\\target\\surefire-reports\\FailedTestsScreenshots\\";
		File file = new File(filePath);
		String[] currentFiles;
		if (file.isDirectory()) {
			currentFiles = file.list();
			for (int i = 0; i < currentFiles.length; i++) {
				File myFile = new File(file, currentFiles[i]);
				myFile.delete();
			}
		}
	}
	
	@BeforeTest
	public void configure()
	{
		
		WebDriverManager.chromedriver().setup();		
		driver = new ChromeDriver();
		driver.navigate().to("https://www.google.com/");
	}
	
	@Test(priority = 1)
	public void test1() 
	{
		WebElement n = driver.findElement(By.name("q"));
	    n.sendKeys("site: sgligis.com");
	    driver.findElement(By.xpath("//div[@class='FPdoLc lJ9FBc']//input[@class='gNO89b']")).click();
	    driver.findElement(By.xpath("//div[@class='TbwUpd NJjxre']//cite")).click();
	}
	
	@Test(priority = 2)
	public void test2() 
	{
		driver.navigate().to("https://en.wikipedia.org/wiki/James_Webb_Space_Telescope");
		assertTrue(false);
	}
	
	@Test(priority = 3)
	public void test3() 
	{
		driver.navigate().to("https://notepad-plus-plus.org/");
		assertTrue(false);
	}
	
	@AfterMethod
	public String getScreenshot(ITestResult result)
	{
		String destination = "";
		if (result.getStatus()==ITestResult.FAILURE) {
		try {
			String dateName = new SimpleDateFormat("yyyyMMddhh_mm_ss").format(new Date());
			TakesScreenshot ts = (TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);
			destination = System.getProperty("user.dir")+"/target/surefire-reports/FailedTestsScreenshots/"+result.getMethod().getMethodName()+"_"+dateName+".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
		}catch(IOException e)
		{
			e.printStackTrace();
			return e.getMessage();
		}
	}
		return destination;
	}
	
	@AfterTest
	public void closeBrowser () 
	{
		driver.close();
	}

}
