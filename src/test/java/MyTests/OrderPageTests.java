package MyTests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import PageObjects.MainPage;
import PageObjects.OrderPage;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;

import static org.hamcrest.CoreMatchers.containsString;

// Тест для проверки всего флоу позитивного сценария оформления заказа
@RunWith(Parameterized.class)
public class OrderPageTests {
    // Веб-драйвер
    private WebDriver webDriver;

    // URL тестируемой страницы
    private final String mainPageUrl = "https://qa-scooter.praktikum-services.ru";

    // Переменные для параметров теста - данных для оформления заказа
    private final String name, surname, address, metro, phone, date, term, color, comment;

    // Сообщение об успешном оформлении заказа
    private final String expectedOrderSuccessText = "Заказ оформлен";

    /**
     * Конструктор класса OrderPageTests
     * @param name Имя
     * @param surname Фамилия
     * @param address Адрес
     * @param metro  Метро
     * @param phone Телефон
     * @param date Дата
     * @param term Срок аренды
     * @param color Цвет
     * @param comment Комментарий
     */
    public OrderPageTests(
            String name,
            String surname,
            String address,
            String metro,
            String phone,
            String date,
            String term,
            String color,
            String comment
    ) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.date = date;
        this.term = term;
        this.color = color;
        this.comment = comment;
    }

    // Параметры для запуска теста @return массив параметров
    @Parameterized.Parameters(name = "Оформление заказа. Позитивный сценарий. Пользователь: {0} {1}")
    public static Object[][] setDataForOrder() {
        return new Object[][] {
                {"Иван",
                "Иванов",
                "Москва, ул. Пушкина, д. 1",
                "Черкизовская",
                "89998887766",
                "10.03.2026",
                "четверо суток",
                "чёрный жемчуг",
                "Позвоните за час"},
                        {"Мария",
                        "Смирнова",
                        "Москва, ул. Ленина, д. 15",
                        "Сокольники",
                        "89991112233",
                        "11.03.2026",
                        "двое суток",
                        "серая безысходность",
                        "Домофон не работает"},
        };
    }

    @Before
    public void startUp() {
        WebDriverManager.chromedriver().setup();
        this.webDriver = new ChromeDriver();
        this.webDriver.get(mainPageUrl);
    }

    @After
    public void tearDown() {
        this.webDriver.quit();
    }

    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в шапке
    @Test
    public void orderWithHeaderButtonWhenSuccess() {
        MainPage mainPage = new MainPage(this.webDriver);
        OrderPage orderPage = new OrderPage(this.webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonHeader();
        makeOrder(orderPage);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                orderPage.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }

    // Тест для проверки процесса оформления заказа после нажатия на кнопку "Заказать" в теле сайта
    @Test
    public void orderWithBodyButtonWhenSuccess() {
        MainPage mainPage = new MainPage(this.webDriver);
        OrderPage orderPage = new OrderPage(this.webDriver);

        mainPage.clickOnCookieAcceptButton();
        mainPage.clickOrderButtonBody();
        makeOrder(orderPage);

        MatcherAssert.assertThat(
                "Problem with creating a new order",
                orderPage.getNewOrderSuccessMessage(),
                containsString(this.expectedOrderSuccessText)
        );
    }

    // Метод, описывающий процедуру оформления заказа @param orderPage экземпляр образа страницы заказа
    private void makeOrder(OrderPage orderPage) {
        orderPage.waitForLoadForm();

        orderPage.setName(this.name);
        orderPage.setSurname(this.surname);
        orderPage.setAddress(this.address);
        orderPage.setMetro(this.metro);
        orderPage.setPhone(this.phone);

        orderPage.clickNextButton();

        orderPage.setDate(this.date);
        orderPage.setTerm(this.term);
        orderPage.setColor(this.color);
        orderPage.setComment(this.comment);

        orderPage.makeOrder();
    }
}
