package ru.yandexEfir.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ru.yandexEfir.constants.Constants.yandexURL;

public class YandexMainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public YandexMainPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 5);
    }

    public YandexMainPage open() {
        driver.get(yandexURL);
        driver.manage().window().maximize();
        String title = driver.getTitle();
        Assert.assertEquals("Яндекс", title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));

        //WebElement yandexLogo = driver.findElement(By.cssSelector("[class=\"home-logo__default\"]"));
        WebElement searchLine = driver.findElement(By.id("text"));
        WebElement searchButton = driver.findElement(By.cssSelector("[class=\"button mini-suggest__button button_theme_search button_size_search i-bem button_js_inited\"]"));
        //wait.until(ExpectedConditions.visibilityOf(yandexLogo));
        wait.until(ExpectedConditions.visibilityOf(searchLine));
        wait.until(ExpectedConditions.visibilityOf(searchButton));
        return this;
    }

    public YandexMainPage pressMoreButton() {
        WebElement moreButton = driver.findElement(By.cssSelector("[data-id=\"more\"]"));
        moreButton.click();
        return this;
    }

    public void selectAllServices() {
        WebElement allServicesLink = driver.findElement(By.cssSelector("[href=\"//yandex.ru/all\"]"));
        wait.until(ExpectedConditions.visibilityOf(allServicesLink));
        allServicesLink.click();

        String winHandleBefore = driver.getWindowHandle();
        for(String winHandle : driver.getWindowHandles()){
            if(!winHandle.equals(winHandleBefore)) {
                driver.switchTo().window(winHandle);
            }
        }

        WebElement allServicesTable = driver.findElement(By.cssSelector("[class=\"services-big__row\"]"));
        wait.until(ExpectedConditions.visibilityOf(allServicesTable));

        String title = driver.getTitle();
        Assert.assertEquals("Все сервисы Яндекса", title);
    }

}
