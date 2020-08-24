package banking;

import java.util.Arrays;
import java.util.Random;

public class CardGenerator {
    private static final String MII = "400000";

    static String generateNumber() {
        Random random = new Random();

        String last = String.valueOf(random.nextInt(999999999));

        String allButLast = MII + "0".repeat(9 - last.length()) + last;

        return allButLast + luhn(allButLast);
    }

    static int luhn(String cardNumberWithoutLastDigit) {
        int[] cardDigits = Arrays.stream(cardNumberWithoutLastDigit
                .split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        int sum = 0;
        for (int i = 0; i < cardDigits.length; i++) {
            if (i % 2 == 0) {
                cardDigits[i] *= 2;
            }

            if (cardDigits[i] > 9) {
                cardDigits[i] -= 9;
            }

            sum += cardDigits[i];
        }

        return sum % 10 == 0 ? 0 : 10 - sum % 10;
    }

    static String generatePin() {
        Random random = new Random();

        String partialPass = String.valueOf(random.nextInt(9999));

        return "0".repeat(4 - partialPass.length()) + partialPass;
    }
}
