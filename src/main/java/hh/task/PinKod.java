package hh.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PinKod {
    private final String[][] ARRAY_PIN_PAD = {{"0", "8"}, {"1", "2", "4"}, {"1", "2", "3", "5"}, {"2", "3", "6"},
            {"1", "4", "5", "7"}, {"2", "4", "5", "6", "8"}, {"3", "5", "6", "9"},
            {"4", "7", "8"}, {"0", "5", "7", "8", "9"}, {"6", "8", "9"}};

    public String guessingPin(String supposedPin) {
        List<String> variationPinArr = guessingPin(supposedPin, 0);
        String variationPin = variationPinArr.toString();
        return variationPin.substring(1, variationPin.length() - 1).replaceAll(" ", "");
    }

    private List<String> guessingPin(String supposedPin, int indexPin) {
        if (supposedPin.length() - 1 > indexPin) {
            List<String> variationPin = new ArrayList<>();
            char[] arrCharSupposedPin = supposedPin.toCharArray();
            int numSupposedPin = Character.getNumericValue(arrCharSupposedPin[indexPin]);
            List<String> supVariationPin = guessingPin(supposedPin, ++indexPin);
            for (String numPinPad : ARRAY_PIN_PAD[numSupposedPin]) {
                for (String subNumPinPad : supVariationPin) {
                    variationPin.add(numPinPad + subNumPinPad);
                }
            }

            return variationPin;
        } else {
            return Arrays.asList(ARRAY_PIN_PAD[Integer.parseInt(supposedPin.substring(indexPin))]);
        }
    }

    public static void main(String[] args) {
        String example = new PinKod().guessingPin("46");
        String value = "13, 15, 16, 19, 43, 45, 46, 49, 53, 55, 56, 59, 73, 75, 76, 79";
        System.out.println(example);
        System.out.println(example.equals(value));
    }
}
