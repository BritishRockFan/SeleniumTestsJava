package ru.yandexEfir;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.Assert;
import org.junit.Test;
import ru.yandexEfir.helpers.TestCase;
import ru.yandexEfir.pages.YandexEfirPage;

import java.io.IOException;

import static ru.yandexEfir.constants.Constants.EFIR_LINK;

public class YandexEfirTests extends TestCase {

    @Test
    @Description("1. Открыть браузер на полный экран и зайти на ya.ru")
    @Severity(SeverityLevel.NORMAL)
    public void openYandex() {
        yandexMainPage.open();
    }

    @Test
    @Description("2. Через пункт ЕЩЕ выбрать раздел Эфир")
    @Severity(SeverityLevel.NORMAL)
    public void openYandexEfir() {
        yandexMainPage
                .open()
                .pressMoreButton()
                .selectAllServices();
        yandexAllServicesPage
                .clickLink(EFIR_LINK);
        yandexEfirPage
                .checkThatOpened();
    }

    @Test
    @Description("3. Перейти в раздел Мои покупки - проверить что выводится заглушка и с правильным текстом " +
            "(Текст – Покупок пока нет. Покупайте и смотрите новинки не выходя из дома)")
    @Severity(SeverityLevel.NORMAL)
    public void checkTextInMyPurchases() {
        yandexEfirPage
                .open()
                .checkThatOpened()
                .navigateSideMenu(yandexEfirPage.myPurchases)
                .checkPurchasesIsEmpty();
    }

    @Test
    @Description("4. Перейти в раздел Фильмы и перейти на 3 фильм во второй полке")
    @Severity(SeverityLevel.NORMAL)
    public void selectAsset() throws IOException {
        yandexEfirPage
                .open()
                .checkThatOpened()
                .navigateSideMenu(yandexEfirPage.myFilms)
                .selectVideoAsset(2, 3);
    }

    @Test
    @Description("5. Проверить что отображаемые данные года и рейтинга в карточке фильма соответствует ответу сервера")
    @Severity(SeverityLevel.CRITICAL)
    public void checkDataInAssetCard() throws IOException {
        yandexEfirPage
                .open()
                .checkThatOpened()
                .navigateSideMenu(yandexEfirPage.myFilms)
                .selectVideoAsset(2,3);

    }

    @Test
    @Description("6. Развернуть плеер в полноэкранный режим")
    @Severity(SeverityLevel.CRITICAL)
    public void checkPlayerFullscreenMode() throws IOException {
        yandexEfirPage
                .open()
                .checkThatOpened()
                .navigateSideMenu(yandexEfirPage.myFilms)
                .selectVideoAsset(2,3)
                .setPlayerToFullscreenMode();
    }

    @Test
    @Description("7. Проверить что плеер играет")
    @Severity(SeverityLevel.BLOCKER)
    public void checkAssetPlayback() throws IOException {
        yandexEfirPage
                .open()
                .checkThatOpened()
                .navigateSideMenu(yandexEfirPage.myFilms)
                .selectVideoAsset(2,3)
                .setPlayerToFullscreenMode()
                .checkPlayerPlaying();
    }

    @Test
    @Description("8. Ввести в поиск слово \"Апгрейд\", нажать Enter для вывода результатов – проверить что " +
            "количество элементов результата поиска соответствует ответу сервера, а также проверить наличие " +
            "фильма Апгрейд и что параметры фильма соответствуют тому, что отдает сервер")
    @Severity(SeverityLevel.BLOCKER)
    public void checkSearchResults() throws InterruptedException {
        String meta = "2018 • фантастика, боевик, триллер, детектив, криминал";
        String request = "Апгрейд";
        yandexEfirPage
                .open()
                .checkThatOpened()
                .search(request);
        Assert.assertEquals(YandexEfirPage.searchResultsCount, YandexEfirPage.arrayWithMoviesInResults.size());

        for (int i = 0; i < YandexEfirPage.arrayWithMoviesInResults.size() - 1; i++) {
            if (YandexEfirPage.arrayWithMoviesInResults.get(i).name.equals(request)) {
                Assert.assertEquals(YandexEfirPage.arrayWithMoviesInResults.get(i).name, YandexEfirPage.firstAssetName);
                Assert.assertEquals(YandexEfirPage.arrayWithMoviesInResults.get(i).meta, YandexEfirPage.meta);
                Assert.assertEquals(YandexEfirPage.arrayWithMoviesInResults.get(i).meta, meta);
                return;
            } else {
                Assert.fail("There's no requested asset");
            }
        }
    }
}
