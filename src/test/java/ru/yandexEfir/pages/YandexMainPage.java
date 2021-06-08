package ru.yandexEfir.pages;

import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static ru.yandexEfir.constants.Constants.*;

public class YandexMainPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "[class=\"home-logo__default\"]")
    public WebElement yandexLogo;

    @FindBy(css = "[class=\"button mini-suggest__button button_theme_search button_size_search i-bem button_js_inited\"]")
    public WebElement searchButton;

    @FindBy(id = "text")
    public WebElement searchLine;

    @FindBy(css = "[data-id=\"more\"]")
    public WebElement moreButton;

    @FindBy(css = "[href=\"//yandex.ru/all\"]")
    WebElement allServicesLink;

    @FindBy(css = "[class=\"services-big__row\"]")
    WebElement allServicesTable;

    public YandexMainPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 5);
    }

    public YandexMainPage open() {
        driver.get(YANDEX_URL);
        driver.manage().window().maximize();
        String title = driver.getTitle();
        Assert.assertEquals(YANDEX_TITLE, title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));

        wait.until(ExpectedConditions.visibilityOf(yandexLogo));
        wait.until(ExpectedConditions.visibilityOf(searchLine));
        wait.until(ExpectedConditions.visibilityOf(searchButton));
        return this;
    }

    public YandexMainPage pressMoreButton() {
        moreButton.click();
        return this;
    }

    public void selectAllServices() {
        wait.until(ExpectedConditions.visibilityOf(allServicesLink));
        allServicesLink.click();

        String winHandleBefore = driver.getWindowHandle();
        for(String winHandle : driver.getWindowHandles()){
            if(!winHandle.equals(winHandleBefore)) {
                driver.switchTo().window(winHandle);
            }
        }

        wait.until(ExpectedConditions.visibilityOf(allServicesTable));

        String title = driver.getTitle();
        Assert.assertEquals(YANDEX_ALL_TITLE, title);
    }

}
