package id.luckynetwork.lyrams.lyralibs.core.database.mysql.result;

import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Getter
public class DefaultResult extends Results {

    private final Connection connection;
    private final Statement statement;
    private final ResultSet resultSet;

    public DefaultResult(Connection connection, Statement statement, ResultSet resultSet) {
        super(connection, statement, resultSet);
        this.connection = connection;
        this.statement = statement;
        this.resultSet = resultSet;
    }

    @Override
    public void close() {
        try {
            statement.close();
        } catch (Exception ignored) {
        }
        try {
            resultSet.close();
        } catch (Exception ignored) {
        }
    }
}
