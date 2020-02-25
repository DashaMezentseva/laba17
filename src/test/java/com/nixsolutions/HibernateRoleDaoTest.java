package com.nixsolutions;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.SQLException;

import com.nixsolutions.domain.Role;
import com.nixsolutions.service.HibernateRoleDao;
import com.nixsolutions.service.RoleDao;
import org.dbunit.Assertion;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class HibernateRoleDaoTest {
    private static final String DATASET = "dataset/fillTable.xml";
    private static final String TABLE_EMPTY = "dataset/role/emptyRole.xml";
    private static final String TABLE_NAME = "role";
    private static final String[] IGNORE_COLS = {"roleId", "Birthday"};
    private RoleDao roleDao;
    private Role[] roles;

    private static IDatabaseTester databaseTester = null;

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




    @Before
    public void importDataSet() throws Exception {
        roleDao = new HibernateRoleDao();
        IDataSet dataSet = readDataSet();
        beforeStart(dataSet);

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

        roles = new Role[3];
        Role role1 = new Role(1L, "aaa");
        Role role2 = new Role(2L, "bbb");
        Role role3 = new Role(3L, "ccc");
        roles[0] = role1;
        roles[1] = role2;
        roles[2] = role3;
    }

    public void fill(String dataSetPath) throws Exception {

        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(dataSetPath));
    }

    @Test(expected = NullPointerException.class)
    public void roleCreate() throws Exception {
        roleDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void roleUpdate() throws Exception {
        roleDao.update(null);

    }

    @Test(expected = NullPointerException.class)
    public void roleRemove() throws Exception {
        roleDao.update(null);
    }
    @Test(expected = NullPointerException.class)
    public void roleFindByName() throws Exception {
        roleDao.findByName(null);
    }
    @Test(expected = NullPointerException.class)
    public void emptyRoleFindByName() throws Exception {
        roleDao.findByName("");
    }

    @Test
    public void testCreate() throws Exception {

        String after = "dataset/role/roleAfterCreating.xml";
        ITable expected = getTableFromFile(TABLE_NAME,
                after);
        ITable actual = getActualTable(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);

    }

    @Test
    public void testUpdate() throws Exception {
        Role role = new Role(2L, "updateName");
        roleDao.update(role);

        fill(TABLE_EMPTY);
        String after = "dataset/role/roleAfterUpdating.xml";

        ITable expected = getTableFromFile(TABLE_NAME,
                after);
        ITable actual = getActualTable(TABLE_NAME);

        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);

    }

    @Test
    public void testFindByName() throws Exception {
        fill(TABLE_EMPTY);
        roleDao.create(roles[0]);
        Role role = roleDao.findByName(roles[0].getName());
        Assert.assertEquals(roles[0], role);
    }
}
