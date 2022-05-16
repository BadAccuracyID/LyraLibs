package id.luckynetwork.lyrams.lyralibs.core.database.mysql;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@RequiredArgsConstructor
public class MySQL {

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private final boolean useSSL;

    @Getter
    private Connection connection;

    /**
     * It connects to the database
     */
    public void connect() throws SQLException {
        if (this.isConnected()) {
            return;
        }

        String url = this.formatUrl(this.host, this.port, this.database, this.useSSL);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }

        Properties config = new Properties();
        config.setProperty("user", username);
        config.setProperty("password", password);

        config.setProperty("useServerPrepStmts", "false");
        config.setProperty("cachePrepStmts", "true");
        config.setProperty("prepStmtCacheSize", "250");
        config.setProperty("prepStmtCacheSqlLimit", "2048");
        config.setProperty("autoReconnect", "true");

        this.connection = DriverManager.getConnection(url, config);
    }

    /**
     * If the connection is open, close it.
     */
    public void disconnect() throws SQLException {
        if (this.isConnected()) {
            connection.close();
        }
    }

    /**
     * If the connection is not null, and it's not closed, and it's valid, then it's connected
     *
     * @return A boolean value.
     */
    public boolean isConnected() {
        boolean connected = false;

        try {
            connected = this.connection != null && !this.connection.isClosed() && this.connection.isValid(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connected;
    }

    /**
     * "Execute the given SQL query, and call the given callback function with the results."
     *
     * The first line of the function creates a new Query object, passing in the SQL query and the callback function. The
     * second line calls the execute() function on the Query object
     *
     * @param sql The SQL query to execute.
     */
    public void executeQuery(String sql) {
        new Query(sql, this).execute();
    }

    /**
     * "Execute the given SQL statement and return the results."
     *
     * The first thing we do is create a new Query object. We pass in the SQL statement and the database connection
     *
     * @param sql The SQL query to execute.
     * @return A new Query object is being returned.
     */
    public Results results(String sql) throws SQLException {
        return new Query(sql, this).getResults();
    }

    private String formatUrl(String host, String port, String database, boolean useSSL) {
        if (database == null)
            database = "";
        if (host == null)
            host = "";
        if (port == null)
            port = "";

        if (!database.isEmpty() && !database.startsWith("/")) {
            database = "/" + database;
        }

        if (useSSL) {
            return "jdbc:mysql://" + host + ":" + port + database + "?useSSL=true";
        } else {
            return "jdbc:mysql://" + host + ":" + port + database + "?useSSL=false";
        }
    }

}
