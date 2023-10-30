package lab2;
import java.util.HashMap;
import java.util.Iterator;

public class Result {//This class is for outputting the result
    private final HashMap<String, Boolean> map;

    public Result(HashMap<String, Boolean> map) {
        this.map = map;
    }

    @Override
    public String toString() {//output the result
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()) {
            String literal = it.next();
            boolean value = map.get(literal);
            sb.append(literal).append("： ");
            sb.append(value ? "1" : "0");
            if(it.hasNext()) sb.append("\n");
        }
        return sb.toString();
    }
    public int[] getResult(int n, int length){// only return literals with value 1
        Iterator<String> it = map.keySet().iterator();
        int [] result = new int[length];
        int count = 0;
        while(it.hasNext()) {
            String literal = it.next();
            if (map.get(literal)&&Integer.parseInt(literal)<=n) result[count++] = Integer.parseInt(literal);
        }
        return result;
    }
}