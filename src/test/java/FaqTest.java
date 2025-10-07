import org.example.MainPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class FaqTest extends BaseTest {

    private final int questionNumber;

    public FaqTest(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        return Arrays.asList(new Object[][]{
                {0},
                {1},
                {2},
                {3},
                {4},
                {5},
                {6},
                {7}
        });
    }

    @Test
    public void testFaqQuestion() {
        MainPage mainPage = new MainPage(driver);
        mainPage.open();
        mainPage.acceptCookies();


        System.out.println("Тестируем вопрос №" + (questionNumber + 1));

        mainPage.clickFaqQuestion(questionNumber);

        boolean isDisplayed = mainPage.isFaqAnswerDisplayed(questionNumber);
        assertTrue("Ответ на вопрос " + (questionNumber + 1) + " не отображается", isDisplayed);

        String answerText = mainPage.getFaqAnswerText(questionNumber);
        System.out.println("Вопрос " + (questionNumber + 1) + " ответ: " +
                (answerText.length() > 50 ? answerText.substring(0, 50) + "..." : answerText));
    }
}
