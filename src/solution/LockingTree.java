package solution;

import java.util.ArrayList;
import java.util.List;

public class LockingTree {

    public static class Lok {
        int treeId;
        int userId;

        public Lok(int treeId, int userId) {
            this.treeId = treeId;
            this.userId = userId;
        }

        public Lok(Lok lok) {
            this.userId = lok.userId;
            this.treeId = lok.treeId;
        }
    }

    public static class TreeNode {
        public int parent;
        public Lok selfLock;
        public List<Lok> locks = new ArrayList<>();  // 持有子孙的锁

        public TreeNode(int parent) {
            this.parent = parent;
        }
    }

    TreeNode[] tree;

    public LockingTree(int[] parent) {
        int n = parent.length;
        tree = new TreeNode[n];
        for (int i = 0; i < n; i++) {
            TreeNode tn = new TreeNode(parent[i]);
            tree[i] = tn;
        }
    }

    public boolean lock(int num, int user) {
        TreeNode tn = tree[num];
        if (tn.selfLock != null) {
            return false;
        }
        Lok lok = new Lok(num, user);
        tn.selfLock = lok;
        spreadLock(num, lok);
        return true;
    }

    public void spreadLock(int num, Lok lok) {
        TreeNode tn = tree[num];
        while (tn.parent != -1) {
            tn = tree[tn.parent];
            tn.locks.add(new Lok(lok));
        }
    }

    public boolean unlock(int num, int user) {
        TreeNode tn = tree[num];
        if (tn.selfLock == null) {
            return false;
        }
        if (tn.selfLock.userId != user) {
            return false;
        }
        List<Lok> list = new ArrayList<>();
        list.add(new Lok(tn.selfLock));
        spreadUnLock(num, list);
        tn.selfLock = null;
        return true;
    }

    public boolean upgrade(int num, int user) {
        TreeNode tn = tree[num];
        if (tn.selfLock != null) {
            return false;
        }
        if (tn.locks.size() == 0) {
            return false;
        }
        TreeNode tn1 = tree[num];
        while (tn1.parent != -1) {
            tn1 = tree[tn1.parent];
            if (tn1.selfLock != null) {
                return false;
            }
        }
        Lok lok = new Lok(num, user);
        tn.selfLock = lok;
        spreadLock(num, lok);
        spreadUnLock(num, tn.locks);
        // 给子孙节点解锁
        for (Lok lok1 : tn.locks) {
            tree[lok1.treeId].selfLock = null;
            tree[lok1.treeId].locks = new ArrayList<>();
        }
        tn.locks = new ArrayList<>();
        return true;
    }

    public void spreadUnLock(int num, List<Lok> locks) {
        TreeNode tn = tree[num];
        while (tn.parent != -1) {
            tn = tree[tn.parent];
            List<Lok> newLocks = new ArrayList<>();
            for (Lok lok : tn.locks) {
                boolean f = true;
                for (Lok t : locks) {
                    if (lok.treeId == t.treeId) {
                        f = false;
                        break;
                    }
                }
                if (f) {
                    newLocks.add(new Lok(lok));
                }
            }
            tn.locks = newLocks;
        }
    }
}
