package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

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

    // Вторая страница заказа
    private final By dateInput = By.xpath("//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriod = By.className("Dropdown-placeholder");
    private final By rentalPeriodOption = By.xpath("//div[contains(@class, 'Dropdown-option')]");
    private final By blackColorCheckbox = By.id("black");
    private final By greyColorCheckbox = By.id("grey");
    private final By commentInput = By.xpath("//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath("//button[text()='Заказать']");
    private final By confirmButton = By.xpath("//button[text()='Да']");
    private final By successModal = By.xpath("//div[contains(@class, 'Order_ModalHeader')]");
    private final By successModalText = By.xpath("//div[contains(@class, 'Order_ModalHeader__3FDaJ') and text()='Заказ оформлен']");


    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillFirstStep(String name, String lastName, String address, String metro, String phone) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));


        driver.findElement(nameInput).sendKeys(name);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(addressInput).sendKeys(address);


        driver.findElement(metroInput).click();
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[text()='" + metro + "']")));
        metroOption.click();

        driver.findElement(phoneInput).sendKeys(phone);


        WebElement nextBtn = driver.findElement(nextButton);
        scrollToElement(nextBtn);
        wait.until(ExpectedConditions.elementToBeClickable(nextBtn));
        nextBtn.click();
    }

    public void fillSecondStep(String date, String rentalPeriodText, String color, String comment) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));


        WebElement dateField = driver.findElement(dateInput);
        dateField.clear();
        dateField.sendKeys(date);


        driver.findElement(rentalPeriod).click();


        WebElement rentalPeriodElement = driver.findElement(rentalPeriod);
        scrollToElement(rentalPeriodElement);
        wait.until(ExpectedConditions.elementToBeClickable(rentalPeriodElement));
        rentalPeriodElement.click();


        WebElement periodOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'Dropdown-option') and text()='" + rentalPeriodText + "']")));
        periodOption.click();


        if ("black".equals(color)) {
            WebElement blackCheckbox = driver.findElement(blackColorCheckbox);
            scrollToElement(blackCheckbox);
            blackCheckbox.click();
        } else if ("grey".equals(color)) {
            WebElement greyCheckbox = driver.findElement(greyColorCheckbox);
            scrollToElement(greyCheckbox);
            greyCheckbox.click();
        }


        driver.findElement(commentInput).sendKeys(comment);


        WebElement orderBtn = driver.findElement(orderButton);
        scrollToElement(orderBtn);
        wait.until(ExpectedConditions.elementToBeClickable(orderBtn));
        orderBtn.click();


        WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(confirmButton));
        confirmBtn.click();
    }

    public boolean isOrderSuccess() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successModalText));
            WebElement modalText = driver.findElement(successModalText);

            return modalText.isDisplayed() && modalText.getText().equals("Заказ оформлен");
        } catch (Exception e) {
            System.out.println("Модальное окно успеха не появилось или текст не соответствует ожидаемому: " + e.getMessage());
            return false;
        }
    }

    private void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", element);
    }
}