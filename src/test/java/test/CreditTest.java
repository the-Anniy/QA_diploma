package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DBUtils;
import data.FormPage;
import data.Status;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;

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
    @DisplayName("Payment through card with 15 symbols")
    void shouldPayNotFullCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber("444444444444444");
        formPage.setCardMonth("06");
        formPage.setCardYear("26");
        formPage.setCardOwner("Ivan Ivanov");
        formPage.setCardCVV("478");
        formPage.pushContinueButton();
        formPage.checkMessageWrongFormat();
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
        formPage.setCardMonth("13");
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
        formPage.setCardYear("24");
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
    @DisplayName("Payment through card with cyrillic symbols in 'Owner' string")
    void shouldPayCyrillicCardOwner() {
        formPage.buyOnCredit();
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
        formPage.buyOnCredit();
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
        formPage.buyOnCredit();
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
        formPage.buyOnCredit();
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