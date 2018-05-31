import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Dice GAME
 * Each PLAYER
 * 5 DICES - 6-sided DICE
 * re-roll some OR all of the dice upto THREE times (including the original roll)
 */
public class yatzyTest {

    private void assertComputeScore(String category, List<Integer> rolls, int expected) {
        assertEquals(expected, computeScore(category, rolls));
    }

    private List<Integer> arrayOf(int first, int second, int third, int fourth, int five) {
        return Arrays.asList(first, second, third, fourth, five);
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

    @Test
    public void scoreIsSumOf2HighestMatchingDice() {
        assertComputeScore("pair", arrayOf(3, 3, 3, 4, 4), 8);
    }

    private int computeScore(String category, List<Integer> rolls) {
        HashMap<String, IntSupplier> commands = new HashMap<>();
        commands.put("chance", () -> rolls.stream().mapToInt(i -> i.intValue()).sum());
        commands.put("yatzy", () -> getSumWhenAllNumberAreTheSameOtherwise0(rolls));
        commands.put("ones", () -> getSumBasedOnDiceNumber(rolls, 1));
        commands.put("twos", () -> getSumBasedOnDiceNumber(rolls, 2));
        commands.put("threes", () -> getSumBasedOnDiceNumber(rolls, 3));
        commands.put("fours", () -> getSumBasedOnDiceNumber(rolls, 4));
        commands.put("fives", () -> getSumBasedOnDiceNumber(rolls, 5));
        commands.put("sixes", () -> getSumBasedOnDiceNumber(rolls, 6));
        commands.put("pair", () -> getSumOfHighest(rolls));

        return commands.getOrDefault(category, () -> 0).getAsInt();
    }

    private int getSumOfHighest(List<Integer> rolls) {
        int maxOne = rolls.stream()
                .mapToInt(i -> i.intValue())
                .max()
                .getAsInt();
        int index = rolls.indexOf(maxOne);
        rolls.set(index, 0);
        System.out.println(index);
        return rolls.stream().mapToInt(i -> i.intValue()).max().getAsInt() + maxOne;
//        return 0;
    }

    private int getSumWhenAllNumberAreTheSameOtherwise0(List<Integer> rolls) {
        return rolls.stream()
                .distinct()
                .count() == 1 ? 50 : 0;
    }
    private int getSumBasedOnDiceNumber(List<Integer> rolls, int number) {
        return rolls.stream()
                .filter(i -> i == number)
                .mapToInt(i -> i.intValue())
                .sum();
    }
}
