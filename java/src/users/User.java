package users;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import javax.persistence.*;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.PersistenceUnitLoadingException;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String email;
    private String hashword;
    private String salt;
    private String sessionToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionTokenExpireDate;

    @Transient
    private static EntityManagerFactory entityManagerFactory;

    @Transient
    private EntityManager entityManager;

    public static void initializeClass(EntityManagerFactory entityManagerFactory) {
        User.entityManagerFactory = entityManagerFactory;
    }

    public User(){
      entityManager = entityManagerFactory.createEntityManager();
    }

    public void create(){
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.persist(this);
        et.commit();
    }

    public void update(){
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.merge(this);
        et.commit();
    }

    public void delete(){
        EntityTransaction et = entityManager.getTransaction();
        et.begin();
        entityManager.remove(this);
        et.commit();
    }

    public void close(){
        entityManager.close();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashword() {
        return hashword;
    }

    public void setHashword(String hash) {
        this.hashword = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSessionToken() {
      return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
      this.sessionToken = sessionToken;
    }

    public Date getSessionTokenExpireDate() {
      return sessionTokenExpireDate;
    }

    public void setSessionTokenExpireDate(Date sessionTokenExpireDate) {
      this.sessionTokenExpireDate = sessionTokenExpireDate;
    }

  public static void main(String[] args) {
    // run this to create a user
    System.setProperty("javax.xml.accessExternalDTD", "all");

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    try{
      EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
      User.initializeClass(entityManagerFactory);
      User user = new User();

      for(int i=0; i<20; i++){
        // try to clear screen
        System.out.println();
      }

      System.out.print("Enter username: ");
      user.setUsername(bufferedReader.readLine());
      System.out.print("Enter email: ");
      user.setEmail(bufferedReader.readLine());
      System.out.print("Enter hash: ");
      user.setHashword(bufferedReader.readLine());
      System.out.print("Enter salt: ");
      user.setSalt(bufferedReader.readLine());

      user.create();
      System.out.println("User creation successful!");
    }
    catch (PersistenceUnitLoadingException e){
      System.out.println("Could not load persistence unit");
    }
    catch (DatabaseException e){
      System.out.println("Database connection failed");
    }
    catch (IOException e){
      System.out.println("Unable to create user");
    }
  }
}
