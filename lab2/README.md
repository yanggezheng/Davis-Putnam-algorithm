The input file is in the folder, you can either paste the test cases to it or change
the path (line 17 in Main.java)(Sorry for hardcoding the path of the file)
For the input, the first line should only contain 2 numbers and a space between them
(EX: 4 1).
The first number is the number of holes and the second number are the hole with no peg
at time 1
the rest lines should contain 3 numbers and 2 spaces between them
(EX: 1 2 3)
It means hole 1, hole 2, and hole 3 are a triple. Each number is a valid hole number
please only enter integers!
The Main.c is the file to be compiled and ran.
If you would like to test the DPLL algorithm, an example is attached below.

Add this function to the main.c
    public static String[][] test1() {
        return new String[][] {{"b","-d"}, {"d","-c"},{"f","-d"},{"f","a","c","-b"},
        {"-f","-a","-b","-d"},{"f","c","-a","-b","-d"},
        {"a","c","d","-f"},{"f","b","-a","-d"},{"f","-a","-b","-c","-d"}};
    }
this means
    (b ∨ ¬d) ∧ (d ∨ ¬c) ∧ (f ∨ ¬d) ∧ (f ∨ a ∨ c ∨ ¬b) ∧
    (¬f ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (f ∨ c ∨ ¬a ∨ ¬b ∨ ¬d) ∧ (a ∨ c ∨ d ∨ ¬f) ∧
    (f ∨ b ∨ ¬a ∨ ¬d) ∧ (f ∨ ¬a ∨ ¬b ∨ ¬c ∨ ¬d)

And in the main function

    Formula f = new Formula(test1);
    Result result = DPLL.solve(f);
    if (result != null) {
    System.out.println(result);
    }else {
    System.out.println("No solution");
    }