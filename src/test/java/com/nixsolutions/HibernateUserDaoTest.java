package com.nixsolutions;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;
import static org.h2.table.Table.TABLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Date;

import com.nixsolutions.domain.Role;
import com.nixsolutions.domain.User;
import com.nixsolutions.service.HibernateUserDao;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HibernateUserDaoTest {
    private static IDatabaseTester databaseTester = null;
    private static final String DATASET = "dataset/fillTable.xml";
    private static final String TABLE_EMPTY = "dataset/user/emptyUser.xml";
    private static final String TABLE_NAME = "user";
    private static final String[] IGNORE_COLS = {"userId"};
    private HibernateUserDao userDao;
    private User[] users;

    @Before
    public void importDataSet() throws Exception {
        userDao = new HibernateUserDao();
        IDataSet dataSet = readDataSet();
        beforeStart(dataSet);

        users = new User[3];

        java.sql.Date date1 = java.sql.Date.valueOf("1990-01-01");
        java.sql.Date date2 = java.sql.Date.valueOf("1991-02-02");
        java.sql.Date date3 = java.sql.Date.valueOf("1992-03-03");

        Role role1 = new Role(1L, "aaa");
        Role role2 = new Role(2L, "bbb");

        User user1 = new User(1L, "aaa", "aaa",
                "aaa", "aaa", "aaa",
                date1, role2);
        User user2 = new User(2L, "bbb", "bbb",
                "bbb", "bbb", "bbb",
                date2, role2);
        User user3 = new User(3L, "ccc", "ccc",
                "ccc", "ccc", "ccc",
                date3, role1);

        users[0] = user1;
        users[1] = user2;
        users[2] = user3;

    }

    public void fill(String dataSetPath) throws Exception {

        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(dataSetPath));
    }

    public ITable getActualTable(String tableName) throws Exception {

        IDataSet actualData = databaseTester.getConnection().createDataSet();

        ITable actualTable = actualData.getTable(tableName);

        return actualTable;
    }

    public ITable getTableFromFile(String tableName, String xmlFilePath)
            throws Exception {
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(xmlFilePath));
        return dataSet.getTable(tableName);
    }

    private IDataSet readDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DATASET));
    }

    private void beforeStart(IDataSet dataSet) throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        String driver = configuration.getProperty("hibernate.connection.driver_class");
        String url = configuration.getProperty("hibernate.connection.url");
        String username = configuration.getProperty("hibernate.connection.username");
        String password = configuration.getProperty("hibernate.connection.password");
        configuration.buildSessionFactory().openSession();
        databaseTester = new JdbcDatabaseTester(driver, url, username, password);
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setDataSet(dataSet);
        databaseTester.onSetup();
    }

    @Test(expected = NullPointerException.class)
    public void userCreate() throws Exception {
        userDao.create(null);

    }

    @Test(expected = NullPointerException.class)
    public void userUpdate() throws Exception {
        userDao.update(null);

    }

    @Test(expected = NullPointerException.class)
    public void userRemove() throws Exception {
        userDao.remove(null);

    }

    @Test(expected = NullPointerException.class)
    public void userFindByLogin() throws Exception {
        User user = userDao.findByLogin(null);
    }

    @Test(expected = NullPointerException.class)
    public void userFindByEmail() throws Exception {
        User user = userDao.findByEmail(null);
    }

}
