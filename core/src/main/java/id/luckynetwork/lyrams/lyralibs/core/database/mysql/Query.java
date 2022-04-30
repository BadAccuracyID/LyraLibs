package id.luckynetwork.lyrams.lyralibs.core.database.mysql;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class Query {

    private final String sqlQuery;
    private final MySQL mySQL;

    public void execute() {
        Statement statement = null;
        try {
            Connection connection = mySQL.getConnection();

            statement = connection.createStatement();
            statement.execute(sqlQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Results getResults() throws SQLException {
        Connection connection = mySQL.getConnection();
        Statement statement = connection.createStatement();

        ResultSet set = statement.executeQuery(sqlQuery);
        return new Results(connection, statement, set);
    }
}
