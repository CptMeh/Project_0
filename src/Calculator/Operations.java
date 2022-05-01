package Calculator;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Responsible for all addition, subtraction, multiplication, and division operations.
 * It saves the result of the last executed operation.
 *
 * TODO:
 *  - Sub
 *  - Brackets und Punkt vor Strich (See note)
 *  - the calcArr should be parsed from top to bottom, so the first sub-calc gets processed first
 */
public class Operations {

    private double result;
    private double tempRes;
    private JTextArea textArea;
    private StringBuilder calc;
    private StringBuilder temp;
    private ArrayList<ArrayList<String>> calcArr; //the complete calculation
    private static final String SIGNS = "+-*/.?"; //? is for denoting the index of a sub-calculation

    public Operations(JTextArea textArea) {
        this.textArea = textArea;
        calc = new StringBuilder();
        calcArr = new ArrayList<>();
    }

    /**
     * Testing-Constructor
     */
    public Operations(StringBuilder calc) {
        this.calc = calc;
        this.calcArr = new ArrayList<>();
    }

    /**
     * Testing-Constructor
     */
    public Operations() {
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
            if (textArea != null) {
                textArea.setText("Invalid Input: " + calc);
            }
        }
        calculate();
        temp = new StringBuilder();
    }

    /**
     * ({@link Operations#evaluate}) Goes through the calc StringBuilder and decides if a character is a number or
     * an operation. If the Input is invalid, the process is stopped.
     */
    private void sortCharacters() throws InvalidInputException {
        ArrayList<Integer[]> bracketIndices;
        int i = 0;
        /*
        int offset = 0;
        int right = 0;
        int left = 0;


        for (int j = 0; j < calc.length() + offset; j++) {
            if (calc.charAt(j) == '*' || calc.charAt(j) == '/') {
                for (int r = 0; r < calc.length(); r++) {
                    if (SIGNS.contains(calc.charAt(r) + "")  && calc.charAt(r) != '.') {
                        calc.insert(r, ")");
                        j++;
                    }
                }
                for (int l = j-1; l >= 0; l--) {
                    if (SIGNS.contains(calc.charAt(l) + "") && calc.charAt(l) != '.') {
                        calc.insert(l, "(");
                        j++;
                    }
                }
                offset++;
            }
        }

        System.out.println(calc);*/

        if (calc.toString().contains("(")) {
            bracketIndices = findBrackets();
            while (bracketIndices.size() != 0) {
                creatSubCalculations(bracketIndices.get(0)[0], bracketIndices.get(0)[1], i);
                bracketIndices = findBrackets();
                i++;
            }
            calcArr.add(splitCalc(temp));
        }


        calcArr.add(splitCalc(temp));
    }

    /**
     * ({@link Operations#sortCharacters}) Creates sub-calculations for the individual bracket groups by selecting all
     * involved elements in the calculation, putting them in a separate ArrayList, and adding them into calcArr.
     * Then it replaces the extracted sub-calculation in the main calculation with "?i", wherein i is
     * the index of the sub-calculation.
     *
     * @param start
     * @param end
     * @param groupIndex
     */
    private void creatSubCalculations(int start, int end, int groupIndex) {
        calcArr.add(splitCalc(new StringBuilder(temp.substring(start + 1, end))));
        temp.replace(start, end+1, "?" + groupIndex);
    }

    /**
     * Check each character and puts them in an array. If one Object is supposed to be a float or a multi digit number,
     * then it will be handled accordingly.
     */
    public ArrayList<String> splitCalc(StringBuilder calcToSplit) {
        ArrayList<String> split = new ArrayList<>();
        int counter = 0;

        counter = splitUpAndCountPoints(split, calcToSplit, counter);
        concatenateNumber(split);

        findFloatNumbers(split, counter);

        return split;
    }

    /**
     * Searches the split ArrayList for all digits of a float. If all were found, all the digits are deleted
     * and the complete float will be inserted into split.
     *
     * @param split       ArrayList<String> containing the individual digits and signs
     * @param counter     amount of float numbers to be assembled
     */
    private void findFloatNumbers(ArrayList<String> split, int counter) {
        StringBuilder floatNum;
        for (int i = counter; i > 0; i--) {
            for (int j = 0; j < split.size(); j++) {
                if (split.get(j).equals(".")) {
                    floatNum = new StringBuilder();
                    floatNum.append(split.remove(j - 1));
                    floatNum.append(split.remove(j - 1));
                    floatNum.append(split.get(j - 1));
                    split.set(j-1, floatNum.toString());
                    break;
                }
            }
        }
    }

    /**
     * Finds all digits of a multi-digit integer and concatenates them into a single number.
     */
    private void concatenateNumber(ArrayList<String> split) {
        StringBuilder num;
        int counter;

        for (int i = 0; i < split.size(); i++) {
            num = new StringBuilder();
            counter = concatenateDigits(split, num, i);
            insertFloatIntoSplit(split, num, counter, i);
        }
    }

    private void insertFloatIntoSplit(ArrayList<String> split, StringBuilder num, int counter, int i) {
        if (counter >= 1) {
            split.set(i, num.toString());
            counter--;
            for (int j = counter; j > 0; j--) {
                split.remove(i +1);
            }
        }
    }

    private int concatenateDigits(ArrayList<String> split, StringBuilder num, int i) {
        int counter = 0;
        while ((i + counter) < split.size() && !SIGNS.contains(split.get(i)) && !SIGNS.contains(split.get(i + counter))) {
            num.append(split.get(i + counter));
            counter++;
        }
        return counter;
    }


    /**
     * Adds each character from calc to the split ArrayList.
     * - If the character is a '.', the counter will be incremented.
     * - If the character is a '?', the next character will be added as well.
     *
     * @param split
     * @param calcToSplit
     * @param counter
     */
    private int splitUpAndCountPoints(ArrayList<String> split, StringBuilder calcToSplit, int counter) {
        for (int i = 0; i < calcToSplit.length(); i++) {
            if (calcToSplit.charAt(i) == '?') {
                split.add(calcToSplit.charAt(i) + "" + calcToSplit.charAt(i + 1));
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
     * ({@link Operations#sortCharacters}) Finds all the indices of the brackets in the calculation.
     *
     * @throws InvalidInputException if open brackets are left unclosed.
     */
    private ArrayList<Integer[]> findBrackets() throws InvalidInputException {
        ArrayList<Integer> startIndices = new ArrayList<>();
        ArrayList<Integer> endIndices = new ArrayList<>();
        ArrayList<Integer[]> bracketGroups = new ArrayList<>();

        findIndices(startIndices, endIndices);
        sortBrackets(startIndices, endIndices, bracketGroups);

        return bracketGroups;
    }

    private void sortBrackets(ArrayList<Integer> startIndices, ArrayList<Integer> endIndices, ArrayList<Integer[]> bracketGroups) throws InvalidInputException {
        if (startIndices.size() == endIndices.size()) {
            pairUpBrackets(bracketGroups, startIndices, endIndices);
        } else {
            throw new InvalidInputException();
        }
    }

    private void findIndices(ArrayList<Integer> startIndices, ArrayList<Integer> endIndices) {
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == '(') {
                startIndices.add(i);
            } else if (temp.charAt(i) == ')') {
                endIndices.add(i);
            }
        }
    }

    /**
     * ({@link Operations#findBrackets}) Find the individual bracket groups in the calc StringBuilder.
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
     * Calculates the complete calculation by first solving all sub-calculations and putting the
     * Results into the overall calculation.
     */
    private void calculate() {
        char sign;

        for (ArrayList<String> subCalc : calcArr) {
            tempRes = 0;
            sign = ' ';
            insertSubResult(subCalc);

            for (String s : subCalc) {
                sign = solve(sign, s);
            }
            subCalc.clear();
            subCalc.add(tempRes + "");
        }
        result = tempRes;
    }

    /**
     * Decides, weather a character is an operation sign or a number. Then it will hand them to {@link Operations#pickOperation}
     * where it is decided what will happen.
     *
     * @param sign
     * @param character
     */
    private char solve(char sign, String character) {
        if (!character.equals("(") && !character.equals(")")) {
            if (SIGNS.contains(character + "")) {
                sign = character.charAt(0);
            } else {
                pickOperation(Double.parseDouble(character), sign);
            }
        }
        return sign;
    }

    /**
     * Finds the Result of the sub-calculation in subCalc at the given index and inserts it into the calculation.
     *
     * @param subCalc
     */
    private void insertSubResult(ArrayList<String> subCalc) {
        for (int j = 0; j < subCalc.size(); j++) {
            if (subCalc.get(j).contains("?")) {
                subCalc.set(j, calcArr.get(Integer.parseInt(subCalc.get(j).charAt(1) + "")).get(0));
            }
        }
    }

    /**
     * Helper for {@link Operations_V1#evaluate}
     * Decides which operation will be executed.
     *
     * @param num       Number to be added/subtracted to the result or by which the
     *                  result will be multiplied/divided.
     * @param sign    char deciding which operation will be chosen
     */
    private void pickOperation(double num, char sign) {
        switch (sign) {
            case ' ' -> {
                //result = num;
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
        tempRes = 0;
        calc = new StringBuilder();
        calcArr.clear();
        softClear();
    }

    public void add(double addNum) {
        //result += addNum;
        tempRes += addNum;
    }

    public void sub(double subNum) {
        //result -= subNum;
        tempRes -= subNum;
    }

    public void mult(double multNum){
        //result *= multNum;
        tempRes *= multNum;
    }

    public void div(double divNum){
        try {
            assert divNum != 0;
            //result /= divNum;
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
