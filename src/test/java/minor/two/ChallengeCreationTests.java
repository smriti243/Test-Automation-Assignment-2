package minor.two;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import java.util.List;

public class ChallengeCreationTests {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C://chrome//chromedriver-win64//chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 20);  
        driver.get("http://localhost:3000/login");
        performLogin("smritirai.jenkins@gmail.com", "123456"); // Update with actual credentials
    }

    private void performLogin(String username, String password) {
        driver.findElement(By.className("uEmail")).sendKeys(username);
        driver.findElement(By.className("uPassword")).sendKeys(password);
        driver.findElement(By.className("submitbtn1")).click();
    }

    @Test
    public void testCreateChallenge() throws InterruptedException {
        Thread.sleep(5000);

        driver.findElement(By.className("btn1")).click();
        Thread.sleep(5000);
        // Fill the challenge form
        driver.findElement(By.className("chName")).sendKeys("testing");
        new Select(driver.findElement(By.className("chType"))).selectByVisibleText("Physical Challenge");
        new Select(driver.findElement(By.className("chFormat"))).selectByVisibleText("Individual Challenge");
        // Handle date picker
        driver.findElement(By.className("chDeadline")).click();
        WebElement datePicker = driver.findElement(By.className("chDeadline"));
        datePicker.sendKeys("29-04-2024");  // Set date as YYYY-MM-DD

        new Select(driver.findElement(By.className("chStakes"))).selectByVisibleText("Money");
        driver.findElement(By.className("chDescription")).sendKeys("testing the creation of challenges");
        driver.findElement(By.className("ccBTN")).click();

        // Navigate back to the home page and go to challenges
        driver.navigate().to("http://localhost:3000/login");
        performLogin("smritirai.jenkins@gmail.com", "123456");

        Thread.sleep(5000);
        driver.findElement(By.className("challenges")).click(); // Assumes there's a button with id 'challenges'
        Thread.sleep(5000);
        // Search for the challenge name in the popup
        boolean found = searchForChallenge("testing");
        Assert.assertTrue(found, "Challenge not found after creation.");
    }

    private boolean searchForChallenge(String challengeName) {
        // This XPath targets <p> tags directly within the div.popup-inner where challenge names are displayed
        List<WebElement> challenges = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'popup-inner')]/p")));
        for (WebElement challenge : challenges) {
            if (challenge.getText().trim().equals(challengeName)) {
                System.out.println("Found challenge: " + challenge.getText());
                return true;
            }
        }
        return false;
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
            FileUtils.copyFile(scrFile, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
