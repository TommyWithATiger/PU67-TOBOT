import static junit.framework.TestCase.assertEquals;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserManager;

public class UserTests extends BaseTest {

  private EntityManagerFactory entityManagerFactory;

  @Before
  public void init() {
    entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");
  }

  @After
  public void destroy() {
    entityManagerFactory.close();
  }

  @Test
  public void testCreateUser() throws Exception {

    UserManager um = UserManager.getInstance();
    um.create(100,"username", "email@email.com", "aduhaophao", "afoiahfoh");
    um.close();

    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    User user = entityManager.find(User.class, 100);
    assertEquals("username",        user.getUsername());
    assertEquals("email@email.com", user.getEmail());
    assertEquals("aduhaophao",      user.getHashword());
    assertEquals("afoiahfoh",       user.getSalt());

    entityManager.remove(user);

    entityManager.getTransaction().commit();
    entityManager.close();
  }
}
