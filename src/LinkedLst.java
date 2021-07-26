import beans.ListNode;

public class LinkedLst {
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
