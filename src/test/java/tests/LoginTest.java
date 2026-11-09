
package tests;

import java.io.ByteArrayInputStream;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.qameta.allure.Allure;

public class LoginTest {

    WebDriver driver;

    @BeforeMethod
    public void setup() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void validLoginTest() {

        Allure.step("Enter valid username");

        driver.findElement(By.id("user-name"))
              .sendKeys("standard_user");

        Allure.step("Enter valid password");

        driver.findElement(By.id("password"))
              .sendKeys("secret_sauce");

        Allure.step("Click login button");

        driver.findElement(By.id("login-button")).click();

        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"), "Login failed");
    }

    @Test
    public void invalidLoginTest() {

        driver.findElement(By.id("user-name"))
              .sendKeys("wrong_user");

        driver.findElement(By.id("password"))
              .sendKeys("wrong_password");

        driver.findElement(By.id("login-button")).click();

        String error = driver.findElement(
            By.cssSelector("[data-test='error']")
        ).getText();

        Assert.assertTrue(
            error.contains("Username and password do not match"),
            "Expected login error was not displayed"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void teardown(org.testng.ITestResult result) {

        if (result.getStatus() == org.testng.ITestResult.FAILURE
                && driver != null) {

            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            Allure.addAttachment(
                "Failure Screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                ".png"
            );
        }

        if (driver != null) {
            driver.quit();
        }
    }
}


