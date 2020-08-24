package banking;

import java.sql.*;

class CreditCard {
    private static int id = 1;

    static void createCard(String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS card(" +
                           "id INTEGER, " +
                           "number TEXT, " +
                           "pin TEXT, " +
                           "balance INTEGER DEFAULT 0" +
                           ")"
                );

                String cardNumber = CardGenerator.generateNumber();
                String pin = CardGenerator.generatePin();
                int balance = 0;

                statement.executeUpdate(
                        "INSERT INTO " +
                           "card " +
                           "VALUES " +
                           "(" +
                                id + ", " +
                                "'" + cardNumber + "'" + ", " +
                                "'" + pin + "'" + ", " +
                                balance +
                           ")"
                );
                id++;

                System.out.println("\nYour card has been created");

                System.out.println("Your card number:");
                System.out.println(cardNumber);

                System.out.println("Your card PIN");
                System.out.println(pin);
                System.out.println();

            } catch (SQLException e) {
                System.out.println("Something went wrong with data base updating (inserting new card)");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }
    }

    static boolean validateCard(String cardNumber, String pin, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        boolean answer = false;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet availableCard = statement.executeQuery(
                        "SELECT number, pin " +
                           "FROM card " +
                           "WHERE " +
                           "number = " + "'" + cardNumber + "' " +
                           "AND pin = " + "'" + pin + "' "
                )) {
                    if (availableCard.next()) {
                        answer = true;
                    }
                } catch (SQLException e) {
                    System.out.println("Something went wrong with taking information about validity");
                }
            } catch (SQLException e) {
                System.out.println("Something went wrong with taking information about validity (statement creation)");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }

        return answer;
    }

    static boolean existCard(String cardNumber, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        boolean answer = false;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet availableCard = statement.executeQuery(
                        "SELECT number " +
                                "FROM card " +
                                "WHERE " +
                                "number = " + "'" + cardNumber + "' "
                )) {
                    if (availableCard.next()) {
                        answer = true;
                    }
                } catch (SQLException e) {
                    System.out.println("Something went wrong with taking information about checking existence");
                }
            } catch (SQLException e) {
                System.out.println("Something went wrong with taking information about checking existence (statement creation)");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }

        return answer;
    }

    //---------------------------------------------------------------------------
    //THE FOLLOWING METHODS ARE FOR VALID DATA ONLY
    //BUT THEY ALSO HAVE SOME DEFAULT RETURN VALUES IN CASE OF WRONG INPUT

    static int getBalance(String cardNumber, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        int balance = -1;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                try (ResultSet availableCard = statement.executeQuery(
                        "SELECT balance " +
                                "FROM card " +
                                "WHERE " +
                                "number = " + "'" + cardNumber + "' "
                )) {
                    if (availableCard.next()) {
                        balance = availableCard.getInt("balance");
                    }
                } catch (SQLException e) {
                    System.out.println("Something went wrong with taking balance information (SQL query)");
                }
            } catch (SQLException e) {
                System.out.println("Something went wrong with data base updating (statement creation)");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }

        return balance;
    }

    static void addIncome(String cardNumber, int money, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "UPDATE card " +
                           "SET balance = balance + " + money + " " +
                           "WHERE number = " + "'" + cardNumber + "' "
                );
            } catch (SQLException e) {
                System.out.println("Something went wrong with adding income to card in data base");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }
    }

    static void doTransfer(String cardNumber, int money, String goalCardNumber, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "UPDATE card " +
                                "SET balance = balance - " + money + " " +
                                "WHERE number = " + "'" + cardNumber + "' "
                );

                statement.executeUpdate(
                        "UPDATE card " +
                                "SET balance = balance + " + money + " " +
                                "WHERE number = " + "'" + goalCardNumber + "' "
                );

            } catch (SQLException e) {
                System.out.println("Something went wrong with doing a transfer through data base");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }
    }

    static void closeAccount(String cardNumber, String dbName) {
        String url = "jdbc:sqlite:" + dbName;

        try (Connection con = DriverManager.getConnection(url)) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate(
                        "DELETE FROM card " +
                                "WHERE number = " + "'" + cardNumber + "' "
                );
            } catch (SQLException e) {
                System.out.println("Something went wrong with deleting card from the data base");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong with data base connection");
        }
    }

}
