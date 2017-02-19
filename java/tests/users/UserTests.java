package users;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
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
    User.initializeClass(entityManagerFactory);
  }

  private User makeUser(){
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

    assertEquals(100,               user.getId());
    assertEquals("username",        user.getUsername());
    assertEquals("email@email.com", user.getEmail());
    assertTrue(user.checkPassword("hunter2"));
    assertFalse(user.checkPassword("password123"));

    user.close();

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

    user.setEmail("email");
    user.update();

    assertEquals(user.getEmail(), "email");

    user.close();

    InOrder inOrder = Mockito.inOrder(entityManagerFactory, entityManager, entityTransaction);

    inOrder.verify(entityManagerFactory).createEntityManager();
    inOrder.verify(entityManager).getTransaction();
    inOrder.verify(entityTransaction).begin();
    inOrder.verify(entityManager).merge(user);
    inOrder.verify(entityTransaction).commit();
    inOrder.verify(entityManager).close();
  }
}
