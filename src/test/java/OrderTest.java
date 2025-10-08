import org.example.MainPage;
import org.example.OrderPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {

    private final String name;
    private final String lastName;
    private final String address;
    private final String metro;
    private final String phone;
    private final String rentalPeriod;
    private final String color;
    private final String comment;
    private final String entryPoint;

    public OrderTest(String name, String lastName, String address, String metro,
                     String phone, String rentalPeriod, String color,
                     String comment, String entryPoint) {
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.metro = metro;
        this.phone = phone;
        this.rentalPeriod = rentalPeriod;
        this.color = color;
        this.comment = comment;
        this.entryPoint = entryPoint;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                        "Иван",
                        "Петров",
                        "ул. Ленина, д. 10",
                        "Черкизовская",
                        "+79991234567",
                        "сутки",
                        "black",
                        "Позвонить за час",
                        "top"
                },
                {
                        "Мария",
                        "Сидорова",
                        "пр. Мира, д. 25",
                        "Сокольники",
                        "+79997654321",
                        "двое суток",
                        "grey",
                        "Оставить у двери",
                        "bottom"
                }
        });
    }

    @Test
    public void testPositiveOrderScenario() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.acceptCookies();

        // Выбор точки входа
        if ("top".equals(entryPoint)) {
            System.out.println("Используем верхнюю кнопку заказа");
            mainPage.clickOrderButtonTop();
        } else {
            System.out.println("Используем нижнюю кнопку заказа");
            mainPage.clickOrderButtonBottom();
        }

        OrderPage orderPage = new OrderPage(driver);

        System.out.println("Заполняем первую часть формы");
        orderPage.fillFirstStep(name, lastName, address, metro, phone);

        System.out.println("Заполняем вторую часть формы");

        orderPage.fillSecondStep("", rentalPeriod, color, comment);
        orderPage.isOrderSuccess();

    }
}