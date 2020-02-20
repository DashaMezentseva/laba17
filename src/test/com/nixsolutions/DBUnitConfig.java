package com.nixsolutions;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import org.apache.commons.dbcp2.BasicDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUnitConfig {

    private BasicDataSource dataSource;


    private static final Logger LOGGER = LoggerFactory
        .getLogger(DBUnitConfig.class);

    public BasicDataSource getDataSource() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("test");

        if (dataSource == null) {
            dataSource = new BasicDataSource();
            dataSource.setDriverClassName(resourceBundle.getString("driver"));
            dataSource.setUrl(resourceBundle.getString("url"));
            dataSource.setUsername(resourceBundle.getString("user"));
            dataSource.setPassword(resourceBundle.getString("password"));
            dataSource.setMaxIdle(10);
            dataSource.setMinIdle(5);
            dataSource.setMaxOpenPreparedStatements(50);
        }
        return dataSource;

    }

    protected synchronized Connection createConnection() {
        Connection connection;
        try {
            connection = getDataSource().getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void setUp() throws Exception {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("test");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, resourceBundle.getString("driver"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, resourceBundle.getString("url"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, resourceBundle.getString("user"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, resourceBundle.getString("password"));
        createTable();
    }

    private void createTable() {
        LOGGER.trace("create Tables");
        Connection connection = null;
        try {
            connection = createConnection();
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
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("Cannot create tables", e);
            rollbackConnection(connection);
        } finally {
            close(connection);

        }

    }

    public void fillTable(String dataSetPath) {
        LOGGER.trace("fill Tables");
        Connection connection = null;
        try {
            connection = createConnection();
            IDataSet dataSet = new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetPath));
            DatabaseOperation.CLEAN_INSERT.execute(getIDBConnection(connection), dataSet);
            connection.commit();
        } catch (DatabaseUnitException | SQLException e) {
            LOGGER.error("Cannot fill tables", e);
            rollbackConnection(connection);
        } finally {
            close(connection);

        }
    }

    public ITable getExpectedTable(String tableName, String xmlFilePath) {
        ITable dataSetTable = null;
        try {
            FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(xmlFilePath));
            dataSetTable = flatXmlDataSet.getTable(tableName);
        } catch (DataSetException e) {
            LOGGER.error("DataSetException", e);
            throw new RuntimeException(e);
        }
        return dataSetTable;
    }

    public ITable getActualTable(String tableName) {
        ITable dataSetTable = null;
        Connection connection = null;
        try {
            connection = createConnection();
            IDataSet dataSet = getIDBConnection(connection).createDataSet();
            dataSetTable = dataSet.getTable(tableName);
            connection.commit();
        } catch (DataSetException | SQLException e) {
            LOGGER.error("DataSetException", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(connection);
        }
        return dataSetTable;

    }

    private IDatabaseConnection getIDBConnection(Connection connection) {
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = new DatabaseConnection(connection);
        } catch (DatabaseUnitException e) {
            LOGGER.error("DatabaseUnitException", e);
            throw new RuntimeException(e);
        }
        DatabaseConfig databaseConfig = databaseConnection.getConfig();
        databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
        return databaseConnection;
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
