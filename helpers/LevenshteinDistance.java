package helpers;

public class LevenshteinDistance {

    /**
     * Calcule la distance de Levenshtein caractère par caractère entre deux chaînes.
     *
     * @param s1 La première chaîne
     * @param s2 La seconde chaîne
     * @return La distance de Levenshtein
     */
    public static int distanceCharLevel(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int cost = s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1;
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1,        // Suppression
                                    dp[i][j - 1] + 1),       // Insertion
                            dp[i - 1][j - 1] + cost  // Substitution
                    );
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }
}