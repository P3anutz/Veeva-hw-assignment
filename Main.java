import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        String driverPath = "G:\\.WORK\\chromedriver\\chromedriver.exe";
        File driverFile = new File(driverPath);
        if (!driverFile.exists()) {
            System.out.println("ChromeDriver not found at: " + driverPath);
            return;
        }

        // connects to chrome with the drivers so selenium can click/type in the browser
        System.setProperty("webdriver.chrome.driver", driverPath);
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // helper functin to passes age into function to fill out all inputs
        java.util.function.Consumer<String> fillCalculator = (age) -> {
            driver.get("https://www.calculator.net/carbohydrate-calculator.html");

            // age
            WebElement ageField = wait.until(ExpectedConditions.elementToBeClickable(By.name("cage")));
            ageField.clear();
            ageField.sendKeys(age);

            // height
            WebElement heightField = driver.findElement(By.name("cheightmeter"));
            heightField.clear();
            heightField.sendKeys("111"); // height in cm

            // weight
            WebElement weightField = driver.findElement(By.name("ckg"));
            weightField.clear();
            weightField.sendKeys("140"); // weight in kg

            // activity
            WebElement activityDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("cactivity")));
            Select select = new Select(activityDropdown);
            select.selectByVisibleText("Light: exercise 1-3 times/week"); // directly select

            // click calculate
            driver.findElement(By.xpath("//input[@value='Calculate']")).click();
        };

        // test case 1 - valid age input: 23
        fillCalculator.accept("23");
        try {
            WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//b[contains(text(),'grams')]")));
            System.out.println("Test 1 passed - valid age: 23");
        } catch (Exception e) {
            System.out.println("Test 1 Failed");
        }

        // test case 1 - invalid age input: blank
        fillCalculator.accept(""); // empty age
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(., 'Please provide')]")));
            System.out.println("Test 2 passed - invalid age: blank");
        } catch (Exception e) {
            System.out.println("Test 2 Failed");
        }

        // test case 1 - valid age input: -5
        fillCalculator.accept("-5");
        try {
            WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(., 'Please provide')]")));
            System.out.println("Test 3 passed - invalid age: -5");
        } catch (Exception e) {
            System.out.println("Test 3 Failed");
        }

        driver.quit();
    }
}
