package solution;

public class Bank {

    long[] all;

    int n;

    public Bank(long[] balance) {
        all = balance;
        n = balance.length;
    }

    public boolean transfer(int account1, int account2, long money) {
        account1--;
        account2--;
        if (account1 >= n || account2 >= n || account1 < 0 || account2 < 0) {
            return false;
        }
        if (all[account1] < money) {
            return false;
        }
        all[account1] -= money;
        all[account2] += money;
        return true;
    }

    public boolean deposit(int account, long money) {
        account--;
        if (account >= n || account < 0) {
            return false;
        }
        all[account] += money;
        return true;
    }

    public boolean withdraw(int account, long money) {
        account--;
        if (account >= n || account < 0) {
            return false;
        }
        if (all[account] < money) {
            return false;
        }
        all[account] -= money;
        return true;
    }
}
