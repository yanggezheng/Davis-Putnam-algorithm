package lab2;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static int numOfJumps;
    static int numOfHoles;
    static int timeForJump;
    static int timeForPeg;
    static int empty;//which hole is empty
    static HashMap<Integer, String> table = new HashMap<>();

    public static String[][] readInputs() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("/Users/yanggezheng/NYU/junior1/AI/projects/src/lab2/input.txt"));//make sure the path to your file is correct
        ArrayList<String> input = new ArrayList<>();
        String line;
        while ((line = in.readLine()) != null) input.add(line);//read file
        String[][] result = new String[input.size()][];
        for (int i = 0; i < input.size(); i++) result[i] = input.get(i).split(" ");//split by space
        return result;
    }

    public static String[] Pegs(String[][] input) {//output all possible pegs at all possible time
        String[] result = new String[Integer.parseInt(input[0][0]) * timeForPeg];
        int count = 0;
        for (int i = 1; i <= Integer.parseInt(input[0][0]); i++) {
            for (int j = 1; j <= timeForPeg; j++) {
                result[count++] = "Peg(" + i + "," + j + ")";
            }
        }
        return result;
    }

    public static String[] Jumps(String[][] input) {//output all possible jumps
        String[] result = new String[numOfJumps];
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 1; i < input.length; i++) {
            for (int j = 1; j < 3; j++) {
                for (int k = 1; k < numOfHoles - 1; k++) {
                    if (j == 1)
                        sb.append("Jump(").append(input[i][0]).append(",").append(input[i][1]).append(",").append(input[i][2]).append(",").append(k).append(")");
                    else
                        sb.append("Jump(").append(input[i][2]).append(",").append(input[i][1]).append(",").append(input[i][0]).append(",").append(k).append(")");
                    result[count++] = sb.toString();
                    sb.setLength(0);
                }
            }
        }
        return result;
    }

    public static String[] addToArray(String[] input, String s) {//add an element to an array
        String[] result = new String[input.length + 1];
        System.arraycopy(input, 0, result, 0, input.length);
        result[input.length] = s;
        return result;
    }

    public static String[] Propositions(int number) {//list all propositions
        String[] result = new String[2 * number + 2 * numOfHoles * timeForJump];
        int count = 1;
        for (int i = 1; i < number + 1; i++) {//Precondition axioms
            String[] temp = table.get(i).split(",");
            int peg1, peg2, peg3;
            peg1 = (temp[0].charAt(temp[0].length() - 1) - 49) * timeForPeg + numOfJumps + temp[3].charAt(0) - 48;
            peg2 = (Integer.parseInt(temp[1]) - 1) * timeForPeg + numOfJumps + temp[3].charAt(0) - 48;
            peg3 = (Integer.parseInt(temp[2]) - 1) * timeForPeg + numOfJumps + temp[3].charAt(0) - 48;
            result[count - 1] = precondition(peg1, peg2, peg3, count++);
        }
        count -= number;
        for (int i = 1; i < number + 1; i++) {//Causal axioms
            String[] temp = table.get(i).split(",");
            int peg1, peg2, peg3;
            peg1 = (temp[0].charAt(temp[0].length() - 1) - 49) * timeForPeg + numOfJumps + temp[3].charAt(0) - 47;
            peg2 = (Integer.parseInt(temp[1]) - 1) * timeForPeg + numOfJumps + temp[3].charAt(0) - 47;
            peg3 = (Integer.parseInt(temp[2]) - 1) * timeForPeg + numOfJumps + temp[3].charAt(0) - 47;
            result[count + number - 1] = causal(peg1, peg2, peg3, count++);
        }
        count = number * 2;
        for (int i = 1; i <= numOfHoles; i++) {//Frame axioms A
            for (int j = 1; j < timeForPeg; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append("-").append(number + j + (i - 1) * timeForPeg).append(" ").append(number + j + (i - 1) * timeForPeg + 1);
                for (int k = 1; k <= number; k++) {
                    String[] temp = table.get(k).split(",");
                    if (temp[3].charAt(0) - 48 == j && (temp[0].charAt(temp[0].length() - 1) - 48 == i || Integer.parseInt(temp[1]) == i))
                        sb.append(" ").append(k);
                }
                result[count++] = sb.toString();
            }
        }//Frame axioms B
        for (int i = 1; i <= numOfHoles; i++) {
            for (int j = 1; j < timeForPeg; j++) {
                StringBuilder sb = new StringBuilder();
                sb.append(number + j + (i - 1) * timeForPeg).append(" ").append("-").append(number + j + (i - 1) * timeForPeg + 1);
                for (int k = 1; k <= number; k++) {
                    String[] temp = table.get(k).split(",");
                    if (Integer.parseInt(temp[2]) == i && temp[3].charAt(0) - 48 == j) sb.append(" ").append(k);
                }
                result[count++] = sb.toString();
            }
        }
        for (int i = 1; i <= number; i++) {//One action at a time A
            for (int j = i + timeForJump; j <= number; j += timeForJump) {
                if (i < j) result = addToArray(result, ("-" + i + " -" + j));
            }
        }

        for (int i = 0; i < numOfHoles; i++) {//starting state
            if (i == empty - 1) result = addToArray(result, "-" + (i * timeForPeg + number + 1));
            else result = addToArray(result, String.valueOf(i * timeForPeg + number + 1));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < numOfHoles; i++) {//ending state
            sb.append((number + i * timeForPeg)).append(" ");
        }
        sb.append(table.size());
        result = addToArray(result, sb.toString());
        for (int i = number + timeForPeg; i <= table.size(); i += timeForPeg) {
            for (int j = i + timeForPeg; j <= table.size(); j += timeForPeg) {
                if (i < j) result = addToArray(result, "-" + i + " -" + j);
            }
        }
        String[] temp1 = new String[6 * number];
        String[] temp2 = new String[result.length - 2 * number];
        int counter = 0;
        for (int i = 0; i < number * 2; i++) {//each precondition and causal axiom is three different statements
            String[] temp = result[i].split(",");
            temp1[counter++] = temp[0];
            temp1[counter++] = temp[1];
            temp1[counter++] = temp[2];
        }
        System.arraycopy(result, number * 2, temp2, 0, temp2.length);
        result = new String[temp2.length + temp1.length];
        System.arraycopy(temp1, 0, result, 0, temp1.length);
        System.arraycopy(temp2, 0, result, temp1.length, temp2.length);
        return result;
    }

    public static String causal(int peg1, int peg2, int peg3, int count) {
        return "-" + count + " -" + peg1 + "," + "-" + count + " -" + peg2 + "," + "-" + count + " " + peg3;
    }

    public static String precondition(int peg1, int peg2, int peg3, int count) {
        return "-" + count + " " + peg1 + "," + "-" + count + " " + peg2 + "," + "-" + count + " -" + peg3;
    }

    public static void update(String[][] input) {//update data after reading data
        numOfHoles = Integer.parseInt(input[0][0]);
        timeForPeg = numOfHoles - 1;
        timeForJump = timeForPeg - 1;
        numOfJumps = 2 * (input.length - 1) * (numOfHoles - 2);
        empty = Integer.parseInt(input[0][1]);
    }
    public static String[][] test1() {
        return new String[][] {{"1","2", "3"}, {"-3","1", "-2"},{"1","-4"},{"3","-1","-2"},
                {"5","6"},{"5", "-6"},
                {"2", "-5"},{"-3", "-5"}};
    }

    public static void main(String[] args) throws IOException {
//        Formula f = new Formula(test1());
//        Result result = DPLL.solve(f);
//        if (result != null) {
//            System.out.println(result);
//        }else {
//            System.out.println("No solution");
//        }
        String[][] input = readInputs();
        update(input);
        String[] arr1 = Jumps(input);
        String[] arr2 = Pegs(input);
        int count = 1;
        int i = 0;
        int index = arr2.length + arr1.length;
        for (; i < arr1.length; i++) table.put(count++, arr1[i]);
        for (; i < index; i++) table.put(count++, arr2[i - arr1.length]);//update table
        String[] output = Propositions(numOfJumps);//get propositions
        String[][] formula = new String[output.length][];//create formula
        for (int j = 0; j < output.length; j++) formula[j] = output[j].split(" ");
        Formula f = new Formula(formula);
        Result result = DPLL.solve(f);//solve
        if (result != null) {//have solution
            System.out.println("Null set of clause. Succeed\nSolution: ");
            System.out.println(table);
            System.out.println(result);//output the result
            int[] choice = result.getResult(numOfJumps, timeForJump);
            for (int j = 1; j < timeForJump; j++)
                for (int value : choice) if (value % timeForJump == j) System.out.println(table.get(value));
            for (int j : choice)
                if (j % timeForJump == 0) System.out.println(table.get(j));//print the jumps sorted by time
        } else {
            System.out.println("No solution");
            System.out.println(table);
        }

    }


}