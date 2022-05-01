package Calculator;

import javax.swing.*;
import java.util.ArrayList;



/**
 * First attempt of {@link Operations}
 * TODO:
 */
public class Operations_V1 {
    private double result;
    private double tempRes;
    private boolean punctuated;
    private JTextArea textArea;
    private ArrayList<String> calc; //the complete calculation

    public Operations_V1(JTextArea textArea) {
        this.textArea = textArea;
        calc = new ArrayList<>();
    }

    /**
     * For testing.
     */
    public Operations_V1(ArrayList<String> calc) {
        this.calc = calc;
    }

    /**
     * Parses the input from the user and appends it to the calculation.
     */
    public void parse(String nextStep) {
        if (!calc.isEmpty()){
            nextStep = setNegative(nextStep);
            if (!appendNumber(nextStep) && !beginMult() && !setBracketsForMult(nextStep)) {
                calc.add(nextStep);
            }
        } else {
            setLastResultAsFirstVariable(nextStep);
        }
    }

    /**
     * Helper for {@link Operations_V1#parse}.
     * Sets the first variable in the calculation to the result from the last calculation.
     */
    private void setLastResultAsFirstVariable(String nextStep) {
        try {
            Double.parseDouble(nextStep);
            calc.add(nextStep);
        } catch (NumberFormatException e) {
            calc.add("" + result);
            calc.add(nextStep);
        }
    }

    /**
     * Helper for {@link Operations_V1#parse}.
     * - If the next calculation is a mult / div, it sets the variables involved in the mult / div into
     *   brackets and returns true.
     * - Else it returns false.
     */
    private boolean setBracketsForMult(String nextStep) {
       if (!nextStep.equals("-") && calc.get(calc.size() - 1).equals("*") || calc.get(calc.size() - 1).equals("/")) {
            if (calc.get(calc.size() - 2).equals("}")) {
                calc.remove(calc.size() - 2);
            } else {
                setFirstBracket();
            }
            calc.add(nextStep);
            calc.add("}");
            return true;
        }
        return false;
    }

    /**
     * Helper for {@link Operations_V1#setBracketsForMult}.
     * Sets a "{" at the beginning of a mult / div.
     */
    private void setFirstBracket() {
        String temp1 = calc.get(calc.size() - 2);
        String temp2 = calc.get(calc.size() - 1);
        calc.set(calc.size() - 2, "{");
        calc.set(calc.size() - 1, temp1);
        calc.add(temp2);
    }

    /**
     * Helper for {@link Operations_V1#parse}.
     * - If the last calculation was completed and the user wants to mult / div with the result,
     *   it sets result as the first variable of the calc and then adds the rest. Afterwars it
     *   returns true.
     * - Else it returns false.
     */
    private boolean beginMult() {
        assert !calc.isEmpty();
        if (calc.size() < 2 && (calc.get(calc.size() - 1).equals("*") || calc.get(calc.size() - 1).equals("/"))) {
            calc.add(0, "" + result);
            result = 0;
            return true;
        }
        return false;
    }

    /**
     * Helper for {@link Operations_V1#parse}.
     * If a "-" sign was parsed, the next number will be made negative and the "-" sign in the calc
     * will be replaced with a "+".
     */
    private String setNegative(String nextStep) {
        if (calc.get(calc.size() - 1).equals("-")) {
            try {
                Double.parseDouble(calc.get(calc.size() - 2));
                calc.set(calc.size() - 1, "+");
            } catch (NumberFormatException ignored) {}
            nextStep = "-" + nextStep;
            calc.remove(calc.size() - 1);
        }
        return nextStep;
    }

    /**
     * Helper for {@link Operations_V1#parse}.
     * Appends the value of the nextStep String to the last part of calc, but only if the last part
     * was either an integer, or a ".".
     *
     * - If both the nextStep and the last part of calc are a ".", the last part will be replaced and
     *   this method return false.
     *
     * - If the last part in the calc is a "}":
     *      1. Removes the bracket.
     *      2. appends the number / "."
     *      3. appends the "}".
     *
     * @return true if nextStep was appended, false otherwise.
     */
    private boolean appendNumber(String nextStep) {
        String bracket;

        if (calc.get(calc.size() - 1).equals(".") && nextStep.equals(".")) {
            calc.remove(calc.size() - 1);
            return false;
        } else if (!nextStep.contains("-")) {
            bracket = checkForBrackets(nextStep);
            if (calc.size() >= 1 && (punctuated || nextStep.equals("."))) {
                return punctuated(nextStep, bracket);
            } else {
                return append(nextStep, bracket);
            }
        }
        return false;
    }

    /**
     * Helper for {@link Operations_V1#appendNumber}
     */
    private boolean punctuated(String nextStep, String bracket) {
        checkForPunctuation(nextStep);
        if (punctuated) {
            addNextStep(nextStep, bracket);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper for {@link Operations_V1#appendNumber}
     */
    private boolean append(String nextStep, String bracket) {
        try {
            Integer.parseInt(calc.get(calc.size() - 1));
            Integer.parseInt(nextStep);
            addNextStep(nextStep, bracket);
            return true;
        } catch (NumberFormatException ignored) {
            punctuated = false;
            return false;
        }
    }

    /**
     * Helper for {@link Operations_V1#punctuated}
     */
    private void checkForPunctuation(String nextStep) {
        if (!nextStep.equals(".")) {
            try {
                Integer.parseInt(nextStep);
            } catch (NumberFormatException e) {
                punctuated = false;
            }
        } else {
            punctuated = true;
        }
    }

    /**
     * Helper for {@link Operations_V1#appendNumber}.
     * Checks if the last part of calc is a ")", removes it from calc and sets and returns the
     * String bracket = ")".
     */
    private String checkForBrackets(String nextStep) {
        String bracket = "";

        try {
            Double.parseDouble(nextStep);
            if (calc.get(calc.size() - 1).equals("}")) {
                bracket = calc.remove(calc.size() - 1);
            }
        } catch (NumberFormatException ignored) {}
        return bracket;
    }
    /**
     * Helper for {@link Operations_V1#appendNumber}.
     * Add the nextStep String to the calc. If the previous part of the calc was a "}", the String
     * bracket will be added afterwards aswell.
     */
    private void addNextStep(String nextStep, String bracket) {
        String temp = calc.remove(calc.size() - 1);
        nextStep = temp + nextStep;

        calc.add(nextStep);
        if (bracket.equals("}")) {
            calc.add(bracket);
        }
    }

    /**
     * Evaluates each Calculation in the calculations array.
     */
    public void evaluate() {
        ArrayList<String> rest = new ArrayList<>(calc); //Don't know why but if you use rest directly in selectString it will alter calc directly
        ArrayList<String[][]> multDiv = getMultDiv(rest);

        multiplications(multDiv);
        replaceResult(multDiv);
        additions();
    }

    /**
     * Helper for {@link Operations_V1#evaluate}
     * Calculates all the multiplications / divisions in the multDiv ArrayList.
     * 1. Calls calculate.
     * 2. Sets the first value in the String[][] in multDiv to the tempRes.
     * 3. Sets tempRes = 0.
     *
     * Step 2 is necessary for {@link Operations_V1#replaceResult}.
     */
    private void multiplications(ArrayList<String[][]> multDiv) {
        char symbol = ' ';

        for (String[][] sss : multDiv) {
            for (int j = 0; j < sss[1].length; j++){
                symbol = calculate(symbol, sss[1][j],  true);
                sss[1][0] = "" + tempRes;
                multDiv.set(multDiv.indexOf(sss), sss);
            }
            tempRes = 0;
            symbol = ' ';
        }
    }

    /**
     * Helper for {@link Operations_V1#evaluate}.
     * Replaces the calculation in calc with its result from multDiv.
     * 1. Replaces the calculation.
     * 2. Deletes the Brackets.
     */
    private void replaceResult(ArrayList<String[][]> multDiv) {
        for (String[][] sss : multDiv) {
            for (int j = 0; j < sss[0].length; j++){
                if (j == sss[0].length-1) {
                    calc.set(Integer.parseInt(sss[0][0]), sss[1][0]);
                } else {
                    calc.remove(Integer.parseInt(sss[0][0]));
                }
            }
        }
        while (calc.contains("{") || calc.contains("}")) {
            calc.remove("{");
            calc.remove("}");
        }
    }

    /**
     * Helper for {@link Operations_V1#evaluate}.
     * 1. Calculates all additions (/subtractions) in the calc.
     * 2. Clears calc for next uses.
     */
    private void additions() {
        char symbol = ' ';

        for (String s : calc) {
            symbol = calculate(symbol, s, false);
        }
        calc.clear();
    }

    /**
     * Helper for {@link Operations_V1#multiplications} and {@link Operations_V1#additions}.
     * 1. Checks if the String s contains a Number.
     * 2a. If it does, it will be sent to {@link Operations_V1#pickOperation}.
     * 2b. if not it contains the Symbol for the calculation and will be saved as such.
     *
     * @param multDiv   boolean deciding if this is a mult/div or a add/sub
     * @param symbol    char sign of the operation
     * @param s         String containing either a number or a symbol
     *
     * @return the current Symbol, which can be used in the next call of this method.
     */
    private char calculate(char symbol, String s, boolean multDiv) {
        double num;

        try {
            num = Double.parseDouble(s);
            pickOperation(num, symbol, multDiv);
        } catch (NumberFormatException e) {
            symbol = s.charAt(0);
        }
        return symbol;
    }

    /**
     * Rearranges the calculation in a way that the "Punkt-Vor-Strich" rule is implemented.
     * 1. Searches for brackets,
     * 2. Adds everything in between into the order ArrayList<String[][]>,
     * 3. Deletes the brackets.
     *
     * (The calcCopy ArrayList has to be a copy of the actual calc, so the original won't get altered.)
     *
     * @param calcCopy      ArrayList<String> a copy of the calc ArrayList.
     * @return an ArrayList containing all the multiplications and divisions.
     */
    public ArrayList<String[][]> getMultDiv(ArrayList<String> calcCopy) {
        ArrayList<String[][]> multDiv = new ArrayList<>();
        ArrayList<String> rest = new ArrayList<>(calcCopy); //Don't know why but if you use rest directly in selectString it will alter calcCopy directly
        int i = 0; //to keep track of the indices in the rest ArrayList
        int j = 1; //to keep track of the indices in the calcCopy ArrayList (Dunno why but has to be = 1 at start)
        int size = rest.size();

        while (rest.contains("*") || (rest.contains("/"))) {
            if ((size <= 3 || (i <= rest.size() - 5) && rest.get(i).equals("{"))) {
                j = getMultDivGroups(multDiv, rest, i, j);
            } else {
                i++;
            }
            if (j > calc.size()) {
                throw new RuntimeException("Somehow it didn't find all the *.");
            }
            j++;
        }
        return multDiv;
    }

    /**
     * Helper for {@link Operations_V1#getMultDiv}.
     * Gathers all the multiplications and divisions in the multDiv ArrayList
     */
    private int getMultDivGroups(ArrayList<String[][]> multDiv, ArrayList<String> rest, int i, int j) {
        int offset = 0;
        //TODO: find a way to not have * and / in the same group.
        if (rest.get(i + 2).equals("*") || rest.get(i + 2).equals("/")) {
            offset = calculateOffset(rest, i, offset);
            return selectStrings(rest, multDiv, i + 1, j, offset);
        }
        return j;
    }

    /**
     * Helper for {@link Operations_V1#getMultDivGroups}.
     * Calculates how long the multDiv group is.
     * A group with one multiplication / division is 3 long. One with more than one is 3 + offset long.
     *
     * @param i         position of the first "{"
     * @param offset    how much longer the group is
     * @param rest      ArrayList<String> rest of the calculation without the multiplications / divisions.
     */
    private int calculateOffset(ArrayList<String> rest, int i, int offset) {
        if (rest.size() > (i + 3)) {
            for (offset = 0; offset < rest.size() - (i + 1); offset++) {
                if (rest.get(i + 3 + offset).equals("}")) {
                    break;
                }
            }
        }
        offset = (offset == 0) ? offset : offset - 1; //somehow only works if offset = offset - 1, except when offset == 0
        return offset;
    }

    /**
     * Helper for {@link Operations_V1#getMultDivGroups}.
     * 1. Selects the Strings Starting from Index i.
     * 2. Then removes the brackets,  which surrounded the strings at first.
     *
     * @param i         index of first bracket
     * @param j         index in the actual calc ArrayList (for later)
     */
    private int selectStrings(ArrayList<String> rest, ArrayList<String[][]> multDiv, int i, int j, int offset) {
        String[][] temp = new String[2][3+offset];

        for (int k = 0; k < (3+offset); k++) {
            temp[0][k] = "" + (j+k);
            temp[1][k] = rest.remove(i);
        }
        rest.remove(i-1); //removes "{"
        rest.remove(i-1); //removes "}"
        multDiv.add(temp);

        return j + 2;
    }

    /**
     * Helper for {@link Operations_V1#evaluate}
     * Decides which operation will be executed.
     *
     * @param num       Number to be added/subtracted to the result or by which the result will
     *                  be multiplied/divided.
     * @param symbol    char deciding which operation will be chosen
     * @param multDiv   boolean deciding to choose result or tempRes as Result of the operation
     */
    private void pickOperation(double num, char symbol, boolean multDiv) {
        switch (symbol) {
            case ' ' -> {
                if (!(multDiv)) {
                    result = num;
                } else {
                    tempRes = num;
                }
            }
            case '*' -> mult(num);
            case '/' -> div(num);
            case '+' -> add(num);
            case '-' -> sub(num);
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
        calc.clear();
        softClear();
    }

    public void add(double addNum) {
        result += addNum;
    }

    public void sub(double subNum) {
        result -= subNum;
    }

    public void mult(double multNum){
        tempRes *= multNum;
    }

    public void div(double divNum){
        try {
            assert divNum != 0;
            tempRes /= divNum;
        } catch (AssertionError e) {
            hardClear();
            System.out.println("You cannot divide by 0!");

        }
    }

    public ArrayList<String> getCalcArr() {
        return calc;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}