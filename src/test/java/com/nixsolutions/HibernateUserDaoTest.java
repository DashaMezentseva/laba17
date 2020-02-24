package com.nixsolutions;

import static org.dbunit.Assertion.assertEqualsIgnoreCols;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.nixsolutions.service.HibernateRoleDao;
import com.nixsolutions.service.UserDao;
import com.nixsolutions.domain.Role;
import com.nixsolutions.domain.User;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.junit.Before;

public class HibernateUserDaoTest extends DBUnitConfig {

    private static final String FILL_DATASET = "dataset/fillTable.xml";
    private static final String EMPTY_TABLE = "dataset/user/emptyUser.xml";
    private static final String TABLE = "user";
    private static final String[] IGNORE = {"userId"};
    private UserDao userDao;
    private User[] users;
    private Role[] roles;
    private int numberRoleRow;
    private int numberUserRow;
    private HibernateRoleDao hibernateRoleDao = new HibernateRoleDao();


    @Before
    public void setUp() throws Exception {
        roles = new Role[2];
        users= new User[3];

        IDataSet dataSet = getDataSet(FILL_DATASET);
        ITable roleTable = dataSet.getTable("role");
        numberRoleRow = roleTable.getRowCount();

        ITable userTable = dataSet.getTable("user");
        numberUserRow = userTable.getRowCount();

        roles[0] = new Role(1L, "aaa");
        roles[1] = new Role(2L, "bbb");

        java.sql.Date date1 = java.sql.Date.valueOf("1990-01-01");
        java.sql.Date date2 = java.sql.Date.valueOf("1991-02-02");
        java.sql.Date date3 = java.sql.Date.valueOf("1992-03-03");


        users[0] = new User(1L, "aaa", "aaa", "aaa", "aaa", "aaa", date1, new Role(2L, "bbb"));
        users[1] = new User(2L, "bbb", "bbb", "bbb", "bbb", "bbb", date2, new Role(3L, "ccc"));
        users[2] = new User(3L, "ccc", "ccc", "ccc", "ccc", "ccc", date3, new Role(1L, "aaa"));

    }




//    @Test
//    public void testCreate() throws Exception {
//
//
//
//        fillTable(EMPTY_TABLE);
//        String after = "dataset/user/userAfterCreating.xml";
//        userDao.create(users[0]);
//        userDao.create(users[1]);
//        userDao.create(users[2]);
//
//        ITable expected = getExpectedTable(TABLE, after);
//        ITable actual = getActualTable(TABLE);
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        String after = "dataset/user/userAfterUpdating.xml";
//
//        User user = users[1];
//        user.setLogin("updateLogin");
//        user.setPassword("updatePassword");
//        user.setEmail("updateEmail");
//        user.setFirstName("updateFirstName");
//        user.setLastName("updateLastName");
//        user.getRole().setRoleId(2L);
//
//        userDao.update(user);
//
//        ITable expected = getExpectedTable(TABLE, after);
//        ITable actual = getActualTable(TABLE);
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//    }
//
//    @Test
//    public void testRemove() throws Exception {
//        String after = "dataset/user/userAfterRemoving.xml";
//
//        userDao.remove(users[2]);
//
//        ITable expected = getExpectedTable(TABLE, after);
//        ITable actual = getActualTable(TABLE);
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE);
//    }
//
//    @Test
//    public void testFindAll() throws Exception {
//        fillTable(FILL_DATASET);
//        List<User> expected = new ArrayList<>();
//        expected.add(users[0]);
//        expected.add(users[1]);
//        expected.add(users[2]);
//
//        List<User> actual = userDao.findAll();
//
//        assertEquals(expected, actual);
//
//    }
//
//    @Test
//    public void testFindByLogin() throws Exception {
//        User user = userDao.findByLogin(users[1].getLogin());
//        assertThat(user.getLogin(), is("bbb"));
//    }
//
//    @Test
//    public void testFindByEmail() throws Exception {
//        User user = userDao.findByEmail(users[1].getEmail());
//        assertThat(user.getEmail(), is("bbb"));
//    }



}
