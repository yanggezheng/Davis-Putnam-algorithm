package lab2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
public class Formula {
    private final ArrayList<Clause> clauses;
    public Formula(String[][] formula) {//constructor
        ArrayList<Clause> clauses = new ArrayList<>();
        for (String[] clause : formula) clauses.add(new Clause(new ArrayList<>(Arrays.asList(clause))));
        this.clauses = clauses;
    }
    public boolean containsLiteral(String literal) {//check whether a formula contains a literal
        for(Clause clause : clauses) if(clause.containsLiteral(literal)) return true;
        return false;
    }
    public static Formula copy(Formula f) {//make a copy
        ArrayList<Clause> newClauses = new ArrayList<>();
        for(Clause c : f.getClauses()) {
            ArrayList<String> literals = new ArrayList<>(c.getLiterals());
            Clause cNew = new Clause(literals);
            newClauses.add(cNew);
        }
        return new Formula(newClauses);
    }

    public boolean checkEmptyClause() {// check if it only an empty clause
        for(Clause clause : clauses) if(clause.containsLiterals()) return false;
        return true;
    }

    public void set(String literal, boolean value) {
        if(value) {//true
            removeClauses(literal);
            removeLiteral("-" + literal);
        } else {//false
            removeClauses("-" + literal);
            removeLiteral(literal);
        }
        removeDuplicates();
    }

    private void removeDuplicates() {
        ArrayList<Clause> set = new ArrayList<>();
        for(Iterator<Clause> iterator = clauses.iterator(); iterator.hasNext();) {
            Clause c = iterator.next();
            if(set.contains(c)) {
                iterator.remove();
            }
            set.add(c);
        }
    }
    public void removeLiteral(String literal) {//remove a literal in every clause containing it
        for(Clause clause : clauses) if(clause.containsLiteral(literal)) clause.removeLiteral(literal);
    }
    public void removeClauses(String literal) {
        clauses.removeIf(clause -> clause.containsLiteral(literal));
    }
    public ArrayList<String> getLiterals() {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        for(Clause c : clauses) for(String lit : c.getLiterals()) set.add(lit.startsWith("-") ? lit.replaceFirst("-","") : lit);
        ArrayList<String> literals = new ArrayList<>(set);
        Collections.sort(literals);
        return literals;
    }
    public Formula(ArrayList<Clause> clauses) {
        this.clauses = clauses;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");
        for(Clause clause : clauses) {
            sb.append(clause.toString());
            if(clauses.indexOf(clause) < clauses.size() - 1) sb.append(", ");//check if we should add ,
        }
        sb.append(" }");//close parentheses
        return sb.toString();
    }
    public ArrayList<Clause> getClauses() {
        return clauses;
    }
}