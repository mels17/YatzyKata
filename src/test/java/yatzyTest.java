import org.junit.Before;
import org.junit.Test;
import yatzy.Yatzy;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Dice GAME
 * Each PLAYER
 * 5 DICES - 6-sided DICE
 * re-roll some OR all of the dice upto THREE times (including the original roll)
 */
public class yatzyTest {

    Yatzy yatzy;
    @Before
    public void setUp() {
        yatzy = new Yatzy();
    }

    private void assertComputeScore(String category, List<Integer> rolls, int expected) {
        assertEquals(expected, yatzy.computeScore(category, rolls));
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

}
