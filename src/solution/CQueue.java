package solution;

import java.util.Stack;

/**
 * 剑指 Offer 09. 用两个栈实现队列
 */
public class CQueue {

    public Stack<Integer> inStack = new Stack<>();
    public Stack<Integer> outStack = new Stack<>();

    public CQueue() {
    }

    public void appendTail(int value) {
        inStack.add(value);
    }

    public int deleteHead() {
        if (!outStack.isEmpty()) {
            return outStack.pop();
        }
        while (!inStack.isEmpty()) {
            outStack.add(inStack.pop());
        }
        if (outStack.isEmpty()) {
            return -1;
        }
        return outStack.pop();
    }
}
