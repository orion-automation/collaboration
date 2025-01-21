package com.eorion.bo.enhancement.collaboration.utils;

import lombok.AllArgsConstructor;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

@AllArgsConstructor
public class BatchSQLExecutor {
    private final DataSource dataSource;

    public void batchExecuteSqlFromFile(InputStreamReader reader) throws SQLException {
        try(var connection = dataSource.getConnection()) {
            var runner = new ScriptRunner(connection);
            runner.setLogWriter(new PrintWriter(System.out, true));
            runner.setErrorLogWriter(new PrintWriter(System.err, true));
            runner.setSendFullScript(true);
            runner.setAutoCommit(true);
            runner.runScript(reader);
        }
    }
}