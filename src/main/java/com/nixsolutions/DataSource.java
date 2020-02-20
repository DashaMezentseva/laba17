package com.nixsolutions;

import java.util.ResourceBundle;
import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource {

    private BasicDataSource dataSource;
    private BasicDataSource dataSourceTest;

    public BasicDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public BasicDataSource getDataSourceTest() {
        return dataSourceTest;
    }

    public void setDataSourceTest(BasicDataSource dataSourceTest) {
        this.dataSourceTest = dataSourceTest;
    }

    public DataSource() {
    }

    public DataSource(BasicDataSource dataSource, BasicDataSource dataSourceTest) {
        this.dataSource = dataSource;
        this.dataSourceTest = dataSourceTest;
    }

    public DataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    {
        dataSource = new BasicDataSource();
        dataSourceTest = new BasicDataSource();

        ResourceBundle resourceBundle = ResourceBundle.getBundle("database");
        ResourceBundle resourceBundleTest = ResourceBundle.getBundle("test");

        try {
            dataSource.setDriverClassName(resourceBundle.getString("driver"));
            dataSource.setUrl(resourceBundle.getString("url"));
            dataSource.setUsername(resourceBundle.getString("user"));
            dataSource.setPassword(resourceBundle.getString("password"));
            dataSource.setMaxIdle(10);
            dataSource.setMinIdle(5);
            dataSource.setMaxOpenPreparedStatements(50);

            dataSourceTest.setDriverClassName(resourceBundleTest.getString("driver"));
            dataSourceTest.setUrl(resourceBundleTest.getString("url"));
            dataSourceTest.setUsername(resourceBundleTest.getString("user"));
            dataSourceTest.setPassword(resourceBundleTest.getString("password"));
            dataSourceTest.setMaxIdle(10);
            dataSourceTest.setMinIdle(5);
            dataSourceTest.setMaxOpenPreparedStatements(50);
        } catch ( InstantiationError e) {
            throw new RuntimeException(e);
        }

    }

}
