package id.luckynetwork.lyrams.lyralibs.core.database.mysql;

import id.luckynetwork.lyrams.lyralibs.core.closer.Closer;
import id.luckynetwork.lyrams.lyralibs.core.database.mysql.result.DefaultResult;
import id.luckynetwork.lyrams.lyralibs.core.database.mysql.result.HikariResult;
import id.luckynetwork.lyrams.lyralibs.core.database.mysql.result.Results;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@AllArgsConstructor
public class Query {

    private final String sqlQuery;
    private final MySQL mySQL;

    /**
     * Execute the SQL query and close the statement.
     */
    public void execute() throws SQLException {
        try (Closer closer = new Closer()) {
            Connection connection = mySQL.isUseHikari() ? closer.add(mySQL.getConnection()) : mySQL.getConnection();
            Statement statement = closer.add(connection.createStatement());

            statement.execute(sqlQuery);
        }
    }

    /**
     * Get a connection, create a statement, execute the query, and return the results.
     *
     * @return A Results object.
     */
    public Results getResults() throws SQLException {
        Connection connection = mySQL.getConnection();
        Statement statement = connection.createStatement();
        ResultSet set = statement.executeQuery(sqlQuery);

        if (mySQL.isUseHikari()) {
            return new HikariResult(connection, statement, set);
        }
        return new DefaultResult(connection, statement, set);
    }
}
