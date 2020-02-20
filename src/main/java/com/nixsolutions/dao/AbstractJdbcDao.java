package com.nixsolutions.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJdbcDao {

    private BasicDataSource dataSource;

    private static final Logger LOGGER = LoggerFactory
        .getLogger(AbstractJdbcDao.class);

    public AbstractJdbcDao(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public AbstractJdbcDao() {
    }

    protected synchronized Connection createConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() throws SQLException {
        Connection connection = createConnection();
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS USER;");
        statement.execute("DROP TABLE IF EXISTS ROLE;");
        statement.execute("CREATE TABLE ROLE("
            + "        ID LONG NOT NULL AUTO_INCREMENT,"
            + "        NAME VARCHAR(50) UNIQUE NOT NULL,"
            + "    PRIMARY KEY(ID));");
        statement.execute("CREATE TABLE USER("
            + "        ID LONG NOT NULL AUTO_INCREMENT,"
            + "        LOGIN VARCHAR(50) UNIQUE NOT NULL,"
            + "    PASSWORD VARCHAR(50) NOT NULL,"
            + "    EMAIL VARCHAR(50) UNIQUE NOT NULL,"
            + "    FIRSTNAME VARCHAR(30) NOT NULL,"
            + "    LASTNAME VARCHAR(30) NOT NULL,"
            + "    BIRTHDAY DATE NOT NULL,"
            + "    ROLE_ID LONG NOT NULL,"
            + "    PRIMARY KEY(ID),"
            + "    FOREIGN KEY(ROLE_ID) REFERENCES role(ID)"
            + "    ON DELETE CASCADE ON UPDATE CASCADE);");
    }

    public void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("Cannot close a connection", e);
            }
        }
    }

    public void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.error("Cannot close a statement", e);
            }
        }
    }

    public void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.error("Cannot close a resultSet", e);
            }
        }
    }

    public void close(Connection connection, Statement statement, ResultSet resultSet) {
        close(resultSet);
        close(statement);
        close(connection);
    }

    public void rollbackConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                LOGGER.error("Cannot rollback connection", e);
            }
        }

    }
}
