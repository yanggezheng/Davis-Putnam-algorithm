package lab2;

import java.util.ArrayList;

public class Clause {
    private final ArrayList<String> literals;
    public Clause(ArrayList<String> literals) {
        this.literals = literals;
    }

    public boolean containsLiterals() {
        return literals.size() != 0;
    }

    @Override
    public boolean equals(Object obj) {//method for comparing two clauses
        if (obj instanceof Clause other) {
            if (other.literals.size() == literals.size()) {
                if (literals.size() == 0) return true;
                for (String lit : literals) {
                    if (!other.containsLiteral(lit)) return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {//override toString method
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (String literal : getLiterals()) {
            sb.append(literal);
            if (getLiterals().indexOf(literal) < getLiterals().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public boolean containsLiteral(String literal) {
        return literals.contains(literal);
    }
    public void removeLiteral(String literal) {
        literals.remove(literal);
    }
    public ArrayList<String> getLiterals() {
        return literals;
    } 
}