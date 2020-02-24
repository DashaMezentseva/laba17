//package com.nixsolutions;
//
//import static org.dbunit.Assertion.assertEqualsIgnoreCols;
//import static org.hamcrest.CoreMatchers.is;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
//
//import com.nixsolutions.service.RoleDao;
//import com.nixsolutions.domain.Role;
//import com.nixsolutions.domain.User;
//import com.nixsolutions.util.HibernateUtil;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//import org.dbunit.dataset.DataSetException;
//import org.dbunit.dataset.IDataSet;
//import org.dbunit.dataset.ITable;
//import org.dbunit.operation.DatabaseOperation;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.junit.Before;
//import org.junit.Test;
//
//public class HibernateRoleDaoTest{
//
//    private static final String EMPTY_TABLE = "dataset/role/emptyRole.xml";
//    private static final String FILL_DATASET = "dataset/fillTable.xml";
//    private static final String TABLE = "role";
//    private static final String[] IGNORE = {"roleId"};
//    private RoleDao roleDao = new HibernateRoleDao();
//    private Role[] roles = new Role[3];
//    private User[] users = new User[3];
//    private IDataSet dataSet;
//
//    public IDataSet getDataSet() {
//        return dataSet;
//    }
//
//    public void setDataSet(IDataSet dataSet) {
//        this.dataSet = dataSet;
//    }
//
//    @Before
//    public void setUp() throws Exception {
//
//        dataSet = getDataSet(FILL_DATASET);
//
//        roleTable = dataSet.getTable("role");
//        int numberRoleRow = roleTable.getRowCount();
//
//        ITable userTable = dataSet.getTable("user");
//        int numberUserRow = userTable.getRowCount();
//        roles[0] = new Role(1L, "aaa");
//        roles[1] = new Role(2L, "bbb");
//        roles[2] = new Role(3L, "ccc");
//
//        java.sql.Date date1 = java.sql.Date.valueOf("1990-01-01");
//        java.sql.Date date2 = java.sql.Date.valueOf("1991-02-02");
//        java.sql.Date date3 = java.sql.Date.valueOf("1992-03-03");
//
//        users[0] = new User(1L, "aaa", "aaa", "aaa", "aaa", "aaa", date1, new Role(2L, "bbb"));
//        users[1] = new User(2L, "bbb", "bbb", "bbb", "bbb", "bbb", date2, new Role(3L, "ccc"));
//        users[2] = new User(3L, "ccc", "ccc", "ccc", "ccc", "ccc", date3, new Role(1L, "aaa"));
//
//        getSetUpOperation();
//    }
//
//    @Test
//    public void testCreate() throws Exception {
//
//        roleDao.create(roles[0]);
//        roleDao.create(roles[1]);
//        roleDao.create(roles[2]);
//
//        String after = "dataset/role/roleAfterCreating.xml";
//        dataSet = getDataSet(after);
//        ITable expected = dataSet.getTable("role");
//        ITable actual = getActualTable("role");
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//
//        String after = "dataset/role/roleAfterUpdating.xml";
//
//        Role role = roles[2];
//        roleDao.create(role);
//        role.setName("updateName");
//        roleDao.update(role);
//        dataSet = getDataSet(after);
//        ITable expected = dataSet.getTable("role");
//        ITable actual = getActualTable("role");
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//    }
//
//    @Test
//    public void testRemove() throws Exception {
//        String after = "dataset/role/roleAfterRemoving.xml";
//
//        roleDao.remove(roles[2]);
//
//        dataSet = getDataSet(after);
//        ITable expected = dataSet.getTable("role");
//        ITable actual = getActualTable("role");
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//
//
//    }
//
//    @Test
//    public void testFindByName() throws Exception {
//
//        Role role = roleDao.findByName(roles[0].getName());
//        assertThat(role.getName(), is("aaa"));
//
//
//
//
//    }
//
//
//}
