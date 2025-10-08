package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Первая страница заказа
    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By lastNameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroInput = By.xpath("//input[@placeholder='* Станция метро']");
    private final By phoneInput = By.xpath("//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath("//button[text()='Далее']");

    // Вторая страница заказа - дата и календарь
    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By calendar = By.className("react-datepicker"); // календарь
    private final By todayDate = By.xpath("//div[contains(@class, 'react-datepicker__day--today')]");
    private final By calendarDays = By.xpath("//div[contains(@class, 'react-datepicker__day ')]");

    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.xpath("//div[@class='Dropdown-option']");
    private final By blackColorCheckbox = By.id("black");
    private final By greyColorCheckbox = By.id("grey");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[contains(@class, 'Button_Button__ra12g') and contains(@class, 'Button_Middle__1CSJM') and text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successModal = By.xpath("//div[contains(@class, 'Order_ModalHeader')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillFirstStep(String name, String lastName, String address, String metro, String phone) {
        // Ждем загрузки формы
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));

        // Заполняем поля
        driver.findElement(nameInput).sendKeys(name);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(addressInput).sendKeys(address);

        // Выбор станции метро
        driver.findElement(metroInput).click();
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='" + metro + "']")));
        metroOption.click();

        driver.findElement(phoneInput).sendKeys(phone);

        // Кликаем "Далее"
        WebElement nextBtn = driver.findElement(nextButton);
        scrollToElement(nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        nextBtn.click();
    }

    public void fillSecondStep(String date, String rentalPeriodText, String color, String comment) {
        // Ждем загрузки второй страницы
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));

        // Выбор даты через календарь (вместо ввода текста)
        selectTodayDate();

        // Выбор срока аренды
        selectRentalPeriod(rentalPeriodText);

        // Выбор цвета
        selectColor(color);

        // Заполнение комментария
        driver.findElement(commentInput).sendKeys(comment);

        // Нажатие кнопки заказа
        clickOrderButton();

        // Подтверждение заказа
        confirmOrder();
    }

    private void selectTodayDate() {
        try {
            System.out.println("Выбираем сегодняшнюю дату");

            // Кликаем на поле ввода даты чтобы открыть календарь
            WebElement dateField = driver.findElement(dateInput);
            dateField.click();

            // Ждем появления календаря
            wait.until(ExpectedConditions.visibilityOfElementLocated(calendar));

            // Получаем сегодняшнюю дату
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String todayFormatted = today.format(formatter);

            System.out.println("Сегодняшняя дата: " + todayFormatted);

            // Пробуем найти и кликнуть на сегодняшнюю дату
            WebElement todayElement = wait.until(ExpectedConditions.elementToBeClickable(todayDate));
            System.out.println("Найдена сегодняшняя дата, кликаем");
            todayElement.click();

            System.out.println("✓ Дата выбрана: " + todayFormatted);

        } catch (Exception e) {
            System.out.println("Ошибка при выборе даты: " + e.getMessage());

            // Альтернативный способ - кликнуть на текущий день в календаре
            try {
                selectCurrentMonthDate();
            } catch (Exception ex) {
                System.out.println("Альтернативный способ также не сработал");
            }
        }
    }

    private void selectCurrentMonthDate() {
        try {
            // Получаем текущий день месяца
            int currentDay = LocalDate.now().getDayOfMonth();
            System.out.println("Текущий день месяца: " + currentDay);

            // Ищем все дни в календаре
            List<WebElement> days = driver.findElements(calendarDays);
            System.out.println("Найдено дней в календаре: " + days.size());

            // Ищем день с текущим числом
            for (WebElement day : days) {
                String dayText = day.getText().trim();
                if (!dayText.isEmpty() && dayText.equals(String.valueOf(currentDay))) {
                    System.out.println("Найден день " + currentDay + ", кликаем");
                    day.click();
                    System.out.println("✓ Дата выбрана: " + currentDay);
                    return;
                }
            }

            System.out.println("День " + currentDay + " не найден в календаре");

        } catch (Exception e) {
            System.out.println("Ошибка в альтернативном выборе даты: " + e.getMessage());
        }
    }

    private void selectRentalPeriod(String rentalPeriodText) {
        try {
            System.out.println("Выбираем срок аренды: " + rentalPeriodText);

            // Находим и кликаем на дропдаун
            WebElement rentalPeriodElement = driver.findElement(rentalPeriod);
            scrollToElement(rentalPeriodElement);
            wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodElement));

            System.out.println("Кликаем на дропдаун выбора срока аренды");
            rentalPeriodElement.click();

            // Ждем появления опций
            wait.until(ExpectedConditions.visibilityOfElementLocated(rentalPeriodOptions));

            // Находим все доступные опции
            List<WebElement> options = driver.findElements(rentalPeriodOptions);
            System.out.println("Найдено опций: " + options.size());

            // Выводим все доступные опции для отладки
            for (WebElement option : options) {
                System.out.println("Доступная опция: '" + option.getText() + "'");
            }

            // Ищем нужную опцию по тексту
            WebElement targetOption = null;
            for (WebElement option : options) {
                if (option.getText().trim().equals(rentalPeriodText)) {
                    targetOption = option;
                    break;
                }
            }

            if (targetOption != null) {
                System.out.println("Найдена опция: '" + targetOption.getText() + "', кликаем");
                scrollToElement(targetOption);
                wait.until(ExpectedConditions.elementToBeClickable(targetOption));
                targetOption.click();
                System.out.println("✓ Срок аренды выбран: " + rentalPeriodText);
            } else {
                System.out.println("✗ Опция '" + rentalPeriodText + "' не найдена");
                // Пробуем кликнуть по первой опции как запасной вариант
                if (!options.isEmpty()) {
                    System.out.println("Кликаем по первой доступной опции: " + options.get(0).getText());
                    options.get(0).click();
                }
            }

        } catch (Exception e) {
            System.out.println("Ошибка при выборе срока аренды: " + e.getMessage());
        }
    }

    private void selectColor(String color) {
        try {
            if ("black".equals(color)) {
                WebElement blackCheckbox = driver.findElement(blackColorCheckbox);
                scrollToElement(blackCheckbox);
                System.out.println("Выбираем черный цвет");
                blackCheckbox.click();
            } else if ("grey".equals(color)) {
                WebElement greyCheckbox = driver.findElement(greyColorCheckbox);
                scrollToElement(greyCheckbox);
                System.out.println("Выбираем серый цвет");
                greyCheckbox.click();
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выборе цвета: " + e.getMessage());
        }
    }

    private void clickOrderButton() {
        try {
            WebElement orderBtn = driver.findElement(orderButton);
            System.out.println("Кликаем кнопку 'Заказать'");
            orderBtn.click();
        } catch (Exception e) {
            System.out.println("Ошибка при клике на кнопку заказа: " + e.getMessage());
        }
    }

    private void confirmOrder() {
        try {
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
            System.out.println("Подтверждаем заказ - кликаем 'Да'");
            confirmBtn.click();
        } catch (Exception e) {
            System.out.println("Ошибка при подтверждении заказа: " + e.getMessage());
        }
    }

    public boolean isOrderSuccess() {
        try {

            wait.until(ExpectedConditions.visibilityOfElementLocated(successModal));


            WebElement successElement = driver.findElement(successModal);
            String actualText = successElement.getText();

            System.out.println("Текст сообщения: " + actualText);


            boolean isSuccess = actualText.contains("Заказ оформлен");

            if (isSuccess) {
                System.out.println("✓ Заказ успешно оформлен");
            } else {
                System.out.println("✗ Текст сообщения не соответствует ожидаемому");
            }

            return isSuccess;

        } catch (Exception e) {
            System.out.println("Модальное окно успеха не появилось: " + e.getMessage());
            return false;
        }
    }

    private void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
    }
}