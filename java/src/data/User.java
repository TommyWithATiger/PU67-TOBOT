package data;

import data.DataAccessObjects.UserDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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

  public User() {
    super();
    userDAO = UserDAO.getInstance();
  }

  /**
   * Instantiates a User object
   *
   * @param username, the username of the user
   * @param email, the email of the user
   */
  public User(String username, String email) {
    this();
    this.username = username;
    this.email = email;
  }

  /**
   * Adds the User to the database
   */
  public void create() {
    userDAO.persist(this);
  }

  /**
   * Removes the User from the database
   */
  public void delete() {
    userDAO.remove(this);
  }

  /**
   * Updates the User's database entry
   */
  public void update() {
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
   * @param id New id of the User
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
   * followed by text).
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
   * @param sessionToken New sessionToken of the User
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
   * @param sessionTokenExpireDate New sessionTokenExpireDate of the User
   */
  public void setSessionTokenExpireDate(Date sessionTokenExpireDate) {
    this.sessionTokenExpireDate = sessionTokenExpireDate;
  }

  /**
   * Generates a new sessionTokenExpireDate
   */
  public void generateSessionTokenExpireDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, 1);
    setSessionTokenExpireDate(calendar.getTime());
  }

  /**
   * Creates a new sessionToken
   */
  public void createSessionToken() {
    generateSessionTokenExpireDate();
    while (getSessionToken() == null) {
      String sessionToken = generateSessionToken();
      try {
        setSessionToken(sessionToken);
      } catch (javax.persistence.RollbackException e) {
        // Ignore, try again
      }
    }
  }

  /**
   * Creates a new sessionToken
   *
   * @return a sessionToken
   */
  private String generateSessionToken() {
    String token = "";
    Random random = new Random();
    String validSymbols = "abcdefghijklmnopqrstuvwxyz0123456789";
    for (int token_symbol = 0; token_symbol < 64; token_symbol++) {
      token += validSymbols.charAt(random.nextInt(validSymbols.length()));
    }
    return token;
  }

  /**
   * Checks a if a session is valid
   *
   * @param token a token for a session to be checked
   * @return true if the session is valid, else false
   */
  public boolean checkUserSessionToken(String token) {
    String actualToken = getSessionToken();
    if (!token.equals(actualToken)) {
      return false;
    }

    if (getSessionTokenExpireDate().before(new Date())) {
      logout();
      return false;
    }
    return true;
  }

  /**
   * Resets the session information
   */
  public void logout() {
    setSessionToken(null);
    setSessionTokenExpireDate(null);
  }

}
