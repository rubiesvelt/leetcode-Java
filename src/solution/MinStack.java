package solution;

import java.util.Stack;

/**
 * 剑指 Offer 30. 包含min函数的栈
 */
class MinStack {

    public Stack<Element> stack = new Stack<>();

    int min = 0x3f3f3f3f;

    /**
     * initialize your data structure here.
     */
    public MinStack() {
    }

    public void push(int x) {
        stack.add(new Element(x, min));
        if (min == 0x3f3f3f3f) {
            min = x;
        } else {
            min = Math.min(x, min);
        }
    }

    public void pop() {
        if (stack.isEmpty()) {
            return;
        }
        Element element = stack.pop();
        min = element.pastMin;
    }

    public int top() {
        if (stack.isEmpty()) {
            return -1;
        }
        return stack.peek().val;
    }

    public int min() {
        if (min == 0x3f3f3f3f) {
            return -1;
        }
        return min;
    }

    public static class Element {
        int val;
        int pastMin;

        public Element(int val, int pastMin) {
            this.val = val;
            this.pastMin = pastMin;
        }
    }
}
