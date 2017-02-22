package data;

import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.*;

import data.DataAccessObjects.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQueries({
        @NamedQuery(name = "findUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "findUserByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User {

  @PersistenceContext
  private static UserDAO userDAO;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String username;
  private String email;
  private String hashword;
  private String sessionToken;

  @Temporal(TemporalType.TIMESTAMP)
  private Date sessionTokenExpireDate;

  @Transient
  private static Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

  /**
   * Creates a new User.
   * The User object will create its own EntityManager.
   */
  public User() {
    super();
    userDAO = UserDAO.getInstance();
  }

  public User(String username, String email) {
    this();
    this.username = username;
    this.email = email;
  }

  public void create(){
    userDAO.persist(this);
  }

  public void delete(){
    userDAO.remove(this);
  }

  public void update(){
    userDAO.merge(this);
  }


  /**
   * Generates a hash from the given password, and saves it.
   * The password is hashed and salted by bcrypt.
   * The password not stored.
   *
   * @param password The new password of the user
   */
  public void setPassword(String password) {
    // copied from http://www.mindrot.org/projects/jBCrypt/
    hashword = BCrypt.hashpw(password, BCrypt.gensalt());
  }

  /**
   * Checks if a given password matches the stored hash.
   *
   * @param candidate A candidate password
   * @return True if candidate matches hash, false otherwise
   */
  public boolean checkPassword(String candidate) {
    // copied from http://www.mindrot.org/projects/jBCrypt/
    return BCrypt.checkpw(candidate, hashword);
  }


  /**
   * Return the id of the User.
   *
   * @return The id of the User
   */
  public int getId() {
    return id;
  }

  /**
   * Set the id of the User.
   *
   * @param id New id of the User, to be set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get the username of the User.
   *
   * @return the username of the User
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the username of the User.
   *
   * @param username New username of the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Get the email of the User.
   *
   * @return the email of the User
   */
  public String getEmail() {
    return email;
  }

  /**
   * Set the email of the User.
   *
   * @param email New email of the User, to be set.
   * @throws IllegalArgumentException If the argument is not a valid email (text followed by @
   *                                  followed by text).
   */
  public void setEmail(String email) {
    if (User.emailPattern.matcher(email).matches()) {
      this.email = email;
    } else {
      throw new IllegalArgumentException("Does not match email pattern");
    }
  }

  /**
   * Get the sessionToken of the User.
   *
   * @return The sessionToken of the User
   */
  public String getSessionToken() {
    return sessionToken;
  }

  /**
   * Set The sessionToken of the User.
   *
   * @param sessionToken New sessionToken of the User, to be set
   */
  public void setSessionToken(String sessionToken) {
    this.sessionToken = sessionToken;
  }

  /**
   * Get the sessionTokenExpireDate of the User.
   *
   * @return The sessionTokenExpireDate of the User
   */
  public Date getSessionTokenExpireDate() {
    return sessionTokenExpireDate;
  }

  /**
   * Set the sessionTokenExpireDate of the User.
   *
   * @param sessionTokenExpireDate New sessionTokenExpireDate of the User, to be set
   */
  public void setSessionTokenExpireDate(Date sessionTokenExpireDate) {
    this.sessionTokenExpireDate = sessionTokenExpireDate;
  }

}
