import java.util.ArrayList;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankAccount {
    String name;
    String username;
    String password;
    String accNumber;
    int balance = 0;
    ArrayList<String> history = new ArrayList<>();

    synchronized void updateBalance(int amount) {
        balance += amount;
    }

    void showBalance() {
        System.out.println("Balance: " + balance);
    }
}

class Transaction {
    static synchronized void withdraw(BankAccount account, int amount) {
        if (amount <= account.balance) {
            account.balance -= amount;
            account.history.add(getTimestamp() + " -" + amount + " Withdrawal");
            System.out.println("Amount Rs." + amount + "/- withdrawn successfully");
        } else {
            System.out.println("Insufficient balance to withdraw the cash");
        }
        account.showBalance();
    }

    static synchronized void deposit(BankAccount account, int amount) {
        account.updateBalance(amount);
        account.history.add(getTimestamp() + " +" + amount + " Deposit");
        System.out.println("Amount Rs." + amount + "/- deposited successfully");
        account.showBalance();
    }

    static synchronized void transfer(BankAccount sender, BankAccount receiver, int amount) {
        if (amount <= sender.balance) {
            sender.balance -= amount;
            receiver.balance += amount;
            sender.history.add(getTimestamp() + " -" + amount + " Transfer to " + receiver.name);
            receiver.history.add(getTimestamp() + " +" + amount + " Transfer from " + sender.name);
            System.out.println("Amount Rs." + amount + "/- transferred successfully");
        } else {
            System.out.println("Insufficient balance to transfer the cash");
        }
        sender.showBalance();
        receiver.showBalance();
    }

    private static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}

class Check {
    static synchronized void checkBalance(BankAccount account) {
        account.showBalance();
    }
}

class History {
    static synchronized void transactionHistory(BankAccount account) {
        System.out.println("Transaction History:");
        for (String transaction : account.history) {
            System.out.println(transaction);
        }
    }
}

class Login {
    static BankAccount authenticate(String username, String password, ArrayList<BankAccount> accounts) {
        for (BankAccount account : accounts) {
            if (account.username.equals(username) && account.password.equals(password)) {
                return account;
            }
        }
        return null;
    }
}

public class ATMPROJ extends JFrame {
    private static final long serialVersionUID = 1L;
    private static ArrayList<BankAccount> accounts = new ArrayList<>();
    private BankAccount account; // Currently logged-in account

    public ATMPROJ() {
        setTitle("Banking System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton withdrawButton = new JButton("Withdraw");
        JButton depositButton = new JButton("Deposit");
        JButton transferButton = new JButton("Transfer");
        JButton checkBalanceButton = new JButton("Check Balance");
        JButton transactionHistoryButton = new JButton("Transaction History");
        JButton exitButton = new JButton("Exit");
        JButton loginButton = new JButton("Login");
        JButton switchAccountButton = new JButton("Switch Account");

        JPanel panel = new JPanel();
        panel.add(withdrawButton);
        panel.add(depositButton);
        panel.add(transferButton);
        panel.add(checkBalanceButton);
        panel.add(transactionHistoryButton);
        panel.add(exitButton);
        panel.add(loginButton);
        panel.add(switchAccountButton);

        setContentPane(panel);
        setLocationRelativeTo(null);

        BankAccount account1 = new BankAccount();
        account1.name = "Omkar Bohidar";
        account1.username = "omkar";
        account1.password = "password";
        account1.accNumber = "123456";

        BankAccount account2 = new BankAccount();
        account2.name = "Amit Prasad";
        account2.username = "amit";
        account2.password = "password";
        account2.accNumber = "123457";

        BankAccount account3 = new BankAccount();
        account3.name = "Durga Prasanna";
        account3.username = "durga";
        account3.password = "password";
        account3.accNumber = "123458";

        BankAccount account4 = new BankAccount();
        account3.name = "Soumya Ranjan";
        account3.username = "soumya";
        account3.password = "password";
        account3.accNumber = "123459";

        BankAccount account5 = new BankAccount();
        account4.name = "Sudesh Moharana";
        account4.username = "sudesh";
        account4.password = "password";
        account4.accNumber = "123460";

        BankAccount account6 = new BankAccount();
        account5.name = "CN";
        account5.username = "CN";
        account5.password = "password";
        account5.accNumber = "123461";

        accounts.add(account1);
        accounts.add(account2);
        accounts.add(account3);
        accounts.add(account4);
        accounts.add(account5);
        accounts.add(account6);


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter your username:");
                String password = JOptionPane.showInputDialog("Enter your password:");

                BankAccount loggedInAccount = Login.authenticate(username, password, accounts);

                if (loggedInAccount != null) {
                    account = loggedInAccount;
                    System.out.println("Login successful. Welcome, " + account.name + "!");
                } else {
                    System.out.println("Invalid username or password. Login failed.");
                }
            }
        });

        switchAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (account != null) {
                    String newUsername = JOptionPane.showInputDialog("Enter new username:");
                    String newPassword = JOptionPane.showInputDialog("Enter new password:");

                    BankAccount newAccount = Login.authenticate(newUsername, newPassword, accounts);

                    if (newAccount != null) {
                        account = newAccount;
                        System.out.println("Switched to another account. Welcome, " + account.name + "!");
                    } else {
                        System.out.println("Invalid username or password. Account switch failed.");
                    }
                } else {
                    System.out.println("Please log in first.");
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to withdraw:"));
                Transaction.withdraw(account, amount);
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to deposit:"));
                Transaction.deposit(account, amount);
            }
        });

        transferButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String receiverAccNumber = JOptionPane.showInputDialog("Enter the receiver's account number:");
                BankAccount receiver = getAccount(receiverAccNumber);
                if (receiver == null) {
                    System.out.println("Receiver account not found!");
                    return;
                }
                int amount = Integer.parseInt(JOptionPane.showInputDialog("Enter the amount to transfer:"));
                Transaction.transfer(account, receiver, amount);
            }
        });

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Check.checkBalance(account);
            }
        });

        transactionHistoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                History.transactionHistory(account);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private static BankAccount getAccount(String accNumber) {
        for (BankAccount account : accounts) {
            if (account.accNumber.equals(accNumber)) {
                return account;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ATMPROJ().setVisible(true);
            }
        });
    }
}