package data.user;

import data.dao.UserDAO;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@NamedQueries({
    @NamedQuery(name = "findAllUsers", query = "SELECT u FROM User u"),
    @NamedQuery(name = "findUsersByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "findUsersByEmail", query = "SELECT u FROM User u WHERE u.email = :email")
})
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;
  private String username;
  private String email;
  private String hashword;
  private String sessionToken;

  @Enumerated(value = EnumType.STRING)
  @Convert(converter = UserTypeConverter.class)
  private UserType userType = UserType.STUDENT;

  @Temporal(TemporalType.TIMESTAMP)
  private Date sessionTokenExpireDate;

  public User() {
    super();
  }

  /**
   * Instantiates a User object
   *
   * @param username, the username of the user
   * @param email, the email of the user
   * @param password, the password of the user
   */
  public User(String username, String email, String password) {
    this();
    setUsername(username);
    setEmail(email);
    setPassword(password);
  }

  /**
   * Instantiates a User object
   *
   * @param username, the username of the user
   * @param email, the email of the user
   * @param password, the password of the user
   * @param userType, the type of user
   */
  public User(String username, String email, String password, UserType userType) {
    this(username, email, password);
    this.userType = userType;
  }

  /**
   * Adds the User to the database
   */
  public void create() {
    UserDAO.getInstance().persist(this);
  }

  /**
   * Removes the User from the database
   */
  public void delete() {
    UserDAO.getInstance().remove(this);
  }

  /**
   * Updates the User's database entry
   */
  public void update() {
    UserDAO.getInstance().merge(this);
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
    if (EmailValidator.getInstance().isValid(email)) {
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
   * A method for generation and setting a session token expiration date. It is set to be one day in
   * the future.
   */
  public void generateSessionTokenExpireDate() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.add(Calendar.DATE, 1);
    setSessionTokenExpireDate(calendar.getTime());
  }

  /**
   * A method for creating a session token. Also creates a the expiration date.
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
   * Generates the session token of length 64 with small letters and numbers.
   *
   * @return The session token
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
   * Checks if the user token is valid. If it is not valid but there is one set in the database, the
   * database is updated
   *
   * @param token The token to check against
   * @return A boolean indicating if the session token is valid.
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
    generateSessionTokenExpireDate();
    return true;
  }

  /**
   * Logs the user out, that is reset the session token and its expire date.
   */
  public void logout() {
    setSessionToken(null);
    setSessionTokenExpireDate(null);
  }

  /**
   * Get the userType of the User.
   *
   * @return enum, The userType of the User
   */
  public UserType getUserType() {
    return userType;
  }

  /**
   * Set The userType of the User.
   *
   * @param userType New userType of the User
   */
  public void setUserType(UserType userType) {
    this.userType = userType;
  }

  /**
   * Check if user is of ADMIN type
   *
   * @return true if ADMIN, else false
   */
  public boolean isAdmin() {
    return userType == UserType.ADMIN;
  }

  /**
   * Overrides the default equals method
   *
   * @param other The object to check against
   * @return A boolean indicating if the other object is equal to this, that is has the same id
   */
  @Override
  public boolean equals(Object other) {
    if (other == null || !(other instanceof User)) {
      return false;
    }
    User otherUser = (User) other;
    return otherUser.id == this.id;
  }

  /**
   * Overrides the hashcode of the object to be equal to its id
   *
   * @return the id of the object
   */
  @Override
  public int hashCode() {
    return id;
  }

}
