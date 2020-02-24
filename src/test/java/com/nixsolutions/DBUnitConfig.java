package com.nixsolutions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUnitConfig {

    protected IDataSet dataSet;
    protected ITable roleTable;
    private IDatabaseTester tester;

    private static final Logger LOGGER = LoggerFactory
        .getLogger(DBUnitConfig.class);

    private static final String FILL_DATASET = "dataset/fillTable.xml";

    protected IDataSet getDataSet(String dataSetPath) throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream(dataSetPath));

    }

    protected IDatabaseConnection createConnection() throws Exception {
        Class.forName("org.h2.Driver");
        Connection jdbcConnection = DriverManager.getConnection(
            "jdbc:h2:mem:lab16;DB_CLOSE_DELAY=-1;", "sa", "");

        IDatabaseConnection dbUnitConnection = new DatabaseConnection(
            jdbcConnection);
        DatabaseConfig dbConfig = dbUnitConnection.getConfig();
        dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
            new H2DataTypeFactory());

        return dbUnitConnection;
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader().getResourceAsStream("dataset/fillTable.xml"));
    }

    public ITable getActualTable(String tableName) throws Exception {
        ITable dataSetTable = null;
        try {

            IDataSet dataSet = createConnection().createDataSet();
            dataSetTable = dataSet.getTable(tableName);

        } catch (DataSetException | SQLException e) {
            LOGGER.error("DataSetException", e);
            throw new RuntimeException(e);
        }
        return dataSetTable;

    }

    public void setUp() throws Exception {

    }


    protected DatabaseOperation getSetUpOperation() throws Exception
    {
        return DatabaseOperation.CLEAN_INSERT;
    }

}
