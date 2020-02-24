package com.nixsolutions;

import com.nixsolutions.service.HibernateRoleDao;
import com.nixsolutions.service.RoleDao;
import com.nixsolutions.domain.Role;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleTest {
    private static final String DATASET = "dataset/role/fillRole.xml";
    private static final String TABLE_EMPTY = "dataset/role/emptyRole.xml";
    private static final String TABLE_NAME = "role";
    private static final String[] IGNORE_COLS = { "roleId" };
    private RoleDao roleDao;
    private DBTest dbTest;
    private Role[] roles;

    @BeforeClass
    public static void generalSetUp() throws Exception {
        new DBTest().createConnection();
        //DbTestHelper().initDb();
    }

    @Before
    public void setUp() throws Exception {
        roleDao = new HibernateRoleDao();
        dbTest = new DBTest();
        dbTest.getDataSet();
        dbTest.createConnection();
        dbTest.fill(DATASET);

        // Configure Roles
        roles = new Role[3];

        roles[0] = new Role(1L, "aaa");
        roles[1] = new Role(2L, "bbb");
        roles[2] = new Role(3L, "ccc");
    }

    @Test(expected = NullPointerException.class)
    public void testCreateWithNull() {
        roleDao.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithBadArgs() {
        Role role = roles[1];
        role.setName("");
        roleDao.create(role);
    }

//    @Test
//    public void testCreate() throws Exception {
//        dbTestHelper.fill(TABLE_EMPTY);
//        String afterCreate = "dataset/role/afterCreate.xml";
//
//        roleDao.create(roles[1]);
//
//        ITable expected = dbTestHelper.getTableFromFile(TABLE_NAME,
//                afterCreate);
//        ITable actual = dbTestHelper.getTableFromSchema(TABLE_NAME);
//
//        assertEqualsIgnoreCols(expected, actual, IGNORE_COLS);
//    }

}
