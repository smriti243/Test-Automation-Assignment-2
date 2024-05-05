package minor.two;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class LoginTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "C://chrome//chromedriver-win64//chromedriver.exe");

        // Initialize the WebDriver
        driver = new ChromeDriver();

        // Maximize the window (optional)
        driver.manage().window().maximize();

        // Navigate to the login page
        driver.get("http://localhost:3000/login");
    }

    @Test
    public void testLoginWithCorrectCredentials() throws InterruptedException {
        // Enter correct credentials
        driver.findElement(By.className("uEmail")).sendKeys("smritirai.jenkins@gmail.com");
        driver.findElement(By.className("uPassword")).sendKeys("123456");
        driver.findElement(By.className("submitbtn1")).click();

        // Wait 10 seconds before checking the URL
        Thread.sleep(10000);

        // Check the URL to verify login was successful
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:3000/home", "URL did not change to home page after correct login.");
    }

    @Test
    public void testLoginWithIncorrectCredentials() throws InterruptedException {
        // Enter incorrect credentials
        driver.findElement(By.className("uEmail")).sendKeys("wrong.email@example.com");
        driver.findElement(By.className("uPassword")).sendKeys("123456");
        driver.findElement(By.className("submitbtn1")).click();

        // Wait 10 seconds before checking the URL
        Thread.sleep(10000);

        // Check the URL to verify login failed
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, "http://localhost:3000/login", "URL changed despite incorrect login.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                // Ensure the directory exists
                File directory = new File("target/reports/");
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                FileUtils.copyFile(scrFile, new File(directory.getPath() + File.separator + result.getName() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Close the browser
        driver.quit();
    }
}
