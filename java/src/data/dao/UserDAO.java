package data.dao;

import data.dao.util.FieldTuple;
import data.user.User;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class UserDAO extends AbstractBaseDAO<User, Integer> {

  /**
   * Instantiates the UserDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  public UserDAO(EntityManagerFactory emFactory) {
    super(User.class, emFactory);
  }

  private static UserDAO instance;

  /**
   * Get all database occurrences of Subject
   *
   * @return List of all Subjects
   */
  public List<User> findAll() {
    return super.find("findAllUsers");
  }

  /**
   * Finds a User by doing a query for the username
   *
   * @param username, the username to query for
   * @return User with matching username, or null if it is not in the database
   */
  public User findUserByUsername(String username) {
    List<User> results = super.find("findUsersByUsername", new FieldTuple("username", username));
    if (!results.isEmpty()) {
      return results.get(0);
    }
    return null;
    //Fixme handle no result exception here
  }

  /**
   * Returns the static UserDAO instance
   *
   * @return UserDAO, null if not instantiated
   */
  public static UserDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static UserDao instance,
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !UserDAO.emFactory.isOpen()) {
      instance = new UserDAO(emFactory);
    }
  }

}
