package users;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class UserManager {

  private static UserManager instance = null;
  private static EntityManagerFactory entityManagerFactory = null;

  private UserManager() {
    entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
  }

  public User create(String username, String email, String hashword, String salt) {
    EntityManager entitymanager = entityManagerFactory.createEntityManager();
    entitymanager.getTransaction().begin();

    User user = new User(username, email, hashword, salt);

    entitymanager.persist(user);
    entitymanager.getTransaction().commit();
    entitymanager.close();

    return user;
  }

  public User create(int id, String username, String email, String hashword, String salt) {
    EntityManager entitymanager = entityManagerFactory.createEntityManager();
    entitymanager.getTransaction().begin();

    User user = new User(id, username, email, hashword, salt);

    entitymanager.persist(user);
    entitymanager.getTransaction().commit();
    entitymanager.close();

    return user;
  }

  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }
    return instance;
  }

  public static void close(){
    if(entityManagerFactory != null){
      entityManagerFactory.close();
    }
  }

}

