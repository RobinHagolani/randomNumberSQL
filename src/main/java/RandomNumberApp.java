import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

public class RandomNumberApp {
    // Replace these with your MySQL database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/random_numbers_db";
    private static final String USER = "root";
    private static final String PASSWORD = "1q2w3e4r";

    public static void main(String[] args) {
        try {
            // Connect to the database
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);

            // Generate a random number between 1 and 1000
            int randomNumber = generateRandomNumber();

            // Insert the random number into the database
            insertRandomNumber(connection, randomNumber);

            // Print all the previously generated numbers and count how many times the app was used
            printNumbersAndUsage(connection);

            // Close the connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(1000) + 1;
    }

    private static void insertRandomNumber(Connection connection, int randomNumber) throws Exception {
        String insertSQL = "INSERT INTO numbers (random_number) VALUES (?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
        preparedStatement.setInt(1, randomNumber);
        preparedStatement.executeUpdate();
        System.out.println("Random number " + randomNumber + " inserted into the database.");
    }

    private static void printNumbersAndUsage(Connection connection) throws Exception {
        String selectSQL = "SELECT random_number, created_at FROM numbers ORDER BY created_at DESC";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectSQL);

        System.out.println("\nPreviously generated numbers:");
        while (resultSet.next()) {
            int number = resultSet.getInt("random_number");
            String timestamp = resultSet.getString("created_at");
            System.out.println("Number: " + number + " | Generated at: " + timestamp);
        }

        String countSQL = "SELECT COUNT(*) AS usage_count FROM numbers";
        ResultSet countResultSet = statement.executeQuery(countSQL);
        if (countResultSet.next()) {
            int usageCount = countResultSet.getInt("usage_count");
            System.out.println("\nThe app has been used " + usageCount + " times.");
        }
    }
}
