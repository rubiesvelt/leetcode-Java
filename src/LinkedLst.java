import beans.ListNode;

public class LinkedLst {

    // 微软面试 1
    // 带环的 相交链表
    public boolean isConnected(ListNode a, ListNode b) {
        ListNode aLoop = getLoop(a);
        ListNode bLoop = getLoop(b);
        // 都不带环
        if (aLoop == null && bLoop == null) {
            return false;  // 按不带环处理
        }
        // 一个有环
        if (aLoop == null || bLoop == null) {
            return false;
        }
        // 两个有环
        ListNode t = aLoop.next;
        while (t != aLoop) {
            if (t == bLoop) {
                return true;
            }
            t = t.next;
        }
        return false;
    }

    public ListNode getLoop(ListNode a) {
        ListNode a1 = a;
        ListNode a2 = a;
        while (a2 != null && a1 != null) {
            a1 = a1.next;
            a2 = a2.next;
            if (a2 == null) {
                return null;
            }
            a2 = a2.next;
            if (a1 == a2) {
                return a1;
            }
        }
        return null;
    }

    // 160. 相交链表
    public ListNode getIntersectionNode(ListNode a, ListNode b) {
        int c1 = 0, c2 = 0;
        ListNode t1 = a, t2 = b;
        while (t1 != null && ++c1 > 0) t1 = t1.next;
        while (t2 != null && ++c2 > 0) t2 = t2.next;
        int t = Math.abs(c1 - c2);
        while (t-- > 0) {
            if (c1 > c2) a = a.next;
            else b = b.next;
        }
        while (a != null && b != null) {
            if (a.equals(b)) {
                return a;
            } else {
                a = a.next;
                b = b.next;
            }
        }
        return null;
    }

    // 92
    public static ListNode reverseBetween(ListNode head, int m, int n) {
        int num = 1;
        ListNode current = head;
        ListNode r0 = new ListNode(head.val, null);
        ListNode r1 = new ListNode(head.val, null);  // 开始反转头部
        ListNode r2 = new ListNode(head.val, null);
        ListNode r3 = new ListNode(head.val, null);
        boolean reverse = false;
        if (head.next == null) {
            return head;
        }
        while (head.next != null) {
            if (reverse == true) {
                // r3 = now, r3.next = r2
                r3 = new ListNode(current.val, r2);
                // r2 = r3
                r2 = new ListNode(r3.val, r3.next);
            }
            if (m == 1) {
                r0 = new ListNode(current.val, current);
            } else if (num == m - 1) {
                r0 = current;
            }
            if (num == m) {
                reverse = true;
                r1 = new ListNode(current.val, null);
                r2 = r1;
                r3 = r1;
            }
            if (num == n) {
                // r3 --> ... --> r1
                // r1.next = current.next
                r1.next = current.next;
                // r0.next = r1
                r0.next = r3;
                break;
            }
            current = current.next;
            num++;
        }
        if (m == 1) {
            return r0.next;
        }
        return head;
    }

    // 61. 旋转链表
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null) {
            return null;
        }
        if (k == 0) {
            return head;
        }
        int len = 1;
        ListNode last = head;
        while (last.next != null) {
            last = last.next;
            len++;
        }
        if (len == 1) {
            return head;
        }
        if (k % len == 0) {
            return head;
        }
        int n = len - k % len;
        ListNode node = head;  // 此处，如果不涉及修改head的操作，可以直接赋值
        while (n > 1) {
            node = node.next;
            n--;
        }
        ListNode res = node.next;
        node.next = null;
        last.next = head;
        return res;
    }
}
