package edu.udacity.java.nano.chat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketChatApplicationTest {

    public WebDriver getDriver() throws InterruptedException {
        // Load Chrome driver from test/resources
        Path currentDir = Paths.get(".").normalize();
        String pathToDriver = currentDir.toAbsolutePath() + "/src/test/resources/chromedriver_linux";
        System.setProperty("webdriver.chrome.driver", pathToDriver);

        WebDriver driver = new ChromeDriver();
        driver.get("localhost:8080");
        Thread.sleep(3000);  // Let the user actually see something!
        return driver;
    }

    public void testLogin(WebDriver userDriver, String userName) throws InterruptedException {

        WebElement loginBox = userDriver.findElement(By.name("username"));
        loginBox.sendKeys(userName);
        Thread.sleep(3000);  // Let the user actually see something!
        loginBox.submit();
        Thread.sleep(3000);  // Let the user actually see something!
    }

    public void testChat(WebDriver userDriver, String message) throws InterruptedException {
        Thread.sleep(3000);  // Let the user actually see something!
        WebElement msgBox = userDriver.findElement(By.id("msg"));
        msgBox.sendKeys(message);
        Thread.sleep(3000);  // Let the user actually see something!
        userDriver.findElement(By.cssSelector("button[onclick^='sendMsgToServer']")).click();
    }

    public void testLeaving(WebDriver userDriver) throws InterruptedException {
        Thread.sleep(3000);  // Let the user actually see something!
        userDriver.findElement(By.className("mdui-btn")).click();
        Thread.sleep(3000);  // Let the user actually see something!
        userDriver.quit();
    }

    @Test
    public void doLoginScreen() throws InterruptedException {
        WebDriver userDriver1 = this.getDriver();
        assertThat("Login", CoreMatchers.containsString(userDriver1.findElement(By.className("act-but")).getText()));
        userDriver1.quit();
    }

    @Test
    public void doLoginComplete() throws InterruptedException {
        String userName1 = "username1";
        WebDriver userDriver1 = this.getDriver();
        assertThat("Login", CoreMatchers.containsString(userDriver1.findElement(By.className("act-but")).getText()));
        testLogin(userDriver1, userName1);
        assertThat(userName1, CoreMatchers.containsString(userDriver1.findElement(By.id("username")).getText()));
        testLeaving(userDriver1);
    }


    @Test
    public void doChat() throws InterruptedException {
        String userName1 = "username1";
        String userName2 = "username2";
        WebDriver userDriver1 = this.getDriver();
        //assertThat("Login", CoreMatchers.containsString(userDriver1.findElement(By.className("act-but")).getText()));
        WebDriver userDriver2 = this.getDriver();
        testLogin(userDriver1, userName1);
        testLogin(userDriver2, userName2);
        testChat(userDriver1, "Hi, I'm " + userName1);
        assertThat(userDriver1.findElement(By.className("message-container")).getText(), containsString("Hi, I'm " + userName1));
        testChat(userDriver2, "Hi, I'm " + userName2);
        assertThat(userDriver1.findElement(By.className("message-container")).getText(), containsString("Hi, I'm " + userName2));
        testChat(userDriver2, "Nice to meet you too " + userName1);
        testChat(userDriver1, "Have a nice day " + userName2 + ". Bye now.");
        testChat(userDriver2, "You too " + userName1 + ". Goodbye!");
        testLeaving(userDriver1);
        testLeaving(userDriver2);

    }

}

