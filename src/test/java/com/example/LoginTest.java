package com.example;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @Test
    void test_login_with_incorrect_credentials() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        
        // Selenium 4 (used in pom.xml) handles the web driver downloading automatically,
        // so we skipped the System.setProperty line to ensure it runs well on Jenkins/EC2.
        WebDriver driver = new ChromeDriver(options);
        
        // Set wait BEFORE trying to find elements so it waits for the page to finish loading!
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        // Note: URL from the lab screenshots, wait until loading completes
        driver.navigate().to("http://103.139.122.250:4000/");
        
        // Enter dummy credentials
        driver.findElement(By.name("email")).sendKeys("qasim@malik.com");
        driver.findElement(By.name("password")).sendKeys("abcdefg");
        
        // Click the submit button
        driver.findElement(By.id("m_login_signin_submit")).click();
        
        // Assuming the XPath points to the error message div
        String errorText = driver.findElement(By.xpath("/html/body/div/div/div[1]/div/div/div/div[2]/form/div[1]")).getText();
        
        assertTrue(errorText.contains("Incorrect email or password"));
        
        driver.quit();
    }
}
