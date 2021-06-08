package ru.yandexEfir.helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.PageFactory;
import ru.yandexEfir.pages.YandexAllServicesPage;
import ru.yandexEfir.pages.YandexEfirPage;
import ru.yandexEfir.pages.YandexMainPage;

public class TestCase {

    public ChromeDriver driver; //setup the driver
    public YandexMainPage yandexMainPage;
    public YandexAllServicesPage yandexAllServicesPage;
    public YandexEfirPage yandexEfirPage;
    DocumentParser documentParser;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    } //setup the driver

    @Before
    public void setup() {
        driver = new ChromeDriver(); //setup the driver
        yandexMainPage = PageFactory.initElements(driver, YandexMainPage.class);
        yandexAllServicesPage = PageFactory.initElements(driver, YandexAllServicesPage.class);
        yandexEfirPage = PageFactory.initElements(driver, YandexEfirPage.class);
        documentParser = new DocumentParser(driver);
        JSWaiter.setDriver(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
