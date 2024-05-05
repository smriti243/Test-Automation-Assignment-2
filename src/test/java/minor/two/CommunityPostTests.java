package minor.two;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
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

public class CommunityPostTests {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C://chrome/chromedriver-win64//chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:3000/login");
        performLogin("smritirai.jenkins@gmail.com", "123456"); 
    }

    private void performLogin(String username, String password) {
        driver.findElement(By.className("uEmail")).sendKeys(username);
        driver.findElement(By.className("uPassword")).sendKeys(password);
        driver.findElement(By.className("submitbtn1")).click();
    }

    @Test
    public void testBlogPostSubmission() throws InterruptedException {
        Thread.sleep(5000);
        driver.findElement(By.className("community")).click();
        WebElement blogInput = driver.findElement(By.className("blog"));
        String blogText = "This is a test post to verify functionality.";
        blogInput.sendKeys(blogText);
        driver.findElement(By.className("blogbtn")).click();
        Thread.sleep(5000);
        WebElement topPost = driver.findElement(By.xpath("//div[@class='post'][1]//p"));
        Assert.assertEquals(topPost.getText(), blogText, "The submitted blog post does not appear at the top of the page.");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (!result.isSuccess()) {
            takeScreenshot(result.getName());
        }
        driver.quit();
    }

    private void takeScreenshot(String testName) {
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File targetFile = new File("target/reports/" + testName + ".png");
        try {
            // Make sure the directory exists
            File directory = new File("target/reports/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            FileUtils.copyFile(scrFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
