package Calculator;

import javax.swing.*;
import java.util.ArrayList;

/**
 * New Version of {@link Operations}
 *
 * TODO:
 *  - Sub
 *  - Brackets und Punkt vor Strich (See note)
 *  - the calcArr should be parsed from top to bottom, so the first sub-calc gets processed first
 */
public class Operations_V3 {

    private double result;
    private double tempRes;
    private JTextArea textArea;
    private StringBuilder calc;
    private StringBuilder temp;
    private ArrayList<ArrayList<String>> calcArr; //the complete calculation
    private static final String SIGNS = "+-*/.?"; //? is for denoting the index of a sub-calculation

    public Operations_V3(JTextArea textArea) {
        this.textArea = textArea;
        calc = new StringBuilder();
        calcArr = new ArrayList<>();
    }

    /**
     * Testing-Constructor
     */
    public Operations_V3(StringBuilder calc) {
        this.calc = calc;
        this.calcArr = new ArrayList<>();
    }

    /**
     * Testing-Constructor
     */
    public Operations_V3() {
        this.calc = new StringBuilder();
        this.calcArr = new ArrayList<>();
    }

    /**
     * Parses the input from the user and appends it to the calculation.
     */
    public void parse(String input) {
        if (calc.isEmpty() && result != 0) {
            calc.append(result);
        }
        calc.append(input);
    }

    /**
     * Evaluates each Calculation in the calculations array.
     */
    public void evaluate() {
        temp = calc;
        try {
            sortCharacters();
        } catch (InvalidInputException e) {
            calcArr.clear();
            //textArea.setText("Invalid Input: " + calc);
        }
        calculate();
        temp = new StringBuilder();
    }

    /**
     * Calculates the complete calculation by first evaluating all sub-calculations and purring the
     * Results into the overall calculation.
     */
    private void calculate() {
        ArrayList<String> subCalc;
        ArrayList<String> mainCalc;
        char sign = ' ';
        int counter = 0;
        String c;

        for (int i = 0; i < calcArr.size(); i++) {
            subCalc = calcArr.get(i);

            insertSubResult(subCalc, i);

            for (int j = 0; j < subCalc.size(); j++) {
                c = subCalc.get(j);

                if (c.contains("?")) {
                    subCalc.set(j, tempRes + "");
                    subCalc.remove(j+1);
                }

                if (!c.equals("(") && !c.equals(")")) {
                    if (SIGNS.contains(c + "")) {
                        sign = c.charAt(0);
                    } else {
                        pickOperation(Double.parseDouble(c), sign);
                    }
                }
            }
        }

        mainCalc = calcArr.get(calcArr.size() - 1);

        for (int i = 0; i < mainCalc.size(); i++) {
            if (mainCalc.get(i).contains("?")) {
                mainCalc.set(i, calcArr.get(Integer.parseInt(mainCalc.get(i + 1))).toString());
                mainCalc.remove(i + 1);
            }
        }
    }

    private void insertSubResult(ArrayList<String> subCalc, int index) {
        for (int j = 0; j < subCalc.size(); j++) {

            if (subCalc.get(j).contains("?")) {
                calcArr.set(index, calcArr.get(subCalc.get(subCalc.indexOf("?")).charAt(1)));
            }
        }
    }

    /**
     * Check each character and puts them in an array. If one Object is supposed to be a float or a multi digit number,
     * then it will be handled accordingly.
     */
    public ArrayList<String> splitCalc(StringBuilder calcToSplit) {
        ArrayList<String> split = new ArrayList<>();
        int counter = 0;

        counter = splitUpAndCountPoints(split, calcToSplit, counter);

        while (counter > 0) {
            for (int i = 0; i < split.size(); i++) {
                if (foundFloat(split, i)) break;
            }
            counter--;
        }
        return split;
    }

    /**
     * Searches the split ArrayList for all digits of a float. If all were found, all the digits are deleted
     * and the complete float will be inserted into split at the given index.
     *
     * @param split     ArrayList<String> containing the individual digits and signs
     * @param index     index of the '.' in split
     */
    private boolean foundFloat(ArrayList<String> split, int index) {
        StringBuilder num = new StringBuilder();
        int right = 0;
        int left = 0;

        if (split.get(index).equals(".")) {
            left = getLeftSide(split, num, left, index);
            num.reverse();
            num.append(".");
            getRightSide(split, num, right, left, index);
            split.set((index - left), num.toString());
            return true;
        }
        return false;
    }

    /**
     * Finds all the digits on the left side of a float and adds them to the num StringBuilder.
     *
     * @param split     ArrayList<String> containing the individual digits and signs
     * @param num       StringBuilder to be filled with the float digits
     * @param left      amount of digits on the left
     * @param index     index of the '.' in split
     *
     * @return integer left, the amount of digits which were removed from split on the left to be added to num.
     */
    private int getLeftSide(ArrayList<String> split, StringBuilder num, int left, int index) {
        for (int j = index - 1; j >= 0; j--, left++) {
            if (findAllDigits(split, index -(1+ left), num)) {
                break;
            }
        }
        return left;
    }

    /**
     * Finds all the digits on the right side of a float and adds them to the num StringBuilder. Then the
     * StringBuilder replaces the '.' in the split ArrayList.
     *
     * @param split     ArrayList<String> containing the individual digits and signs
     * @param num       StringBuilder to be filled with the float digits
     * @param right     amount of digits on the right
     * @param left      amount of digits on the left
     * @param index     index of the '.' in split
     */
    private void getRightSide(ArrayList<String> split, StringBuilder num, int right, int left, int index) {
        for (int j = split.size() - (index + 1); j < split.size() - 1; j++, right++) {
            if (findAllDigits(split, (index - left)+1, num)) {
                break;
            }
        }
    }

    /**
     * Adds each character from calc to the split ArrayList.
     * - If the character is a '.', the counter will be incremented.
     * - If the character is a '?', the next character will be added aswell.
     *
     * @param split
     * @param calcToSplit
     * @param counter
     */
    private int splitUpAndCountPoints(ArrayList<String> split, StringBuilder calcToSplit, int counter) {
        for (int i = 0; i < calcToSplit.length(); i++) {
            if (calcToSplit.charAt(i) == '?') {
                split.add(calcToSplit.charAt(i) + calcToSplit.charAt(i + 1) + "");
                i++;
            } else {
                split.add(calcToSplit.charAt(i) + "");
            }
            if (calcToSplit.charAt(i) == '.') {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Checks if a character is a operation sign or a variable.
     *
     * @param split     ArrayList<String> containing the individual digits and signs
     * @param num       StringBuilder to be filled with the float digits
     * @param index     index of the character in split
     */
    private boolean findAllDigits(ArrayList<String> split, int index, StringBuilder num) {
        if(!SIGNS.contains(split.get(index))) {
            num.append(split.remove(index));
            return false;
        } else {
            return true;
        }
    }

    /**
     * ({@link Operations_V3#evaluate}) Goes through the calc-StringBuilder and decides if a character is a number or
     * an operation. If the Input is invalid, the process is stopped.
     */
    private void sortCharacters() throws InvalidInputException {
        ArrayList<Integer[]> bracketIndices;
        int i = 0;

        if (calc.toString().contains("(")) {
            bracketIndices = findBrackets();

            while (bracketIndices.size() != 0) {
                creatSubCalculations(bracketIndices.get(0)[0], bracketIndices.get(0)[1], i);
                bracketIndices = findBrackets();
                i++;
            }

        } else if (calc.toString().contains("*") || calc.toString().contains("/")) {

        }

        calcArr.add(splitCalc(temp));
    }

    /**
     * ({@link Operations_V3#sortCharacters}) Creates sub-calculations for the individual bracket groups by selecting all involved elements
     * in the calculation, putting them in a separate StringBuilder, and adding them into calcArr.
     * Then it replaces the extracted sub-calculation in the main calculation with "?i", wherein i is
     * the index of the sub-calculation.
     *
     * @param start
     * @param end
     * @param groupIndex
     */
    private void creatSubCalculations(int start, int end, int groupIndex) {
        ArrayList<String> subcalc = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            subcalc.add(temp.charAt(i) + "");
        }
        calcArr.add(subcalc);
        temp.replace(start, end+1, "?" + groupIndex);
    }

    /**
     * ({@link Operations_V3#sortCharacters}) Finds all the indices of the brackets in the calculation.
     *
     * @throws InvalidInputException if open brackets are left unclosed.
     */
    private ArrayList<Integer[]> findBrackets() throws InvalidInputException {
        ArrayList<Integer> startIndices = new ArrayList<>();
        ArrayList<Integer> endIndices = new ArrayList<>();
        ArrayList<Integer[]> bracketGroups = new ArrayList<>();

        //refactor into findIndices()
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '(') {
                startIndices.add(i);
            } else if (temp.charAt(i) == ')') {
                endIndices.add(i);
            }
        }

        //refactor into sortBrackets():
        if (startIndices.size() == endIndices.size()) {
            pairUpBrackets(bracketGroups, startIndices, endIndices);
        } else {
            throw new InvalidInputException();
        }

        return bracketGroups;
    }

    /**
     * ({@link Operations_V3#findBrackets}) Find the individual bracket groups in the calc StringBuilder.
     *
     * @param bracketGroups     ArrayList<Integer[]> to be filled with the index-pairs of the bracket groups
     * @param startIndices      ArrayList<Integer> contains all indices of '(' brackets
     * @param endIndices        ArrayList<Integer> contains all indices of ')' brackets
     */
    private void pairUpBrackets(ArrayList<Integer[]> bracketGroups, ArrayList<Integer> startIndices, ArrayList<Integer> endIndices) {
        assert (startIndices.size() == endIndices.size());
        int size = startIndices.size();

        /*if (bothBracketTypes(startIndices, endIndices)) {
            splitUpBracketProblem(startIndices, endIndices);
        } else if (bracketsInBrackets(startIndices, endIndices)) {*/
        for (int i = size; i > 0; i--) {
            bracketGroups.add(new Integer[] {startIndices.remove(i-1), endIndices.remove(0)});
        }/*
        } else if (bracketsNextToBrackets(startIndices, endIndices)) {
            //custom shit
        } */
    }

    /**
     * Helper for {@link Operations#evaluate}
     * Decides which operation will be executed.
     *
     * @param num       Number to be added/subtracted to the result or by which the
     *                  result will be multiplied/divided.
     * @param sign    char deciding which operation will be chosen
     */
    private void pickOperation(double num, char sign) {
        switch (sign) {
            case ' ' -> {
                result = num;
                tempRes = num;
            }
            case '*' -> mult(num);
            case '/' -> div(num);
            case '+' -> add(num);
            case '-' -> sub(num);
            case '.' -> point(num);
        }
    }

    /**
     * Only clears the visual on the calculator.
     */
    public void softClear() {
        textArea.setText("");
    }

    /**
     * Clears the complete calculation.
     */
    public void hardClear() {
        result = 0;
        calc = new StringBuilder();
        calcArr.clear();
        softClear();
    }

    public void add(double addNum) {
        result += addNum;
        tempRes += addNum;
    }

    public void sub(double subNum) {
        result -= subNum;
        tempRes -= subNum;
    }

    public void mult(double multNum){
        result *= multNum;
        tempRes *= multNum;
    }

    public void div(double divNum){
        try {
            assert divNum != 0;
            result /= divNum;
            tempRes /= divNum;
        } catch (AssertionError e) {
            hardClear();
            System.out.println("You cannot divide by 0!");

        }
    }

    public void point(double num) {

    }

    public StringBuilder getCalc() {
        return calc;
    }

    public ArrayList<ArrayList<String>> getCalcArr() {
        return calcArr;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
