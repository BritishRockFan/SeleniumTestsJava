package ru.yandexEfir.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentParser {
    public static MovieCardMetadata movieCardMetadata;
    private static WebDriver driver;

    public DocumentParser(WebDriver driver) {
        this.driver = driver;
    }

    public static MovieCardMetadata parseAssetMeta(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        Elements elementsYear = document.getElementsByClass("stream-program-title__subtitle stream-watching__player-header-subtitle");
        Elements elementRating = document.getElementsByClass("stream-watching__restriction stream-program-title__restriction");
        String year = elementsYear.text().substring(0,4);
        String rating = elementRating.text();

        movieCardMetadata = new MovieCardMetadata(year, rating);

        return movieCardMetadata;
    }

    public static List<SearchResults> searchCheck(String request) throws InterruptedException {
        String url = "https://yandex.ru/efir?stream_active=serp&search_text=" + request;

        driver.get(url);
        Thread.sleep(5000);

//        JSWaiter.setDriver(driver);
//        JSWaiter.waitJQueryAngular();
        Document doc = Jsoup.parse(driver.getPageSource());

        Elements results = doc.getElementsByClass("Feed-Item Feed-Item_type_card Grid-Item");

        List<SearchResults> allResults = new ArrayList<>();

        results.forEach(result -> {
            Element nameElement = result.child(0).child(0).child(1).child(0);
            String name = nameElement.text();

            Element metaElement = result.child(0).child(0).child(1).child(1);
            String meta = metaElement.text();

            allResults.add(new SearchResults(name, meta));
        });

        return allResults;
    }
}

