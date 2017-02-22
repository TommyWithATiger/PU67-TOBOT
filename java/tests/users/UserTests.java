package users;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.when;

import base.BaseTest;
import data.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
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

  private User makeUser() {
    User user = new User();
    user.setId(100);
    user.setUsername("username");
    user.setEmail("email@email.com");
    user.setPassword("hunter2");
    user.create();
    return user;
  }

  @Test
  public void testCreateUser() throws Exception {

    User user = makeUser();

    assertEquals(100, user.getId());
    assertEquals("username", user.getUsername());
    assertEquals("email@email.com", user.getEmail());

    InOrder inOrder = Mockito.inOrder(entityManagerFactory, entityManager, entityTransaction);

    inOrder.verify(entityManagerFactory).createEntityManager();
    inOrder.verify(entityManager).getTransaction();
    inOrder.verify(entityTransaction).begin();
    inOrder.verify(entityManager).persist(user);
    inOrder.verify(entityTransaction).commit();
    inOrder.verify(entityManager).close();
  }

  @Test
  public void testDeleteUser() throws Exception {

    User user = makeUser();

    when(entityManager.find(User.class, 100)).thenReturn(user);

    user.delete();

    InOrder inOrder = Mockito.inOrder(entityManagerFactory, entityManager, entityTransaction);

    inOrder.verify(entityManagerFactory).createEntityManager();
    inOrder.verify(entityManager).getTransaction();
    inOrder.verify(entityTransaction).begin();
    inOrder.verify(entityManager).remove(user);
    inOrder.verify(entityTransaction).commit();
    inOrder.verify(entityManager).close();
  }

  @Test
  public void testUpdateUser() throws Exception {

    User user = makeUser();

    user.setEmail("email2@email.com");
    user.update();

    assertEquals(user.getEmail(), "email2@email.com");

    InOrder inOrder = Mockito.inOrder(entityManagerFactory, entityManager, entityTransaction);

    inOrder.verify(entityManagerFactory).createEntityManager();
    inOrder.verify(entityManager).getTransaction();
    inOrder.verify(entityTransaction).begin();
    inOrder.verify(entityManager).merge(user);
    inOrder.verify(entityTransaction).commit();
    inOrder.verify(entityManager).close();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongEmail() throws Exception {
    User user = new User();
    user.setEmail("this_is_not_valid");
  }

  @Test
  public void testCorrectEmail() throws Exception {
    User user = new User();
    user.setEmail("this_is_valid@this_is_valid");
    assertEquals(user.getEmail(), "this_is_valid@this_is_valid");
  }

  @Test
  public void testPassword() throws Exception {
    User user = new User();
    user.setPassword("hunter2");
    assertTrue(user.checkPassword("hunter2"));
    assertFalse(user.checkPassword("password123"));
  }
}
