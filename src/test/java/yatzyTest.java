import com.sun.java.swing.plaf.windows.WindowsTreeUI;
import org.junit.Test;

import java.util.*;
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
        assertComputeScore("pair", arrayOf(1, 1, 6, 2, 6), 12);
        assertComputeScore("pair", arrayOf(3, 3, 3, 4, 1), 6);
        assertComputeScore("pair", arrayOf(3, 3, 3, 3, 1), 6);
    }

    @Test
    public void ifTwoPairsOfDiceWithSameNumberThePlayerScoresTheSumOfTheseDice() {
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 3, 3), 0);
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 3, 4), 0);
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 2, 2), 0);
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
        commands.put("two pairs", () -> sumTwoPairsOfDiceWithSameNumber(rolls));

        return commands.getOrDefault(category, () -> 0).getAsInt();
    }

    private int sumTwoPairsOfDiceWithSameNumber(List<Integer> rolls) {
        List<Integer> distinctNumbers = rolls.stream().distinct().collect(Collectors.toList());
        if (distinctNumbers.size() < 2) return 0;
        List<Integer> frequency = distinctNumbers.stream()
                .map(i -> Collections.frequency(rolls, i)).collect(Collectors.toList());
        return 0;
//        if(Collections.frequency(frequency, 2) > 2) return 0;


//        return 2 * (distinctNumbers.get(frequency.lastIndexOf(2)) + distinctNumbers.get(frequency.indexOf(2)));
    }

    private int getSumOfHighest(List<Integer> rolls) {
        List<Integer> listOfNumbersFoundInPairs = new ArrayList<>();
        for(int i = 0; i < rolls.size(); i++) {
            for(int j = i+1; j < rolls.size(); j++) {
                if(rolls.get(i).equals(rolls.get(j))) {
                    listOfNumbersFoundInPairs.add(rolls.get(i));
//                    rolls.remove(rolls.get(j));
                    rolls.set(j, 0);
                    break;
                }
            }
            rolls.set(i, 0);
//            rolls.remove(rolls.get(i));
        }


        return Collections.max(listOfNumbersFoundInPairs) * 2;


//        int maxOne = getMaxOfArrayList(rolls);
//        rolls.set(rolls.indexOf(maxOne), 0);
//        return getMaxOfArrayList(rolls) + maxOne;
    }

    private int getMaxOfArrayList(List<Integer> rolls) {
        return rolls.stream()
                .mapToInt(i -> i.intValue())
                .max()
                .getAsInt();
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
