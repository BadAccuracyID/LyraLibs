package id.luckynetwork.lyrams.lyralibs.core.database.mysql;

import id.luckynetwork.lyrams.lyralibs.core.closer.Closer;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@AllArgsConstructor
public class Query {

    private final String sqlQuery;
    private final MySQL mySQL;

    /**
     * Execute the SQL query and close the statement.
     */
    public void execute() throws SQLException {
        try (Closer closer = new Closer()) {
            Connection connection = mySQL.isUseHikari() ? closer.add(Objects.requireNonNull(mySQL.getHikariDataSource()).getConnection()) : closer.add(mySQL.getConnection());
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
        return new Results(connection, statement, set);
    }
}
