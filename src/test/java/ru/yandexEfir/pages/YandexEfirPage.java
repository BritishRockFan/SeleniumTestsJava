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

import static ru.yandexEfir.constants.Constants.*;
import static ru.yandexEfir.helpers.DocumentParser.parseAssetMeta;
import static ru.yandexEfir.helpers.DocumentParser.searchCheck;

public class YandexEfirPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public static int searchResultsCount;
    public static String firstAssetName;
    public static String meta;
    public static List<SearchResults> arrayWithMoviesInResults;

    @FindBy(css = "[href=\"/efir?stream_active=purchases&from_block=efir_newtab\"]")
    public WebElement myPurchases;

    @FindBy(css = "[href=\"/efir?stream_active=category&stream_category=film&from_block=efir_newtab\"]")
    public WebElement myFilms;

    @FindBy(xpath = "//*[@id=\"stream-storefront__gallery\"]/div/div[1]")
    public WebElement player;

    @FindBy(css = "[role=\"navigation\"]")
    public WebElement sideMenu;

    @FindBy(css = "[class=\"Cut Cut_oneLine Cut_1-20\"]")
    public WebElement whatToWatch;

    @FindBy(css = "[class=\"stream-program-title__title stream-watching__player-header-title i-multiline-overflow\"]")
    public WebElement assetTitleInPlayer;

    @FindBy(css = "[class=\"stream-program-title__subtitle stream-watching__player-header-subtitle\"]")
    public WebElement yearElementInMovieCard;

    @FindBy(css = "[class=\"stream-watching__restriction stream-program-title__restriction\"]")
    public WebElement ratingElementInMovieCard;

    @FindBy(xpath = "//*[@id=\"stream-purchases__layout\"]/div/div/div/div[1]")
    WebElement emptyScreenTitle;

    @FindBy(xpath = "//*[@id=\"stream-purchases__layout\"]/div/div/div/div[2]")
    WebElement emptyScreenSubtitle;

    @FindBy(css = "[class=\"StreamPlayer\"]")
    WebElement playerInMovieCard;

    @FindBy(css = "[data-control-name=\"fullscreen\"]")
    WebElement fullscreenButton;

    @FindBy(id = "uniq15887768464221")
    WebElement searchBar;

    @FindBy(xpath = "//*[@id=\"stream-serp__layout\"]/div/div[2]")
    WebElement resultsOnThePage;

    @FindBy(xpath = "//*[@id=\"stream-serp__layout\"]/div/div[2]/div[1]/div/div/div[2]/div[1]")
    WebElement firsAssetInSearch;

    @FindBy(xpath = "//*[@id=\"stream-serp__layout\"]/div/div[2]/div[1]/div/div/div[2]/div[2]")
    WebElement metaOfFirstAsset;

    public YandexEfirPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, TIMEOUT);
    }

    public YandexEfirPage open() {
        driver.manage().window().maximize();
        driver.get(EFIR_URL);
        String title = driver.getTitle();
        Assert.assertEquals(YANDEX_EFIR_TITLE, title);

        wait.until(webDriver -> ((JavascriptExecutor) driver)
                .executeScript("return document.readyState").toString().equals("complete"));
        return this;
    }

    public YandexEfirPage checkThatOpened() {
        wait.until(ExpectedConditions.visibilityOf(playerInMovieCard));
        wait.until(ExpectedConditions.visibilityOf(sideMenu));
        //wait.until(ExpectedConditions.visibilityOf(whatToWatch));
        return this;
    }

    public YandexEfirPage navigateSideMenu(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        element.click();
        return this;
    }

    public YandexEfirPage selectVideoAsset(int row, int movie) throws IOException {
        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
        String pathToAsset = "//*[@id=\"stream-category__feed\"]/div/div[" + row + "]/div/div[2]/div/div[2]/div[" + movie + "]";

        WebElement asset = driver.findElement(By.xpath(pathToAsset));
        wait.until(ExpectedConditions.visibilityOf(asset));
        wait.until(ExpectedConditions.elementToBeClickable(asset));
        WebElement title = driver.findElement(By.xpath(pathToAsset + "/div/div/div[2]/div[1]/div[1]/a"));
        String assetName = title.getAttribute("innerText");

        WebElement linkElement = driver.findElement(By.xpath(pathToAsset + "/div/div/div[1]/div[1]/a"));
        String link = linkElement.getAttribute("href");

        asset.click();
        String assetNameInPlayer = assetTitleInPlayer.getAttribute("innerText");
        Assert.assertEquals(assetName, assetNameInPlayer);

        //Year and rating
        String year = yearElementInMovieCard.getAttribute("textContent").substring(0, 4);
        String rating = ratingElementInMovieCard.getAttribute("outerText");

        MovieCardMetadata movieCardMetadata = parseAssetMeta(link);
        Assert.assertEquals(year, movieCardMetadata.getYear());
        Assert.assertEquals(rating, movieCardMetadata.getRating());
        return this;
    }

    public YandexEfirPage checkPurchasesIsEmpty() {
        driver.manage().timeouts().implicitlyWait(TIMEOUT, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.visibilityOf(emptyScreenTitle));
        wait.until(ExpectedConditions.visibilityOf(emptyScreenSubtitle));
        String emptyTitle = emptyScreenTitle.getAttribute("innerText");
        String emptySubtitle = emptyScreenSubtitle.getAttribute("innerText");
        Assert.assertEquals(emptyTitle, "Покупок пока нет");
        Assert.assertEquals(emptySubtitle, "Покупайте и смотрите новинки не выходя из дома");
        return this;
    }

    public YandexEfirPage setPlayerToFullscreenMode() {
        Dimension dimension = playerInMovieCard.getSize();
        int heightSmall = dimension.getHeight();
        int widthSmall = dimension.getWidth();

        playerInMovieCard.click();

        String fullScreenText = fullscreenButton.getAttribute("outerText");
        Assert.assertEquals(fullScreenText, "Развернуть во весь экран");
        fullscreenButton.click();

        dimension = playerInMovieCard.getSize();
        int heightFullscreen = dimension.getHeight();
        int widthFullscreen = dimension.getWidth();

        if (heightFullscreen < heightSmall && widthFullscreen < widthSmall) { //Checking that dimension is scaling up just in case.
            Assert.fail("Player size did not changed");
        }

        WebElement fullScreenPlayer = driver.findElement(By.xpath("//div[contains(@class,'stream_fullscreen_yes')]"));
        Assert.assertTrue(fullScreenPlayer.isDisplayed());
        return this;
    }

    public YandexEfirPage checkPlayerPlaying() {
        WebElement playingCheck = driver.findElement(By.xpath("//div[contains(@class,'stream-watching_state_playing')]"));
        Assert.assertTrue(playingCheck.isDisplayed());
        return this;
    }

    public YandexEfirPage search(String request) throws InterruptedException {
        searchBar.sendKeys(request);
        searchBar.sendKeys(Keys.RETURN);

//        JSWaiter.setDriver(driver);
//        JSWaiter.waitJQueryAngular();

        Thread.sleep(5000);
        searchResultsCount = Integer.parseInt(resultsOnThePage.getAttribute("childElementCount"));
        firstAssetName = firsAssetInSearch.getAttribute("innerText");
        meta = metaOfFirstAsset.getAttribute("innerText");

        arrayWithMoviesInResults = searchCheck(request);
        return this;
    }
}
