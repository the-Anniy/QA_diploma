package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DBUtils;
import data.FormPage;
import data.Status;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;

public class CreditTest {
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
    void shouldPayByAcceptedCard() {
        formPage.buyOnCredit();
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
    void shouldPayByDeclinedCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444442");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through unknown card")
    void shouldPayUnknownCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444443");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through invalid card number")
    void shouldPayInvalidCardNumber() {
        formPage.buyOnCredit();
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
        formPage.buyOnCredit();
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
    void shouldPayInvalidMonth() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("17");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongDate();
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Month' string")
    void shouldPayInvalidMonth00() {
        formPage.buyOnCredit();
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
    void shouldPayInvalidYear() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("22");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageOverDate();
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Year' string")
    void shouldPayInvalidYear00() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("00");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageOverDate();
    }

    @Test
    @DisplayName("Payment through card with invalid owner")
    void shouldPayInvalidCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan 258 Iвanoв");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through card with invalid CVV")
    void shouldPayInvalidCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("4DD");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();
    }

    @Test
    @DisplayName("Input of 4 symbols in 'CVV', string limitation")
    void shouldNotAllow4DigitsInCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("4444");
        formPage.checkCVVInputValue("444");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through empty 'Card number' string")
    void shouldPayEmptyCardNumber() {
        formPage.buyOnCredit();
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
    void shouldPayEmptyCardMonth() {
        formPage.buyOnCredit();
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
    void shouldPayEmptyCardYear() {
        formPage.buyOnCredit();
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
    void shouldPayEmptyCardOwner() {
        formPage.buyOnCredit();
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
    void shouldPayEmptyCardCVV() {
        formPage.buyOnCredit();
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
    void shouldPayByApprovedCardCreditStatusDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444441");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkCreditStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Payment through declined card, database check")
    void shouldPayByDeclinedCardInCreditStatusInDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber("4444444444444442");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkCreditStatus(Status.DECLINED);
    }
}