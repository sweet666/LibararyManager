package by.pvt.safronenko.library.tools.datasource;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data source that works with MySQL with internal connection pool.
 */
public class DataSource {

    /**
     * Logger.
     */
    private static final Logger logger = Logger.getLogger(DataSource.class);

    /**
     * Connection pool.
     */
    private final BoneCP connectionPool;

    /**
     * Constructs entity manager with given user and password.
     *
     * @param user     User.
     * @param password Password.
     */
    public DataSource(String jdbcDriver, String jdbcUrl, String user, String password) {
        try {
            // Load the driver.
            Class.forName(jdbcDriver);

            // Set up configuration.
            BoneCPConfig config = new BoneCPConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(user);
            config.setPassword(password);

            // Create connection pool config.
            this.connectionPool = new BoneCP(config);
        } catch (Exception e) {
            logger.error("Error while setting up entity manager.", e);
            throw new RuntimeException(jdbcDriver + " initialization error.", e);
        }
    }

    /**
     * Returns all records from given table ordered by given column.
     *
     * @param table   Table to select from.
     * @param orderBy Column to order by.
     * @return Table records as a list of data sets.
     * @throws SQLException In case of error.
     */
    public List<Map<String, Object>> getAll(String table, String orderBy) throws SQLException {
        String preparedStatementSQL = String.format("SELECT * FROM %s ORDER BY %s", table, orderBy);
        Connection connection = null;
        // Explicit connection lease / release.
        try {
            connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementSQL);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                return resultSetToArrayList(resultSet);
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    /**
     * Returns all records from given SQL query, as prepared statement.
     *
     * @param preparedStatementSQL Prepared statement as a string.
     * @param parameters           Parameters to prepared statement.
     * @return Table records as a list of data sets.
     * @throws SQLException In case of error.
     */
    public List<Map<String, Object>> getAllWithSql(String preparedStatementSQL, Object... parameters)
            throws SQLException {
        // Implicit connection lease / release with try-with-resources.
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementSQL);
            for (int i = 1; i <= parameters.length; i++) {
                preparedStatement.setObject(i, parameters[i - 1]);
            }
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                return resultSetToArrayList(resultSet);
            }
        } // try with resources will call #close() on connection.
    }

    /**
     * Returns one record by given condition.
     *
     * @param table           Table.
     * @param condition       Condition string.
     * @param conditionValues Condition values.
     * @return One record or null if there is no record.
     * @throws SQLException In case of error.
     */
    public Map<String, Object> getWithCondition(String table, String condition, Object... conditionValues) throws SQLException {
        String preparedStatementSQL = String.format("SELECT * FROM %s WHERE %s", table, condition);
        List<Map<String, Object>> records = getAllWithSql(preparedStatementSQL, conditionValues);
        if (records.isEmpty()) {
            return null;
        }
        return records.get(0);
    }

    /**
     * Returns single record from table by given id. Uses "id" field as a primary key.
     *
     * @param table           Table to get records from.
     * @param primaryKeyValue Primary key value.
     * @return Table record a data set or null when there is no record by given id.
     * @throws SQLException In case of error.
     */
    public Map<String, Object> get(String table, Object primaryKeyValue) throws SQLException {
        return get(table, "id", primaryKeyValue);
    }

    /**
     * Returns single record from table by given id. Uses "id" field as a primary key.
     *
     * @param table           Table to get records from.
     * @param primaryKeyValue Primary key value.
     * @return Table record a data set or null when there is no record by given id.
     * @throws SQLException In case of error.
     */
    public Map<String, Object> get(String table, String idName, Object primaryKeyValue) throws SQLException {
        String preparedStatementSQL = String.format("SELECT * FROM %s WHERE %s = ?", table, idName);
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementSQL);
            preparedStatement.setObject(1, primaryKeyValue);
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getResultSet()) {
                List<Map<String, Object>> data = resultSetToArrayList(resultSet);
                if (data.isEmpty()) {
                    return null;
                }
                return data.get(0);
            }
        }
    }

    /**
     * Inserts a record into the given table.
     *
     * @param table Table.
     * @param data  Data to insert (excluding auto-generated keys).
     * @return Auto-generated key if it exists.
     * @throws SQLException In Case of error.
     */
    public Long insert(String table, Map<String, Object> data) throws SQLException {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException();
        }

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ")
                .append(table)
                .append(" ( ");

        StringBuilder conditionBuilder = new StringBuilder(" VALUES ( ");
        for (Map.Entry<String, Object> e : data.entrySet()) {
            sqlBuilder.append(e.getKey())
                    .append(", ");
            conditionBuilder.append(" ?, ");
        }

        // Omit the last comma.
        sqlBuilder.setLength(sqlBuilder.length() - 2);
        conditionBuilder.setLength(conditionBuilder.length() - 2);

        sqlBuilder.append(")");
        conditionBuilder.append(")");

        sqlBuilder.append(conditionBuilder);

        try (Connection connection = connectionPool.getConnection()) {
            // Update happens in transaction.
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString(),
                    Statement.RETURN_GENERATED_KEYS);

            // Set parameters for prepared statement.
            int counter = 1;
            for (Map.Entry<String, Object> e : data.entrySet()) {
                preparedStatement.setObject(counter, e.getValue());
                counter++;
            }

            preparedStatement.executeUpdate();
            connection.commit();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                // Return auto-generated key if present, numeric type.
                return (Long) rs.getObject(1);
            }
            return null;
        }
    }

    /**
     * Updates a record by "id" primary key.
     *
     * @param table Table to update.
     * @param data  Data, including a primary key.
     * @return Whether the record was updated.
     * @throws SQLException In case of error.
     */
    public boolean update(String table, Map<String, Object> data) throws SQLException {
        return update(table, "id", data);
    }

    /**
     * Updates a record by primary key.
     *
     * @param table          Table to update.
     * @param primaryKeyName PK name.
     * @param data           Data, including a primary key.
     * @return Whether the record was updated.
     * @throws SQLException In case of error.
     */
    public boolean update(String table, String primaryKeyName, Map<String, Object> data) throws SQLException {
        if (data == null || data.size() < 2 || data.get(primaryKeyName) == null) {
            throw new IllegalArgumentException("Data requires at least two entries with key: " + primaryKeyName);
        }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE ")
                .append(table)
                .append(" SET ");
        for (Map.Entry<String, Object> e : data.entrySet()) {
            if (!primaryKeyName.equals(e.getKey())) {
                sqlBuilder.append(e.getKey())
                        .append(" = ?, ");
            }
        }

        // Omit the last comma.
        sqlBuilder.setLength(sqlBuilder.length() - 2);

        sqlBuilder.append(" WHERE ").
                append(primaryKeyName).
                append(" = ?");

        Object pk = data.get(primaryKeyName);

        try (Connection connection = connectionPool.getConnection()) {
            // Update happens in transaction.
            connection.setAutoCommit(false);

            // PreparedStatement uses builder pattern internally.
            PreparedStatement preparedStatement = connection.prepareStatement(sqlBuilder.toString());

            // Set parameters for prepared statement.
            int counter = 1;
            for (Map.Entry<String, Object> e : data.entrySet()) {
                if (!primaryKeyName.equals(e.getKey())) {
                    preparedStatement.setObject(counter, e.getValue());
                    counter++;
                }
            }

            preparedStatement.setObject(data.size(), pk);
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        }
    }

    /**
     * Removes a record from table.
     *
     * @param table           Table to update.
     * @param primaryKeyValue PK value.
     * @return Whether the record was updated.
     * @throws SQLException In case of error.
     */
    public boolean remove(String table, Object primaryKeyValue) throws SQLException {
        return remove(table, "id", primaryKeyValue);
    }

    /**
     * Removes a record from table.
     *
     * @param table           Table to update.
     * @param primaryKeyName  PK name.
     * @param primaryKeyValue PK value.
     * @return Whether the record was updated.
     * @throws SQLException In case of error.
     */
    public boolean remove(String table, String primaryKeyName, Object primaryKeyValue) throws SQLException {
        String preparedStatementSQL = String.format("DELETE FROM %s WHERE %s = ?", table, primaryKeyName);

        try (Connection connection = connectionPool.getConnection()) {
            // Delete happens in transaction.
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(preparedStatementSQL);
            preparedStatement.setObject(1, primaryKeyValue);
            int result = preparedStatement.executeUpdate();
            connection.commit();
            return result > 0;
        }
    }

    /**
     * Shuts down connection pool.
     */
    public void shutdown() {
        connectionPool.shutdown();
    }

    /**
     * Converts JDBC result set to a list of simple maps: columnName to columnValue.
     *
     * @param rs Result set to convert.
     * @return List of simple maps
     * @throws SQLException In case of error.
     */
    private List<Map<String, Object>> resultSetToArrayList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }
}
