package users;

import java.util.Date;
import javax.persistence.*;

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
    private EntityManager entityManager;

    protected User() {
    // needed to satisfy JPA
    }

    public User(EntityManagerFactory entityManagerFactory) {
        assignEntityManager(entityManagerFactory);
    }

    public void assignEntityManager(EntityManagerFactory entityManagerFactory){
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
}
