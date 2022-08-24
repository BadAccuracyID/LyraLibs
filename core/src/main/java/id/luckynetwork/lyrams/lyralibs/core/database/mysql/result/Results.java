package id.luckynetwork.lyrams.lyralibs.core.database.mysql.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@AllArgsConstructor
@Getter
public abstract class Results implements AutoCloseable {

    private final Connection connection;
    private final Statement statement;
    private final ResultSet resultSet;

    @Override
    public abstract void close();

}
