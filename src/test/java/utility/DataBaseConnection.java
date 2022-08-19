package utility;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;

public final class DataBaseConnection {

    private static DataBaseConnection dataBaseConnection;

    private final String URL;
    private final String USERNAME;
    private final String PASSWORD;

    private DataBaseConnection() {
        URL = ConfProperties.getProperty("postgresql.url");
        USERNAME = ConfProperties.getProperty("postgresql.username");
        PASSWORD = ConfProperties.getProperty("postgresql.password");
    }

    public static DataBaseConnection getInstance() {
        if (dataBaseConnection == null) {
            dataBaseConnection = new DataBaseConnection();
        }
        return dataBaseConnection;
    }

    public void execute(String sqlScriptPath) {
        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD))  {
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader(sqlScriptPath));
            sr.runScript(reader);
        } catch (SQLException | FileNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public RegistrationData createAccByRole(WebDriver driver, int role)    {
        RegistrationData moderator = RegistrationFacade.registerUserAccount(driver);

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD))  {
            String sql = "UPDATE users" +
                    " SET role_id=?" +
                    " WHERE email=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, role);
            preparedStatement.setString(2, moderator.getEmail());
            preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return moderator;
    }

    public void deleteUserByEmail(String email) {
        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD))  {
            String sql = "DELETE FROM users" +
                    " WHERE email=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}



