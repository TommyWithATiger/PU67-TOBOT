package data.DataAccessObjects;

import data.DataAccessObjects.util.FieldTuple;
import data.User;
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

  protected static UserDAO instance;

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
   * Returns the static UserDao instance,
   * Creates one if null
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   * @return the Static UserDao instance for this server instance
   */
  public static UserDAO getInstance(EntityManagerFactory emFactory) {
    if (instance == null) {
      instance = new UserDAO(emFactory);
    }
    return instance;
  }

}
