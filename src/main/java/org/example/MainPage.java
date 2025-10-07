package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Кнопка куки
    private final By cookieButton = By.id("rcc-confirm-button");

    // Кнопка «Заказать» вверху страницы
    private final By orderButtonTop = By.xpath("//button[text()='Заказать']");

    // Кнопка «Заказать» внизу страницы
    private final By orderButtonBottom = By.xpath("//div[contains(@class, 'Home_FinishButton')]//button");

    // Вопросы о важном
    private final By faqQuestions = By.xpath("//div[@data-accordion-component='AccordionItemButton']");
    private final By faqAnswers = By.xpath("//div[@data-accordion-component='AccordionItemPanel']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        //URL сервиса
        String url = "https://qa-scooter.praktikum-services.ru/";
        driver.get(url);
    }

    public void acceptCookies() {
        try {
            WebElement cookieBtn = wait.until(ExpectedConditions.elementToBeClickable(cookieButton));
            cookieBtn.click();
        } catch (Exception e) {
            System.out.println("Куки уже приняты или кнопка не найдена");
        }
    }

    public void clickOrderButtonTop() {
        List<WebElement> orderButtons = driver.findElements(orderButtonTop);
        if (!orderButtons.isEmpty()) {
            WebElement topButton = orderButtons.get(0);
            scrollToElement(topButton);
            wait.until(ExpectedConditions.elementToBeClickable(topButton));
            topButton.click();
        }
    }

    public void clickOrderButtonBottom() {
        WebElement bottomButton = driver.findElement(orderButtonBottom);
        scrollToElement(bottomButton);
        wait.until(ExpectedConditions.elementToBeClickable(bottomButton));
        bottomButton.click();
    }

    private void scrollToElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void clickFaqQuestion(int index) {
        List<WebElement> questions = driver.findElements(faqQuestions);
        if (index < questions.size()) {
            WebElement question = questions.get(index);
            scrollToElement(question);
            wait.until(ExpectedConditions.elementToBeClickable(question));
            question.click();
            // Ждем анимацию
            wait.until(ExpectedConditions.visibilityOf(question));
        }
    }

    public String getFaqAnswerText(int index) {
        List<WebElement> answers = driver.findElements(faqAnswers);
        if (index < answers.size()) {
            wait.until(ExpectedConditions.visibilityOf(answers.get(index)));
            return answers.get(index).getText();
        }
        return "";
    }

    public boolean isFaqAnswerDisplayed(int index) {
        List<WebElement> answers = driver.findElements(faqAnswers);
        if (index < answers.size()) {
            try {
                wait.until(ExpectedConditions.visibilityOf(answers.get(index)));
                return answers.get(index).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}