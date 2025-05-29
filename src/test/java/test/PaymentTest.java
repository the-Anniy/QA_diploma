package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DBUtils;
import data.FormPage;
import data.Status;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

public class PaymentTest {

    private FormPage formPage;

    @BeforeEach
    void setUpPage() {
        formPage = new FormPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void clearAll() {
        DBUtils.clearAllData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Payment through approved card")
    void shouldPayByApprovedCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through declined card")
    void shouldPayByNotApprovedCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444442");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through card with 15 symbols")
    void shouldPayBynNotFullCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("444444444444444");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through invalid card number")
    void shouldPayInvalidNumberCard() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("444444444444AAAA");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();
    }

    @Test
    @DisplayName("Input of 17 symbols in 'Card number', string limitation")
    void shouldNotAllow17DigitsInCardNumber() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("44444444444444417"); // 17 символов
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.checkCardNumberInputValue("4444 4444 4444 4441");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through card with invalid month")
    void showPayBadMonth() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("13");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongDate();

    }

    @Test
    @DisplayName("Payment through card with '00' in 'Month' string")
    void showPayBadMonth00() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("00");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongDate();

    }

    @Test
    @DisplayName("Payment through card with invalid year")
    void showPayBadYear() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("24");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageOverDate();
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Year' string")
    void showPayBadYear00() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("00");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageOverDate();
    }

    @Test
    @DisplayName("Payment through card with cyrillic symbols in 'Owner' string")
    void shouldPayCyrillicCardOwner() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Иван Иванов");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }
    @Test
    @DisplayName("Payment through card with invalid owner")
    void shouldPayInvalidCardOwner() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("08");
        formPage.setCardYear("25");
        formPage.setCardOwner("Ivan 23$8 Ivanov");
        formPage.setCardCVV("345");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Input of 4 symbols in 'CVV', string limitation")
    void shouldNotAllow4DigitsInCVV() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("2486");
        formPage.checkCVVInputValue("248");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }
    @Test
    @DisplayName("Input of value '1000'in 'CVV' string, border check")
    void shouldNotAllow41000InCVV() {
        formPage.buyForYourMoney();

        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("1000");
        formPage.checkCVVInputValue("100");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through empty 'Card number' string")
    void showPayWithEmptyCardNumber() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();
    }


    @Test
    @DisplayName("Payment through empty 'Month' string")
    void showPayWithEmptyCardMonth() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();

    }

    @Test
    @DisplayName("Payment through empty 'Year' string")
    void showPayWithEmptyCardYear() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();

    }

    @Test
    @DisplayName("Payment through empty 'Owner' string")
    void showPayWithEmptyCardOwner() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageRequiredField();

    }

    @Test
    @DisplayName("Payment through empty 'CVV' string")
    void showPayWithEmptyCardCVV() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();

    }

    @Test
    @DisplayName("Payment through approved card, database check")
    void showPayAndEntryDB() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkPaymentStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Payment through declined card, database check")
    void shouldNoPayByDeclinedCardStatusInDB() {
        formPage.buyForYourMoney();
        formPage.setCardNumber("4444444444444442");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkPaymentStatus(Status.DECLINED);
    }
}
