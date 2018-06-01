package yatzy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Yatzy {
    public int computeScore(String category, List<Integer> rolls) {
        /**
         * Command patters with IntSupplier as value - Good?
         * Object and Interfaces as value - I thought it would be over-complicating this. Thoughts?
         */
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

    /**
     * Found distinct numbers in the array to reduce number of iterations. Required? Better way?
     */
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

    /**
     * Two for loops - efficiency?
     */
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
