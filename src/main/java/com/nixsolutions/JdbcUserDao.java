package com.nixsolutions;

import com.nixsolutions.dao.AbstractJdbcDao;
import com.nixsolutions.dao.UserDao;
import com.nixsolutions.entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcUserDao extends AbstractJdbcDao implements UserDao {


    private static final Logger LOG = LoggerFactory
        .getLogger(JdbcUserDao.class);

    private BasicDataSource dataSource = null;

    private static final String INSERT_USER_SQL = "INSERT INTO user "
        + "VALUES(NULL,?,?,?,?,?,?,?)";

    private static final String UPDATE_USER = "UPDATE user SET login=?,"
        + " password=?, email=?, firstName=?, lastName=?, birthday=?,"
        + " role_id=? WHERE id=?";

    private static final String DELETE_USER = "DELETE FROM user WHERE id=?";

    private static final String FIND_ALL_USERS = "SELECT * FROM user";
    private static final String FIND_USER_BY_LOGIN =
        "SELECT * FROM user WHERE login =?";
    private static final String FIND_USER_BY_EMAIL =
        "SELECT * FROM user WHERE email =?";

    private static final String ID = "ID";
    private static final String LOGIN = "LOGIN";
    private static final String PASSWORD = "PASSWORD";
    private static final String EMAIL = "EMAIL";
    private static final String FIRST_NAME = "FIRSTNAME";
    private static final String LAST_NAME = "LASTNAME";
    private static final String BIRTHDAY = "BIRTHDAY";
    private static final String ROLE_ID = "ROLE_ID";
    private static final String FIND_USER_BY_ID ="SELECT * FROM user WHERE id =?";

    private BasicDataSource dataSource() {
        return new BasicDataSource();
    }

    public JdbcUserDao(BasicDataSource dataSource) {
        super(dataSource);
    }

    public JdbcUserDao() {
    }

    @Override
    public void create(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(INSERT_USER_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setDate(6, new java.sql.Date(
                user.getBirthday().getTime()));
            preparedStatement.setLong(7, user.getRoleId());

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            LOG.error("Cannot create a user", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }

    }

    @Override
    public void update(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getFirstName());
            preparedStatement.setString(5, user.getLastName());
            preparedStatement.setDate(6, new java.sql.Date(
                user.getBirthday().getTime()));
            preparedStatement.setLong(7, user.getRoleId());
            preparedStatement.setLong(8, user.getId());

            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot update a user", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }

    }

    @Override
    public void remove(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setLong(1, user.getId());

            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot remove a user", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(preparedStatement);
            close(connection);
        }

    }

    @Override
    public List<User> findAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> list = new ArrayList<>();

        try {
            connection = createConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(FIND_ALL_USERS);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
                list.add(user);
            }
            connection.commit();
        } catch (SQLException e) {
            LOG.error("Cannot find all users", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
        return list;


    }

    @Override
    public User findByLogin(String login) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(FIND_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
            }
            connection.commit();

        } catch (SQLException | NullPointerException e ) {
            LOG.error("Cannot find user by login", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(FIND_USER_BY_EMAIL);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
            }
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot find user by email", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return user;
    }

    public User findById(String id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = createConnection();
            preparedStatement = connection.prepareStatement(FIND_USER_BY_ID);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(ID));
                user.setLogin(resultSet.getString(LOGIN));
                user.setPassword(resultSet.getString(PASSWORD));
                user.setEmail(resultSet.getString(EMAIL));
                user.setFirstName(resultSet.getString(FIRST_NAME));
                user.setLastName(resultSet.getString(LAST_NAME));
                user.setBirthday(resultSet.getDate(BIRTHDAY));
                user.setRoleId(resultSet.getLong(ROLE_ID));
            }
            connection.commit();

        } catch (SQLException e) {
            LOG.error("Cannot find user by id", e);
            rollbackConnection(connection);
            throw new RuntimeException(e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            close(connection);
        }
        return user;
    }
}
