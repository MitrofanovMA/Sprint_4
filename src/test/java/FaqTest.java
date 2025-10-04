import org.example.MainPage;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class FaqTest extends BaseTest {

    @Test
    public void testFaqSection() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.acceptCookies();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        int questionsCount = mainPage.getFaqQuestionsCount();
        System.out.println("Найдено вопросов: " + questionsCount);


        for (int i = 0; i < questionsCount && i < 8; i++) {
            System.out.println("Тестируем вопрос №" + (i + 1));


            mainPage.clickFaqQuestion(i);


            boolean isDisplayed = mainPage.isFaqAnswerDisplayed(i);
            assertTrue("Ответ на вопрос " + (i + 1) + " не отображается", isDisplayed);


            String answerText = mainPage.getFaqAnswerText(i);
            System.out.println("Вопрос " + (i + 1) + " ответ: " +
                    (answerText.length() > 50 ? answerText.substring(0, 50) + "..." : answerText));


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}