package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DBUtils;
import data.DataHelper;
import page.FormPage;
import data.Status;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;


public class CreditTest {
    private FormPage formPage;

    @BeforeEach
    void setUpPage() {
        String appUrl = "http://localhost";
        int appPort = 8080;
        formPage = new FormPage(appUrl + ":" + appPort);
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
    @DisplayName("Payment through approved card, database check")
    void shouldPayByApprovedCardCreditStatusDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
        DBUtils.checkCreditStatus(Status.APPROVED);
    }

    @Test
    @DisplayName("Payment through declined card, database check")
    void shouldPayByDeclinedCardInCreditStatusInDB() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateDeclinedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
        DBUtils.checkCreditStatus(Status.DECLINED);
    }

    @Test
    @DisplayName("Payment through card with 15 symbols")
    void shouldPayNotFullCard() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithShortLength());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through invalid card number")
    void shouldPayInvalidCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithLettersOrSymbols());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Input of 17 symbols in 'Card number', string limitation")
    void shouldNotAllow17DigitsInCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateInvalidCardNumberWithLongLength());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through card with invalid month")
    void shouldPayInvalidMonth() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateInvalidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Month' string")
    void shouldPayInvalidMonth00() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateInvalidMonthZero());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверно указан срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with invalid year")
    void shouldPayInvalidYear() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateExpiredYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with '00' in 'Year' string")
    void shouldPayInvalidYear00() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateInvalidYearZero());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Истёк срок действия карты");
    }

    @Test
    @DisplayName("Payment through card with cyrillic symbols in 'Owner' string")
    void shouldPayCyrillicCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner("Иван Иванов");
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Payment through card with invalid owner")
    void shouldPayInvalidCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateInvalidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkMessageError();
    }

    @Test
    @DisplayName("Input of 4 symbols in 'CVV', string limitation")
    void shouldNotAllow4DigitsInCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateFourDigitsCVV());
        // Смотрим автоматическую обрезку 4-го символа и успешность отправки формы
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Input of value '1000' in 'CVV' string, border check")
    void shouldNotAllow41000InCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateBoundaryFourDigitCVV());
        // Смотрим автоматическую обрезку 4-го символа и успешность отправки формы
        formPage.pushContinueButton();
        formPage.checkMessageSuccess();
    }

    @Test
    @DisplayName("Payment through empty 'Card number' string")
    void shouldPayEmptyCardNumber() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateEmptyCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Month' string")
    void shouldPayEmptyCardMonth() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateEmptyString());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Year' string")
    void shouldPayEmptyCardYear() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateEmptyString());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }

    @Test
    @DisplayName("Payment through empty 'Owner' string")
    void shouldPayEmptyCardOwner() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateEmptyString());
        formPage.setCardCVV(DataHelper.generateRandomValidCVV());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Поле обязательно для заполнения");
    }

    @Test
    @DisplayName("Payment through empty 'CVV' string")
    void shouldPayEmptyCardCVV() {
        formPage.buyOnCredit();
        formPage.setCardNumber(DataHelper.generateApprovedCardNumber());
        formPage.setCardMonth(DataHelper.generateRandomValidMonth());
        formPage.setCardYear(DataHelper.generateRandomValidYear());
        formPage.setCardOwner(DataHelper.generateRandomValidOwnerName());
        formPage.setCardCVV(DataHelper.generateEmptyString());
        formPage.pushContinueButton();
        formPage.checkInputSubMessage("Неверный формат");
    }
}