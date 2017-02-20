package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Pattern;
import javax.persistence.*;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.PersistenceUnitLoadingException;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table
public class User {

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
  private static EntityManagerFactory entityManagerFactory;

  @Transient
  private EntityManager entityManager;

  @Transient
  private static Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

  /**
   * Initializes the User class, by giving it a reference to an EntityManagerFactory.
   *
   * @param  entityManagerFactory  an EntityManagerFactory for the current database server
   */
  public static void initializeClass(EntityManagerFactory entityManagerFactory) {
    User.entityManagerFactory = entityManagerFactory;
    EntityManager em = entityManagerFactory.createEntityManager();

    Query findUserByUsername = em.createQuery("SELECT u FROM User u WHERE u.username = :username");
    entityManagerFactory.addNamedQuery("findUserByUsername", findUserByUsername);

    Query findUserByEmail = em.createQuery("SELECT u FROM User u WHERE u.email = :email");
    entityManagerFactory.addNamedQuery("findUserByEmail", findUserByEmail);

    em.close();
  }

  /**
   * Creates a new User.
   * The User object will create its own EntityManager.
   */
  public User() {
    updateEntityManager();
  }

  /**
   * Updates the current entityManager.
   */
  private void updateEntityManager() {
    if (entityManager != null) {
      close();
    }
    entityManager = entityManagerFactory.createEntityManager();
  }

  /**
   * Tries to create a database entry, using the current values of the fields.
   * Does not catch exceptions thrown by the database.
   */
  public void create() {
    EntityTransaction et = entityManager.getTransaction();
    et.begin();
    entityManager.persist(this);
    et.commit();
  }

  /**
   * Tries to update a database entry, using the current values of the fields.
   * Does not catch exceptions thrown by the database.
   */
  public void update() {
    EntityTransaction et = entityManager.getTransaction();
    et.begin();
    entityManager.merge(this);
    et.commit();
  }

  /**
   * Tries to delete a database entry.
   * Does not catch exceptions thrown by the database.
   */
  public void delete() {
    User.delete(id);
  }

  /**
   * Tries to delete a database entry.
   * Does not catch exceptions thrown by the database.
   *
   * @param id The id of the user to be deleted
   */
  public static void delete(int id) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction et = entityManager.getTransaction();
    et.begin();
    // User object must be deleted by same manager that finds it
    User user = entityManager.find(User.class, id);
    entityManager.remove(user);
    et.commit();
    entityManager.close();
  }

  /**
   * Tries to find a database entry by looking up a field in the database.
   * Returns the first found result. Should only be used on columns that are unique.
   *
   * @param namedQueryString  The name of a query that is saved in the entityManagerFactory
   * @param fieldName         A parameter field that is specified in the corresponding query
   * @param fieldValue        The value of that parameter field. This is value we search for
   * @return                  The User with fieldName == fieldValue, null if not found
   */
  private static User find(String namedQueryString, String fieldName, String fieldValue) {
    EntityManager em = entityManagerFactory.createEntityManager();
    TypedQuery<User> nq = em.createNamedQuery(namedQueryString, User.class);
    nq.setParameter(fieldName, fieldValue);
    User user;
    try {
      user = nq.getSingleResult();
      user.updateEntityManager();
    } catch (NoResultException e) {
      user = null;
    }
    em.close();
    return user;
  }

  /**
   * Tries to find a database entry by looking up username in the database.
   *
   * @param username The username of the user to be found
   * @return         The User with the specified username, null if not found
   */
  public static User findByUsername(String username) {
    return find("findUserByUsername", "username", username);
  }

  /**
   * Tries to find a database entry by looking up email in the database.
   *
   * @param email The email of the user to be found
   * @return      The User with the specified email, null if not found
   */
  public static User findByEmail(String email) {
    return find("findUserByEmail", "email", email);
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
   * @return          True if candidate matches hash, false otherwise
   */
  public boolean checkPassword(String candidate) {
    // copied from http://www.mindrot.org/projects/jBCrypt/
    return BCrypt.checkpw(candidate, hashword);
  }

  /**
   * Closes the private entityManager, freeing resources.
   * After this is called, you will not be able to use the create/update/delete/find methods.
   */
  public void close() {
    entityManager.close();
  }

  /** Return the id of the User.
   *
   *  @return The id of the User
   */
  public int getId() {
    return id;
  }

  /** Set the id of the User.
   *
   *  @param id New id of the User, to be set
   */
  public void setId(int id) {
    this.id = id;
  }

  /** Get the username of the User.
   *
   *  @return the username of the User
   */
  public String getUsername() {
    return username;
  }

  /** Set the username of the User.
   *
   *  @param username New username of the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /** Get the email of the User.
   *
   *  @return the email of the User
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
    if(User.emailPattern.matcher(email).matches()){
      this.email = email;
    }
    else {
      throw new IllegalArgumentException("Does not match email pattern");
    }
  }

  /** Get the sessionToken of the User.
   *
   *  @return The sessionToken of the User
   */
  public String getSessionToken() {
    return sessionToken;
  }

  /** Set The sessionToken of the User.
   *
   *  @param sessionToken New sessionToken of the User, to be set
   */
  public void setSessionToken(String sessionToken) {
    this.sessionToken = sessionToken;
  }

  /** Get the sessionTokenExpireDate of the User.
   *
   *  @return The sessionTokenExpireDate of the User
   */
  public Date getSessionTokenExpireDate() {
    return sessionTokenExpireDate;
  }

  /** Set the sessionTokenExpireDate of the User.
   *
   *  @param sessionTokenExpireDate New sessionTokenExpireDate of the User, to be set
   */
  public void setSessionTokenExpireDate(Date sessionTokenExpireDate) {
    this.sessionTokenExpireDate = sessionTokenExpireDate;
  }

  /** Interactively create/update/delete Users from the database. */
  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    try {
      EntityManagerFactory entityManagerFactory = Persistence
          .createEntityManagerFactory("Eclipselink_JPA");
      User.initializeClass(entityManagerFactory);

      System.out.print("Enter R to remove, U to update, anything else to create: ");
      String choice = bufferedReader.readLine();
      if (choice.equals("R")) {
        System.out.print("Enter username to delete: ");
        User user = User.findByUsername(bufferedReader.readLine());

        user.delete();
        System.out.println("User removed successfully!");
      } else if(choice.equals("U")) {
        System.out.print("Enter username to update: ");
        User user = User.findByUsername(bufferedReader.readLine());
        System.out.print("Enter new username: ");
        user.setUsername(bufferedReader.readLine());
        System.out.print("Enter new email: ");
        user.setEmail(bufferedReader.readLine());
        System.out.print("Enter new password: ");
        user.setPassword(bufferedReader.readLine());

        user.update();
        System.out.println("User updated successfully!");
      } else {
        User user = new User();
        System.out.print("Enter username: ");
        user.setUsername(bufferedReader.readLine());
        System.out.print("Enter email: ");
        user.setEmail(bufferedReader.readLine());
        System.out.print("Enter password: ");
        user.setPassword(bufferedReader.readLine());

        user.create();
        System.out.println("User creation successful!");
      }
    } catch (PersistenceUnitLoadingException e) {
      System.out.println("Could not load persistence unit");
    } catch (DatabaseException e) {
      System.out.println("Database connection failed");
    } catch (IOException e) {
      System.out.println("Unable to create user");
    }

    entityManagerFactory.close();
  }
}
