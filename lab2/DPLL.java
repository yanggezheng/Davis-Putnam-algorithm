package lab2;
import java.util.ArrayList;
import java.util.HashMap;
public class DPLL {//this class is for calculation using DPLL, some pieces of code are from https://github.com/j-christl/DpllSatSolver
    public static Result solve(Formula formula) {
        HashMap<String, Boolean> sentences = new HashMap<>();
        return solve(formula, sentences, 0);
    }
    private static Result solve(Formula formula, HashMap<String, Boolean> variableSetting, int count) {
        if(formula.getClauses().size() == 0) return new Result(variableSetting);
        if(formula.checkEmptyClause()) return null;
        ArrayList<String> literals = formula.getLiterals();
        int category = 0;//we check if it is a one literal or pure literal
        String lit = null;
        for (Clause clause : formula.getClauses()) {
            if (clause.getLiterals().size() == 1) {
                lit = clause.getLiterals().get(0);
                if (lit.startsWith("-")) {
                    lit = lit.replaceFirst("-", "");
                    category = 2;
                } else category = 1;
                break;
            }
        }

        if(category == 0) {//check for pure literal
            plrLoop:for(Clause c : formula.getClauses()) {
                for(String literal : c.getLiterals()) {
                    String opposite = literal.startsWith("-") ? literal.replaceFirst("-","") : "-" + literal;
                    if(!formula.containsLiteral(opposite)) {
                        if(literal.startsWith("-")) {
                            lit = literal.replaceFirst("-","");
                            category = 2;
                        } else {
                            lit = literal;
                            category = 1;
                        }
                        break plrLoop;
                    }
                }
            }
        }

        if(lit == null) lit = literals.get(0);
        if(category == 0 || category == 1) {//skip the true case and set x := false
            Formula newFormula = Formula.copy(formula);
            HashMap<String, Boolean> newSettings = (HashMap<String, Boolean>) variableSetting.clone();
            newSettings.put(lit, true);
            newFormula.set(lit, true);
            Result resultL = solve(newFormula, newSettings, count + 1);
            if (resultL != null) return resultL;
        }
        if(category == 0 || category == 2) {//skip the false case and set x := true
            Formula newFormula = Formula.copy(formula);
            HashMap<String, Boolean> newSettings = (HashMap<String, Boolean>) variableSetting.clone();
            newSettings.put(lit, false);
            newFormula.set(lit, false);
            return solve(newFormula, newSettings, count + 1);
        }
        return null;
    }

}
