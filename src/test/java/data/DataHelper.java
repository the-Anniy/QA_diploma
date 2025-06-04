package data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    public static final String APPROVED_CARD_NUMBER = "4444444444444441";
    public static final String DECLINED_CARD_NUMBER = "4444444444444442";
    private static final String INVALID_OWNER = "Ivan 123$ Smith";
    private static final String EMPTY_STRING = "";
    private static final String INVALID_MONTH_ZERO = "00";

    public static String generateApprovedCardNumber() {
        return APPROVED_CARD_NUMBER;
    }

    public static String generateDeclinedCardNumber() {
        return DECLINED_CARD_NUMBER;
    }

    public static String generateRandomValidOwnerName() {
        String[] firstNames = {"Ivan", "Petr", "Sergey", "Alexey", "Dmitry", "Andrey", "Maxim", "Vasily"};
        String[] lastNames = {"Ivanov", "Petrov", "Sidorov", "Smirnov", "Kuznetsov", "Popov", "Lebedev"};
        Random random = new Random();
        return firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)];
    }

    public static String generateRandomValidCVV() {
        Random random = new Random();
        return String.format("%03d", random.nextInt(900) + 100);
    }

    public static String generateRandomValidMonth() {
        Random random = new Random();
        int monthValue = random.nextInt(12) + 1;
        return String.format("%02d", monthValue);
    }

    public static String generateRandomValidYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYearShort = Integer.parseInt(currentDate.format(DateTimeFormatter.ofPattern("yy")));
        Random random = new Random();
        int yearsToAdd = random.nextInt(5) + 1;
        int validYear = currentYearShort + yearsToAdd;
        return String.format("%02d", validYear);
    }

    public static String generateExpiredYear() {
        LocalDate currentDate = LocalDate.now();
        int currentYearShort = Integer.parseInt(currentDate.format(DateTimeFormatter.ofPattern("yy")));
        return String.format("%02d", currentYearShort - 1);
    }

    public static String generateInvalidYearZero() {
        return "00";
    }

    public static String generateInvalidMonth() {
        String[] invalidMonths = {"13", "99"};
        return invalidMonths[new Random().nextInt(invalidMonths.length)];
    }

    public static String generateInvalidMonthZero() {
        return INVALID_MONTH_ZERO;
    }

    public static String generateInvalidOwnerName() {
        return INVALID_OWNER;
    }

    public static String generateEmptyString() {
        return EMPTY_STRING;
    }

    public static String generateEmptyCardNumber() {
        return EMPTY_STRING;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String generateInvalidCardNumberWithShortLength() {
        int[] lengths = {0, 1, 8, 15};
        int length = lengths[new Random().nextInt(lengths.length)];
        return generateRandomDigits(length);
    }

    public static String generateInvalidCardNumberWithLongLength() {
        Random random = new Random();
        int randomDigit = random.nextInt(10);
        return APPROVED_CARD_NUMBER + randomDigit;
    }

    public static String generateInvalidCardNumberWithLettersOrSymbols() {
        String[] invalidFormats = {
                "444444444444444A",
                "4444 4444 444X 4444",
                "ABCD123456789012"
        };
        return invalidFormats[new Random().nextInt(invalidFormats.length)];
    }

    public static String generateFourDigitsCVV() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000);
        return String.valueOf(number);
    }

    public static String generateBoundaryFourDigitCVV() {
        return "1000";
    }
}