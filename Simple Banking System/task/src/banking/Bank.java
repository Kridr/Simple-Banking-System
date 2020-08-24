package banking;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class Bank {
    private final static String mainMenu =
            "1. Create an account\n" +
            "2. Log into account\n" +
            "0. Exit";

    private final static String cardMenu =
            "1. Balance\n" +
            "2. Add income\n" +
            "3. Do transfer\n" +
            "4. Close account\n" +
            "5. Log out\n" +
            "0. Exit";

    private final static String byeMessage = "\nBye!";

    private final static Scanner scanner = new Scanner(System.in);

    private static String dbName;

    //-----------------------------------------------------

    private BankStatesOfWork state = BankStatesOfWork.MAIN_MENU;
    private String currentCardNumber = "";

    public Bank(String[] args) {
        dbName = args[1];
    }

    public void process() {
        while (singleProcess()) {}
    }

    private boolean singleProcess() {
        switch (state) {
            case MAIN_MENU:
                System.out.println(mainMenu);
                mainMenuInterface();
                return true;
            case CARD_MENU:
                System.out.println(cardMenu);
                cardMenuInterface();
                return true;
            case EXIT:
                System.out.println(byeMessage);
                return false;
        }
        return false;
    }

    private void mainMenuInterface() {
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                createCard();
                break;
            case 2:
                logIn();
                break;
            case 0:
                generalExit();
                break;
        }
    }

    private void createCard() {
        CreditCard.createCard(dbName);
    }

    private void logIn() {
        System.out.println("\nEnter your card number:");
        String cardNumber = scanner.next();
        System.out.println("Enter your PIN:");
        String pin = scanner.next();

        if (CreditCard.validateCard(cardNumber, pin, dbName)) {
            System.out.println("\nYou have successfully logged in!\n");
            state = BankStatesOfWork.CARD_MENU;
            currentCardNumber = cardNumber;
        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void cardMenuInterface() {
        int option = scanner.nextInt();

        switch (option) {
            case 1:
                checkBalance();
                break;
            case 2:
                addIncome();
                break;
            case 3:
                doTransfer();
                break;
            case 4:
                closeAccount();
                break;
            case 5:
                logOut();
                break;
            case 0:
                generalExit();
                break;
        }
    }

    private void checkBalance() {
        System.out.format("\nBalance: %d\n\n", CreditCard.getBalance(currentCardNumber, dbName));
    }

    private void addIncome() {
        System.out.println("\nEnter income:");
        int money = scanner.nextInt();

        CreditCard.addIncome(currentCardNumber, money, dbName);

        System.out.println("Income was added!\n");
    }

    private void doTransfer() {
        System.out.println("\nTransfer");

        System.out.println("Enter card number:");
        String cardNumber = scanner.next();

        if (Objects.equals(cardNumber, currentCardNumber)) {

            System.out.println("You can't transfer money to the same account!\n");

        } else if (!cardNumber.matches("\\d".repeat(16)) ||
                CardGenerator.luhn(cardNumber.substring(0, cardNumber.length() - 1)) !=
                        Character.getNumericValue(cardNumber.charAt(cardNumber.length() - 1))) {

            System.out.println("Probably you made mistake in the card number. Please try again!\n");

        } else if (!CreditCard.existCard(cardNumber, dbName)){

            System.out.println("Such a card does not exist.\n");

        } else {
            System.out.println("Enter how much money you want to transfer:");
            int money = scanner.nextInt();

            if (money > CreditCard.getBalance(currentCardNumber, dbName)) {
                System.out.println("Not enough money!\n");
            } else {
                CreditCard.doTransfer(currentCardNumber, money, cardNumber, dbName);
                System.out.println("Success!\n");
            }

        }
    }

    private void closeAccount() {
        CreditCard.closeAccount(currentCardNumber, dbName);

        currentCardNumber = "";

        System.out.println("\nThe account has been closed!\n");

        state = BankStatesOfWork.MAIN_MENU;
    }

    private void logOut() {
        state = BankStatesOfWork.MAIN_MENU;
        currentCardNumber = "";

        System.out.println("\nYou have successfully logged out!\n");
    }

    private void generalExit() {
        state = BankStatesOfWork.EXIT;
    }
}
