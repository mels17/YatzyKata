import org.junit.Test;

import java.util.*;
import java.util.function.IntSupplier;
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
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 3, 3), 8);
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 3, 4), 0);
        assertComputeScore("two pairs", arrayOf(1, 1, 2, 2, 2), 6);
    }

    @Test
    public void ifThereAreThreeDiceWithSameNumber_ReturnSumOfDice() {
        assertComputeScore("three of a kind", arrayOf(3, 3, 3, 4, 5), 9);
        assertComputeScore("three of a kind", arrayOf(3, 1, 3, 4, 5), 0);
        assertComputeScore("three of a kind", arrayOf(3, 3, 3, 3, 1), 9);
    }

    @Test
    public void ifThereAreFourDiceWithTheSameNumberReturnSumOfDice() {
        assertComputeScore("four of a kind", arrayOf(2, 2, 2, 2, 5), 8);
        assertComputeScore("four of a kind", arrayOf(2, 2, 2, 5, 5), 0);
        assertComputeScore("four of a kind", arrayOf(2, 2, 2, 2, 2), 8);
    }

    @Test
    public void straightDiceTest() {
        assertComputeScore("small straight", arrayOf(1, 2, 3, 4, 5), 15);
        assertComputeScore("large straight", arrayOf(2, 3, 4, 5, 6), 20);
    }

    @Test
    public void fullHouseTest() {
        assertComputeScore("full house", arrayOf(1, 1, 2, 2, 2), 8);
        assertComputeScore("full house", arrayOf(2, 2, 3, 3, 4), 0);
        assertComputeScore("full house", arrayOf(4, 4, 4, 4, 4), 0);
    }
    private int computeScore(String category, List<Integer> rolls) {
        HashMap<String, IntSupplier> commands = new HashMap<>();
        commands.put("chance", () -> getDiceSum(rolls));
        commands.put("yatzy", () -> getSumWhenAllNumberAreTheSameOtherwise0(rolls));
        commands.put("ones", () -> getSumBasedOnDiceNumber(rolls, 1));
        commands.put("twos", () -> getSumBasedOnDiceNumber(rolls, 2));
        commands.put("threes", () -> getSumBasedOnDiceNumber(rolls, 3));
        commands.put("fours", () -> getSumBasedOnDiceNumber(rolls, 4));
        commands.put("fives", () -> getSumBasedOnDiceNumber(rolls, 5));
        commands.put("sixes", () -> getSumBasedOnDiceNumber(rolls, 6));
        commands.put("pair", () -> getSumOfHighest(rolls));
        commands.put("two pairs", () -> sumTwoPairsOfDiceWithSameNumber(rolls));
        commands.put("three of a kind", () -> sumOfSameKind(rolls, 3));
        commands.put("four of a kind", () -> sumOfSameKind(rolls, 4));
        commands.put("small straight", () -> straightGetScore(rolls, 1));
        commands.put("large straight", () -> straightGetScore(rolls, 2));
        commands.put("full house", () -> fullHouseGetScore(rolls));

        return commands.getOrDefault(category, () -> 0).getAsInt();
    }

    private int fullHouseGetScore(List<Integer> rolls) {
        int threeSum = sumOfSameKind(rolls, 3);
        int twoSum = 0;
        List<Integer> distinctNumbers = rolls.stream().distinct().collect(Collectors.toList());
        List<Integer> frequencies = distinctNumbers.stream()
                .map(i -> Collections.frequency(rolls, i))
                .collect(Collectors.toList());


        for (int i = 0; i < frequencies.size(); i++){
            if(frequencies.get(i) == 2)
                twoSum = distinctNumbers.get(i) * 2;
        }
        if (twoSum == 0 || threeSum == 0) {
            return 0;
        }
        return twoSum + threeSum;
    }

    private int getDiceSum(List<Integer> rolls) {
        return rolls.stream().mapToInt(i -> i.intValue()).sum();
    }

    private int straightGetScore(List<Integer> rolls, int startNumber) {
        List<Integer> straightDice = IntStream.rangeClosed(startNumber, startNumber + 4)
                .boxed().collect(Collectors.toList());
        return  rolls.equals(straightDice) ? getDiceSum(straightDice) : 0;
    }

    private int sumOfSameKind(List<Integer> rolls, int number) {

        List<Integer> distinctNumbers = rolls.stream().distinct().collect(Collectors.toList());
        List<Integer> frequencies = distinctNumbers.stream()
                .map(i -> Collections.frequency(rolls, i))
                .collect(Collectors.toList());


        for (int i = 0; i < frequencies.size(); i++){
            if(frequencies.get(i) >= number) return distinctNumbers.get(i) * number;
        }
        return 0;
    }

    private int sumTwoPairsOfDiceWithSameNumber(List<Integer> rolls) {
        List<Integer> listOfNumbersFoundInPairs = getListOfNumbersFoundInPairs(rolls);
        return listOfNumbersFoundInPairs.size() < 2 ? 0 : getDiceSum(listOfNumbersFoundInPairs) * 2;

    }

    private int getSumOfHighest(List<Integer> rolls) {
        List<Integer> listOfNumbersFoundInPairs = getListOfNumbersFoundInPairs(rolls);
        return Collections.max(listOfNumbersFoundInPairs) * 2;
    }

    private List<Integer> getListOfNumbersFoundInPairs(List<Integer> rolls) {
        List<Integer> listOfNumbersFoundInPairs = new ArrayList<>();
        for(int i = 0; i < rolls.size(); i++) {
            for(int j = i+1; j < rolls.size(); j++) {
                if(rolls.get(i).equals(rolls.get(j))) {
                    listOfNumbersFoundInPairs.add(rolls.get(i));
                    rolls.set(j, 0);
                    break;
                }
            }
            rolls.set(i, 0);
        }
        return listOfNumbersFoundInPairs;
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
