package com.nixsolutions;

import com.nixsolutions.dao.AbstractJdbcDao;
import com.nixsolutions.dao.RoleDao;
import com.nixsolutions.entity.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcRoleDao extends AbstractJdbcDao implements RoleDao {

    private static final Logger LOG = LoggerFactory
        .getLogger(JdbcRoleDao.class);
    private BasicDataSource dataSource;

    private static final String INSERT_ROLE_SQL = "INSERT INTO role "
        + "VALUES(NULL,?)";

    private static final String UPDATE_ROLE = "UPDATE role SET name=?"
        + " WHERE id=?";

    private static final String DELETE_ROLE = "DELETE FROM role WHERE name=?";

    private static final String FIND_ROLE_BY_NAME =
        "SELECT * FROM role WHERE name =?";

    private static final String ROLE_ID = "ID";
    private static final String ROLE_NAME = "NAME";

    public JdbcRoleDao(BasicDataSource dataSource) {
        super(dataSource);
    }

    public JdbcRoleDao() {
    }

    private BasicDataSource dataSource() {
        return new BasicDataSource();
    }


    @Override
    public void create(Role role) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(
                INSERT_ROLE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot create a role", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }

    @Override
    public void update(Role role) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = createConnection();

            preparedStatement = connection.prepareStatement(UPDATE_ROLE);
            preparedStatement.setString(1, role.getName());
            preparedStatement.setLong(2, role.getId());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot update a role", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }

    }


    @Override
    public void remove(Role role) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(DELETE_ROLE);
            preparedStatement.setString(1, role.getName());
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot remove a role", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }
    }


    @Override
    public Role findByName(String name) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Role role = null;
        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(FIND_ROLE_BY_NAME);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                role = new Role();
                role.setId(resultSet.getLong(ROLE_ID));
                role.setName(resultSet.getString(ROLE_NAME));
            }
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot find a role", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return role;
    }
}
