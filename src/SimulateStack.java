import java.util.Stack;

public class SimulateStack {

    /*
     * 括号问题 —— when you are confused, use stack
     * Balanced Brackets
     */
    public static String isBalanced(String s) {
        Stack<Character> st = new Stack<>();
        char[] cs = s.toCharArray();
        for (char val : cs) {
            if (val == '(' || val == '[' || val == '{') {
                st.push(val);
            } else if (st.isEmpty()) {
                return "NO";
            } else if (val == ')') {
                char c = st.pop();
                if (c != '(') return "NO";
            } else if (val == ']') {
                char c = st.pop();
                if (c != '[') return "NO";
            } else if (val == '}') {
                char c = st.pop();
                if (c != '{') return "NO";
            }
        }
        if (!st.isEmpty()) return "NO";
        return "YES";
    }
}
