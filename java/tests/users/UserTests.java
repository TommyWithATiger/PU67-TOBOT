package users;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

import base.BaseTest;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTests extends BaseTest {

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityTransaction entityTransaction;

  @Before
  public void init() {
    when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    when(entityManager.getTransaction()).thenReturn(entityTransaction);
  }

  @After
  public void destroy() {
    entityManagerFactory.close();
  }

  @Test
  public void testCreateUser() throws Exception {

    User user = new User(entityManagerFactory);
    user.setId(100);
    user.setUsername("username");
    user.setEmail("email@email.com");
    user.setHashword("aduhaophao");
    user.setSalt("afoiahfoh");

    user.create();

    assertEquals("username",        user.getUsername());
    assertEquals("email@email.com", user.getEmail());
    assertEquals("aduhaophao",      user.getHashword());
    assertEquals("afoiahfoh",       user.getSalt());

    user.close();

    InOrder inOrder = Mockito.inOrder(entityManagerFactory, entityManager, entityTransaction);

    inOrder.verify(entityManagerFactory).createEntityManager();
    inOrder.verify(entityManager).getTransaction();
    inOrder.verify(entityTransaction).begin();
    inOrder.verify(entityManager).persist(user);
    inOrder.verify(entityTransaction).commit();
    inOrder.verify(entityManager).close();
  }
}
