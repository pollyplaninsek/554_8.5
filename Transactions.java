//Polly Planinsek
import java.math.RoundingMode;
import java.text.DecimalFormat;

class Transactions extends Thread {
    public static void main(String args[]) {
        // formating to print out two decimal places
        final DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.DOWN);

        // inital balances that will be randomly generated in a range min-max
        final double min = 0.00;
        final double max = 500.00;
        final double[] balances = new double[10];
        for (int i=0; i<10; i++)
            balances[i] = (Math.random() * (max - min)) + min;

        // create 10 savings accounts with the random balances
        final SavingAccount[] saAccounts = new SavingAccount[10];
        for (int i= 0; i<10; i++)
            saAccounts[i] = new SavingAccount(balances[i]);

        // just debugging print statments
        for (int i=0; i<10; i++)
            System.out.println("starting \"unknown\" balances of account #" + (i+1) + " : $" + df.format(saAccounts[i].getBalance()));
    
        // create n threads & boss to handle calls based on book problem specifics
        Thread th1 = new Thread() {
            public void run() {
                saAccounts[0].transfer(100, saAccounts[1]);
                System.out.println("returned with a balance of account #" + 1 + " : $" + df.format(saAccounts[0].getBalance()));
            }
        };
        Thread th2 = new Thread() {
            public void run() {
                saAccounts[1].transfer(100, saAccounts[9]);
                System.out.println("returned with a balance of account #" + 2 + " : $" + df.format(saAccounts[1].getBalance()));
            }
        };

        Thread boss = new Thread() {
            public void run() {
                for( SavingAccount s : saAccounts)
                    s.deposit(1000);
                System.out.println("Boss has finished");
            }
        };
        th1.run();
        th2.run();
        try {
            sleep(100);
        }
        catch(InterruptedException e){
            // sleep throws exception
            e.printStackTrace();
        }
        boss.run();
    }
}