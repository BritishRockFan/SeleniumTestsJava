package ru.yandexEfir.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ru.yandexEfir.constants.Constants.allServicesURL;

public class YandexAllServicesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public YandexAllServicesPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 5);
    }

    public YandexAllServicesPage open() {
        driver.get(allServicesURL);
        driver.manage().window().maximize();
        String title = driver.getTitle();
        Assert.assertEquals("Все сервисы Яндекса", title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));

        WebElement allServicesTable = driver.findElement(By.cssSelector("[class=\"services-big__row\"]"));
        wait.until(ExpectedConditions.visibilityOf(allServicesTable));
        return this;
    }

    public void clickLink(String cssSelector) {
        WebElement link = driver.findElement(By.cssSelector(cssSelector));
        link.click();
    }
}
