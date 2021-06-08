package ru.yandexEfir.pages;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ru.yandexEfir.constants.Constants.ALL_SERVICES_URL;
import static ru.yandexEfir.constants.Constants.YANDEX_ALL_TITLE;

public class YandexAllServicesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "[class=\"services-big__row\"]")
    public WebElement allServicesTable;

    public YandexAllServicesPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 5);
    }

    public YandexAllServicesPage open() {
        driver.get(ALL_SERVICES_URL);
        driver.manage().window().maximize();
        String title = driver.getTitle();
        Assert.assertEquals(YANDEX_ALL_TITLE, title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));

        wait.until(ExpectedConditions.visibilityOf(allServicesTable));
        return this;
    }

    public void clickLink(String cssSelector) {
        WebElement link = driver.findElement(By.cssSelector(cssSelector));
        link.click();
    }
}
