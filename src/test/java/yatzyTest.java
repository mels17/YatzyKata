import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Dice GAME
 * Each PLAYER
 * 5 DICES - 6-sided DICE
 * re-roll some OR all of the dice upto THREE times (including the original roll)
 */
public class yatzyTest {

    private void assertComputeScore(String category, int[] rolls, int expected) {
        assertEquals(expected, computeScore(category, rolls));
    }

    private int[] arrayOf(int first, int second, int third, int fourth, int five) {
        return new int[]{first, second, third, fourth, five};
    }

    @Test
    public void playerScoresSumOfAllDiceNoMatterWhatTheyRead() {
        assertComputeScore("chance", arrayOf(1, 1, 3, 3, 6), 14);
        assertComputeScore("chance", arrayOf(4, 5, 5, 6, 1), 21);
    }

    @Test
    public void ifAllTheDiceRollsHaveTheSameNumberPlayerScores50() {
        assertComputeScore("yatzy", arrayOf(1, 1, 1, 1, 1), 50);
        assertComputeScore("yatzy", arrayOf(1, 1, 2, 1, 1), 0);
    }

    @Test
    public void playerScoresTheSumOfDiceThatReads1_Or_2_Or_3_Or_4_Or_5_Or_6() {
        assertComputeScore("ones", arrayOf(3, 3, 3, 4, 5), 0);
        assertComputeScore("twos", arrayOf(2, 3, 2, 5, 1), 4);
        assertComputeScore("threes", arrayOf(2, 3, 2, 5, 1), 3);
        assertComputeScore("fours", arrayOf(1, 1, 2, 4, 4), 8);
        assertComputeScore("fives", arrayOf(1, 5, 2, 5, 5), 15);
        assertComputeScore("sixes", arrayOf(6, 5, 2, 5, 5), 6);
    }

    private int computeScore(String category, int[] rolls) {

//        int score = 0;
        HashMap<String, IntSupplier> commands = new HashMap<>();
        commands.put("chance", () -> Arrays.stream(rolls).sum());
        commands.put("yatzy", () -> getSumWhenAllNumberAreTheSameOtherwise0(rolls));
        commands.put("ones", () -> getSumBasedOnDiceNumber(rolls, 1));
        commands.put("twos", () -> getSumBasedOnDiceNumber(rolls, 2));
        commands.put("threes", () -> getSumBasedOnDiceNumber(rolls, 3));
        commands.put("fours", () -> getSumBasedOnDiceNumber(rolls, 4));
        commands.put("fives", () -> getSumBasedOnDiceNumber(rolls, 5));
        commands.put("sixes", () -> getSumBasedOnDiceNumber(rolls, 6));

        return commands.get(category).getAsInt();
//        switch (category) {
//            case "chance":
//                score = Arrays.stream(rolls).sum();
//                break;
//            case "yatzy":
//                score = Arrays.stream(rolls)
//                        .distinct()
//                        .count() == 1 ? 50 : 0;
//                break;
//            case "ones":
//                score = getSumBasedOnDiceNumber(rolls, 1);
//                break;
//            case "twos":
//                score = getSumBasedOnDiceNumber(rolls, 2);
//                break;
//            case "threes":
//                score = getSumBasedOnDiceNumber(rolls, 3);
//                break;
//            case "fours":
//                score = getSumBasedOnDiceNumber(rolls, 4);
//                break;
//            case "fives":
//                score = getSumBasedOnDiceNumber(rolls, 5);
//                break;
//            case "sixes":
//                score = getSumBasedOnDiceNumber(rolls, 6);
//                break;
//        }
//        return score;
    }

    private int getSumWhenAllNumberAreTheSameOtherwise0(int[] rolls) {
        return Arrays.stream(rolls)
                .distinct()
                .count() == 1 ? 50 : 0;
    }
    private int getSumBasedOnDiceNumber(int[] rolls, int number) {
        return Arrays.stream(rolls)
                .filter(i -> i == number)
                .sum();
    }
}
