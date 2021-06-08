package ru.yandexEfir.pages;

import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandexEfir.helpers.MovieCardMetadata;
import ru.yandexEfir.helpers.SearchResults;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.yandexEfir.helpers.DocumentParser.parseAssetMeta;
import static ru.yandexEfir.helpers.DocumentParser.searchCheck;
import static ru.yandexEfir.constants.Constants.efirURL;

public class YandexEfirPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static int searchResultsCount;
    public static String firstAssetName;
    public static String meta;
    public static List<SearchResults> someResultApiArray;

    @FindBy(css = "[href=\"/efir?stream_active=purchases&from_block=efir_newtab\"]")
    public WebElement myPurchases;

    @FindBy(css = "[href=\"/efir?stream_active=category&stream_category=film&from_block=efir_newtab\"]")
    public WebElement myFilms;

    public YandexEfirPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 5);
    }

    public YandexEfirPage open() {
        driver.manage().window().maximize();
        driver.get(efirURL);
        String title = driver.getTitle();
        Assert.assertEquals("Яндекс.Эфир", title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));
        return this;
    }

    public YandexEfirPage checkThatOpened() {
        WebElement player = driver.findElement(By.xpath("//*[@id=\"stream-storefront__gallery\"]/div/div[1]"));
        wait.until(ExpectedConditions.visibilityOf(player));
        WebElement sideMenu = driver.findElement(By.cssSelector("[role=\"navigation\"]"));
        wait.until(ExpectedConditions.visibilityOf(sideMenu));
        WebElement whatToWatch = driver.findElement(By.cssSelector("[class=\"Cut Cut_oneLine Cut_1-20\"]"));
        whatToWatch.isSelected();
        return this;
    }

    public YandexEfirPage navigateSideMenu(WebElement element) {
        element.click();
        element.isSelected();
        return this;
    }

    public YandexEfirPage selectVideoAsset(int row, int movie) throws IOException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebElement asset = driver.findElement(By.xpath("//*[@id=\"stream-category__feed\"]/div/div[" + row + "]/div/div[2]/div/div[2]/div[" + movie + "]"));
        wait.until(ExpectedConditions.visibilityOf(asset));
        wait.until(ExpectedConditions.elementToBeClickable(asset));
        WebElement title = driver.findElement(By.xpath("//*[@id=\"stream-category__feed\"]/div/div[" + row + "]/div/div[2]/div/div[2]/div[" + movie + "]" + "/div/div/div[2]/div[1]/div[1]/a"));
        String assetName = title.getAttribute("innerText");

        WebElement linkElement = driver.findElement(By.xpath("//*[@id=\"stream-category__feed\"]/div/div[" + row + "]/div/div[2]/div/div[2]/div[" + movie + "]/div/div/div[1]/div[1]/a"));
        String link = linkElement.getAttribute("href");



        asset.click();
        WebElement assetTitleInPlayer = driver.findElement(By.cssSelector("[class=\"stream-program-title__title stream-watching__player-header-title i-multiline-overflow\"]"));
        String assetNameInPlayer = assetTitleInPlayer.getAttribute("innerText");
        Assert.assertEquals(assetName, assetNameInPlayer);

        //Year and rating
        WebElement yearElement = driver.findElement(By.cssSelector("[class=\"stream-program-title__subtitle stream-watching__player-header-subtitle\"]"));
        WebElement ratingElement = driver.findElement(By.cssSelector("[class=\"stream-watching__restriction stream-program-title__restriction\"]"));

        String year = yearElement.getAttribute("textContent").substring(0,4);
        String rating = ratingElement.getAttribute("outerText");
        System.out.println(assetName);
        System.out.println(assetNameInPlayer);
        System.out.println(year);
        System.out.println(rating);
        System.out.println(link);

        MovieCardMetadata movieCardMetadata = parseAssetMeta(link);
        Assert.assertEquals(year, movieCardMetadata.getYear());
        Assert.assertEquals(rating, movieCardMetadata.getRating());
        return this;
    }

    public YandexEfirPage checkPurchasesIsEmpty() {
        WebElement emptyScreenTitle = driver.findElement(By.xpath("//*[@id=\"stream-purchases__layout\"]/div/div/div/div[1]"));
        WebElement emptyScreenSubtitle = driver.findElement(By.xpath("//*[@id=\"stream-purchases__layout\"]/div/div/div/div[2]"));
        wait.until(ExpectedConditions.visibilityOf(emptyScreenTitle));
        wait.until(ExpectedConditions.visibilityOf(emptyScreenSubtitle));
        String emptyTitle = emptyScreenTitle.getAttribute("innerText");
        String emptySubtitle = emptyScreenSubtitle.getAttribute("innerText");
        Assert.assertEquals(emptyTitle, "Покупок пока нет");
        Assert.assertEquals(emptySubtitle, "Покупайте и смотрите новинки не выходя из дома");
        return this;
    }

    public YandexEfirPage setPlayerToFullscreenMode() {
        WebElement player = driver.findElement(By.cssSelector("[class=\"StreamPlayer\"]"));
        Dimension dimension = player.getSize();
        int heightSmall = dimension.getHeight();
        int widthSmall = dimension.getWidth();

        player.click();

        WebElement fullscreenButton = driver.findElement(By.cssSelector("[data-control-name=\"fullscreen\"]"));
        String fullScreenText = fullscreenButton.getAttribute("outerText");
        Assert.assertEquals(fullScreenText, "Развернуть во весь экран");
        fullscreenButton.click();

        dimension = player.getSize();
        int heightFullscreen = dimension.getHeight();
        int widthFullscreen = dimension.getWidth();

        if (heightFullscreen < heightSmall && widthFullscreen < widthSmall) {
            Assert.fail("Player size did not changed");
        }

        WebElement fullScreenPlayer = driver.findElement(By.xpath("//div[contains(@class,'stream_fullscreen_yes')]"));
        fullScreenPlayer.isDisplayed();
        return this;
    }

    public YandexEfirPage checkPlayerPlaying() {
        WebElement playingCheck = driver.findElement(By.xpath("//div[contains(@class,'stream-watching_state_playing')]"));
        playingCheck.isDisplayed();
        return this;
    }

    public YandexEfirPage search(String request) throws InterruptedException {
        WebElement searchBar = driver.findElement(By.id("uniq15887768464221"));
        searchBar.sendKeys(request);
        searchBar.sendKeys(Keys.RETURN);

        Thread.sleep(5000);

        WebElement results = driver.findElement(By.xpath("//*[@id=\"stream-serp__layout\"]/div/div[2]"));
        searchResultsCount = Integer.parseInt(results.getAttribute("childElementCount"));

        WebElement firsAsset = driver.findElement(By.xpath("//*[@id=\"stream-serp__layout\"]/div/div[2]/div[1]/div/div/div[2]/div[1]"));
        firstAssetName = firsAsset.getAttribute("innerText");

        WebElement metaOfFirstAsset = driver.findElement(By.xpath("//*[@id=\"stream-serp__layout\"]/div/div[2]/div[1]/div/div/div[2]/div[2]"));
        meta = metaOfFirstAsset.getAttribute("innerText");

        someResultApiArray = searchCheck(request);
        return this;
    }
}
