package id.luckynetwork.lyrams.lyralibs.core.database.mysql;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@AllArgsConstructor
@Getter
public class Results implements AutoCloseable {

    private final Connection connection;
    private final Statement statement;
    private final ResultSet resultSet;

    @Override
    public void close() {
        try {
            resultSet.close();
        } catch (Exception ignored) {
        }
        try {
            connection.close();
        } catch (Exception ignored) {
        }
        try {
            statement.close();
        } catch (Exception ignored) {
        }
    }
}
