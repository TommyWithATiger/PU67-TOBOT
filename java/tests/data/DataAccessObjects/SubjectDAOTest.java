package data.DataAccessObjects;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

import base.BaseTest;
import data.Subject;
import data.Topic;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubjectDAOTest extends BaseTest {

  private static User user1;
  private static User user2;
  private static User user3;

  private static Topic topic1;
  private static Topic topic2;
  private static Topic topic3;

  private static Subject subject1;
  private static Subject subject2;
  private static Subject subject3;
  private static Subject subject4;
  private static Subject subject5;

  @BeforeClass
  public static void populate() {
    EntityManager em = entityManagerFactory.createEntityManager();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy", "Think and argue");
    topic3 = new Topic("Odontology", "Fix teeth");

    user1 = new User("John", "john@email.com", "hunter2");
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user3 = new User("Mary", "mary@email.com", "hunter2");

    em.getTransaction().begin();
    em.persist(topic1);
    em.persist(topic2);
    em.persist(topic3);
    em.persist(user1);
    em.persist(user2);
    em.persist(user3);
    em.getTransaction().commit();

    subject1 = new Subject("Ex.Phil", "NTNU", "EXP0004", "examen philosophicum");
    subject1.addTopic(topic2);
    subject1.addEditor(user1);
    subject2 = new Subject("Menneske maskin interaksjon", "NTNU", "TDT4180", "man machine interaction");
    subject2.addTopic(topic1);
    subject2.addEditor(user2);
    subject3 = new Subject("Menneske maskin interaksjon", "UIO", "INF4260", "man machine interaction");
    subject3.addTopic(topic1);
    subject3.addEditor(user3);
    subject4 = new Subject("Tannpleie 1", "UIO", "TP1100", "Innledende tannpleie I");
    subject4.addTopic(topic3);
    subject4.addEditor(user1);
    subject5 = new Subject("Databaser", "NTNU", "TDT4145", "Databasesystemer og modellering");
    subject5.addTopic(topic1);
    subject5.addEditor(user2);

    em.getTransaction().begin();
    em.persist(subject1);
    em.persist(subject2);
    em.persist(subject3);
    em.persist(subject4);
    em.persist(subject5);
    em.getTransaction().commit();
    em.close();
  }

  @Override
  public void cleanDB(){
    // Do not clean.
    // This allows populate to be @BeforeClass only, which speeds the tests up by a lot.
  }

  @Test
  public void testFindAll() throws Exception {
    List result = SubjectDAO.getInstance().findAll();
    assertTrue(result.contains(subject1));
    assertTrue(result.contains(subject2));
    assertTrue(result.contains(subject3));
    assertTrue(result.contains(subject4));
    assertTrue(result.contains(subject5));
    assertEquals(5, result.size());
  }

  @Test
  public void testFindSubjectsByTitle1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByTitle("Ex.Phil");
    assertTrue(result1.contains(subject1));
    assertEquals(1, result1.size());
  }

  @Test
  public void testFindSubjectsByTitle2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByTitle("Menneske maskin interaksjon");
    assertTrue(result2.contains(subject2));
    assertTrue(result2.contains(subject3));
    assertEquals(2, result2.size());
  }

  @Test
  public void testFindSubjectsByTitle3() throws Exception {
    List result3 = SubjectDAO.getInstance().findSubjectsByTitle("Tannpleie");
    assertTrue(result3.contains(subject4));
    assertEquals(1, result3.size());
  }

  @Test
  public void testFindSubjectsByTitle4() throws Exception {
    List result4 = SubjectDAO.getInstance().findSubjectsByTitle("Database");
    assertTrue(result4.contains(subject5));
    assertEquals(1, result4.size());
  }

  @Test
  public void testFindSubjectsByCode1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByCode("TDT");
    assertTrue(result1.contains(subject2));
    assertTrue(result1.contains(subject5));
    assertEquals(2, result1.size());
  }

  @Test
  public void testFindSubjectsByCode2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByCode("EXP");
    assertTrue(result2.contains(subject1));
    assertEquals(1, result2.size());
  }

  @Test
  public void testFindSubjectsByCode3() throws Exception {
    List result3 = SubjectDAO.getInstance().findSubjectsByCode("T");
    assertTrue(result3.contains(subject2));
    assertTrue(result3.contains(subject4));
    assertTrue(result3.contains(subject5));
    assertEquals(3, result3.size());
  }

  @Test
  public void testFindSubjectsByCode4() throws Exception {
    List result4 = SubjectDAO.getInstance().findSubjectsByCode("INF");
    assertTrue(result4.contains(subject3));
    assertEquals(1, result4.size());
  }

  @Test
  public void testFindSubjectsByInstitution1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByInstituton("NTNU");
    assertTrue(result1.contains(subject1));
    assertTrue(result1.contains(subject2));
    assertTrue(result1.contains(subject5));
    assertEquals(3, result1.size());
  }

  @Test
  public void testFindSubjectsByInstitution2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByInstituton("UIO");
    assertTrue(result2.contains(subject3));
    assertTrue(result2.contains(subject4));
    assertEquals(2, result2.size());
  }

  @Test
  public void testFindSubjectsByInstitutionAndCode1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByInstitutionAndCode("NTNU", "TDT");
    assertTrue(result1.contains(subject2));
    assertTrue(result1.contains(subject5));
    assertEquals(2, result1.size());
  }

  @Test
  public void testFindSubjectsByInstitutionAndCode2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByInstitutionAndCode("NTNU", "EXP");
    assertTrue(result2.contains(subject1));
    assertEquals(1, result2.size());
  }

  @Test
  public void testFindSubjectsByInstitutionAndCode3() throws Exception {
    List result3 = SubjectDAO.getInstance().findSubjectsByInstitutionAndCode("UIO", "TP");
    assertTrue(result3.contains(subject4));
    assertEquals(1, result3.size());
  }

  @Test
  public void testFindSubjectsByInstitutionAndCode4() throws Exception {
    List result4 = SubjectDAO.getInstance().findSubjectsByInstitutionAndCode("UIO", "INF");
    assertTrue(result4.contains(subject3));
    assertEquals(1, result4.size());
  }

  @Test
  public void testFindSubjectsByTopic1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByTopic(topic1);
    assertTrue(result1.contains(subject2));
    assertTrue(result1.contains(subject3));
    assertTrue(result1.contains(subject5));
    assertEquals(3, result1.size());
  }

  @Test
  public void testFindSubjectsByTopic2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByTopic(topic2);
    assertTrue(result2.contains(subject1));
    assertEquals(1, result2.size());
  }

  @Test
  public void testFindSubjectsByTopic3() throws Exception {
    List result3 = SubjectDAO.getInstance().findSubjectsByTopic(topic3);
    assertTrue(result3.contains(subject4));
    assertEquals(1, result3.size());
  }

  @Test
  public void testFindSubjectsByEditor1() throws Exception {
    List result1 = SubjectDAO.getInstance().findSubjectsByEditor(user1);
    assertTrue(result1.contains(subject1));
    assertTrue(result1.contains(subject4));
    assertEquals(2, result1.size());
  }

  @Test
  public void testFindSubjectsByEditor2() throws Exception {
    List result2 = SubjectDAO.getInstance().findSubjectsByEditor(user2);
    assertTrue(result2.contains(subject2));
    assertTrue(result2.contains(subject5));
    assertEquals(2, result2.size());
  }

  @Test
  public void testFindSubjectsByEditor3() throws Exception {
    List result3 = SubjectDAO.getInstance().findSubjectsByEditor(user3);
    assertTrue(result3.contains(subject3));
    assertEquals(1, result3.size());
  }

}
