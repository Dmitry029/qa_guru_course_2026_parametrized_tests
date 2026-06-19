package quru.qa;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import quru.qa.data.Language;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тесты сайта авиакомпании Belavia")
public class DataProviderTests extends BaseTest {

    @BeforeEach
    public void setUpTests() {
        Selenide.open("");
    }

    @CsvSource(value = {
        "Беларуская , https://by.belavia.by/",
        "English , https://by.belavia.en/",
        "Русский , https://by.belavia"
    })

    @ParameterizedTest(name = "При выборе языка {0} должен быть следующий URL: {1}")
    @Tag("smoke")
    void checkUrlTest(String language, String expectedUrl) {
        $("#lang-title").click();
        String locator = String.format("//*[@class='menu-content']//*[contains(text(), '%s')]", language);
        $x(locator).click();
        assertThat(Objects.equals(url(), expectedUrl));
    }

    @ParameterizedTest(name = "При выборе языка {0} должен быть следующий URL: {1}. Данные берутся из файла")
    @Tag("regress")
    @CsvFileSource(
        resources = {"/test_data/languageAndUrls.csv"}
    )
    void checkUrlTestDataFromFile(String language, String expectedUrl) {
        $("#lang-title").click();
        String locator = String.format("//*[@class='menu-content']//*[contains(text(), '%s')]", language);
        $x(locator).click();
        assertThat(Objects.equals(url(), expectedUrl));
    }

    @MethodSource
    @ParameterizedTest(name = "При выборе языка {0} должен быть следующе пункты меню: {1}")
    @Tag("regress")
    void checkMainMenuTextTest(String country, List<String> expectedButtons) {
        $("#lang-title").click();
        String locator = String.format("//*[@class='menu-content']//*[contains(text(), '%s')]", country);
        $x(locator).click();
        List<String> actualButtons = $$("[class='nav-tabs'] li").texts();
        assertThat(actualButtons).containsAll(expectedButtons);
    }

    static Stream<Arguments> checkMainMenuTextTest() {
        return Stream.of(
            Arguments.of(
                Language.EN.description,
                List.of("Book Flights", "Online Check-in", "Booking status", "Transfer", "Hotels", "Search")
            ),
            Arguments.of(
                Language.RU.description,
                List.of("Купить", "Регистрация на рейс", "Статус бронирования", "Трансфер", "Отели", "Поиск")
            ),
            Arguments.of(
                Language.BY.description,
                List.of("Набыць", "Рэгістрацыя на рэйс", "Статус браніравання", "Трансфер", "Гатэлі", "Пошук")
            )
        );
    }
}