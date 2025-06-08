package src.helpers;

public class RomanConverter {

    public static String toRomanWithDot(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("Le nombre doit être compris entre 1 et 3999.");
        }

        String[] milliers = {"", "M", "MM", "MMM"};
        String[] centaines = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] dizaines = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] unites = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        String roman =
                milliers[number / 1000]
                        + centaines[(number % 1000) / 100]
                        + dizaines[(number % 100) / 10]
                        + unites[number % 10];

        return roman + ".";
    }
}
