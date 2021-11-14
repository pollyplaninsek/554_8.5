//Polly Planinsek
import java.util.concurrent.locks.*;

class SavingAccount{
    private double balance;
    private final ReentrantLock l;
    private final Condition needMore;

    SavingAccount() {
        balance = 0.00;
        l = new ReentrantLock();
        needMore = l.newCondition();
    }

    SavingAccount(double k) {
        balance = k;
        l = new ReentrantLock();
        needMore = l.newCondition();
    }

    public double getBalance() {
        // added for debugging
        return this.balance;
    }

    // deposit (k)
    public void deposit(double k) {
        System.out.println("calling deposit");
        l.lock();
        try {
            // increment balance
            this.balance += k;
            // wake up waiting threads, more money deposited
            // will crash without synchronize
            synchronized(needMore) {
                needMore.notifyAll();
            }
        }
        finally {
            l.unlock();
        }
    }

    // withdraw (k)
    public void withdraw(double k) {
        System.out.println("calling withdraw");
        l.lock();
        try {
            // block until k is availible
            while(this.balance < k){
                System.out.println("waiting");
                needMore.await();
            }
            // amount is availible
            this.balance -= k;
        }
        catch (InterruptedException e) {
            // await throws exception
            e.printStackTrace();
        }
        finally {
            l.unlock();
        }
    }

    // transfer (k, Account) as shown in 8.5 pg 197 from the text
    public void transfer(double k, SavingAccount other) {
        System.out.println("calling transfer");
        l.lock();
        try {
            // call other saving account withdrawl
            other.withdraw(k);
            // call this classes deposit, the reenterant lock will allow locking twice
            this.deposit(k);
        }
        finally {
            l.unlock();
        }
    }

}