package data;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.DataAccessObjects.SubjectDAO;
import data.user.User;
import data.user.UserType;
import java.util.List;
import org.junit.Test;

public class SubjectTest extends BaseTest {

  @Test
  public void testMakeSubject() {
    Subject subject1 = new Subject();
    subject1.setTitle("Math");
    subject1.setDescription("From algebra to calculus.");
    Subject subject2 = new Subject("English for beginners", "blah, blah");
    Subject subject3 = new Subject("Piano", "Musical Academy", "MUS1001",
        "Classical piano lessons");

    assertNotNull(subject1);
    assertEquals("Math", subject1.getTitle());
    assertEquals("From algebra to calculus.", subject1.getDescription());

    assertNotNull(subject2);
    assertEquals("English for beginners", subject2.getTitle());
    assertEquals("blah, blah", subject2.getDescription());

    assertNotNull(subject2);
    assertEquals("Piano", subject3.getTitle());
    assertEquals("Musical Academy", subject3.getInstitution());
    assertEquals("MUS1001", subject3.getSubjectCode());
    assertEquals("Classical piano lessons", subject3.getDescription());

  }

  @Test
  public void testGetSetTitle() {
    Subject subject = new Subject("Math", "From algebra to calculus.");

    assertEquals("Math", subject.getTitle());
    assertNotSame("English", subject.getTitle());

    subject.setTitle("English");

    assertNotSame("Math", subject.getTitle());
    assertEquals("English", subject.getTitle());
  }

  @Test
  public void testGetSetDescription() {
    Subject subject = new Subject("Math", "From algebra to calculus.");

    assertEquals("From algebra to calculus.", subject.getDescription());
    assertNotSame("Piano lessons.", subject.getDescription());

    subject.setDescription("blah, blah");

    assertNotSame("From algebra to calculus.", subject.getDescription());
    assertEquals("blah, blah", subject.getDescription());
  }

  @Test
  public void testGetSetInstitution() {
    Subject subject = new Subject("Piano", "Musical Academy", "MUS1001", "Classical piano lessons");

    assertEquals("Musical Academy", subject.getInstitution());
    assertNotSame("Chefs school", subject.getInstitution());

    subject.setInstitution("blah, blah");

    assertNotSame("Musical Academy", subject.getInstitution());
    assertEquals("blah, blah", subject.getInstitution());
  }

  @Test
  public void testGetSetSubjectCode() {
    Subject subject = new Subject("Piano", "Musical Academy", "MUS1001", "Classical piano lessons");

    assertEquals("MUS1001", subject.getSubjectCode());
    assertNotSame("TDT4140", subject.getSubjectCode());

    subject.setSubjectCode("MA4100");

    assertNotSame("MUS1001", subject.getSubjectCode());
    assertEquals("MA4100", subject.getSubjectCode());
  }

  @Test
  public void testGetID() {
    Subject subject = new Subject("Math", "From algebra to calculus.");

    assertEquals(0, subject.getId());

  }

  @Test
  public void addToRemoveFromSubject() {
    Topic topic = new Topic("Math", "From algebra to calculus.");
    Subject subject = new Subject("Math 101", "intro course to basic math.");

    subject.addTopic(topic);
    assertTrue(subject.hasTopic(topic));
    assertTrue(subject.getTopics().contains(topic));

    subject.removeTopic(topic);
    assertFalse(subject.hasTopic(topic));
  }

  @Test
  public void testCreate() throws Exception {
    Subject subject = new Subject("subject", "The best subject");

    subject.create();

    Subject test = SubjectDAO.getInstance().findById(subject.getId());

    assertEquals(subject, test);
  }

  @Test
  public void testDelete() throws Exception {
    Subject subject = new Subject("subject", "The best subject");
    subject.create();
    Subject test = SubjectDAO.getInstance().findById(subject.getId());
    assertEquals(subject, test);

    subject.delete();
    test = SubjectDAO.getInstance().findById(subject.getId());
    assertEquals(null, test);
  }

  @Test
  public void testUpdate() throws Exception {
    Subject subject = new Subject("something", "we do stuff");

    subject.create();

    subject.setTitle("other subject");
    subject.update();

    List<Subject> results = SubjectDAO.getInstance().findSubjectsByTitle("other subject");

    assertTrue(results.contains(subject));
  }

  @Test
  public void testEquals() {
    Subject subject = new Subject("subject", "The best subject");
    subject.create();
    Subject subject2 = new Subject("subject2", "The second best subject");
    subject2.create();

    assertNotSame(subject, subject2);
    assertEquals(subject, SubjectDAO.getInstance().findById(subject.getId()));
    assertFalse(subject.equals(5));
    assertFalse(subject.equals(null));
  }

  @Test
  public void testHasCode() {
    Subject subject = new Subject("subject", "The best subject");
    subject.create();

    assertEquals(subject.hashCode(), subject.getId());
  }

  @Test
  public void testGetSetEditor() {
    Subject subject = new Subject("subject", "The best subject");
    subject.create();

    User admin = new User("admin", "admin@mail.com", "root", UserType.ADMIN);
    User editor1 = new User("editor1", "editor1@mail.com", "1234");
    User editor2 = new User("editor2", "editor2@mail.com", "IHateMondays");
    admin.create();
    editor1.create();
    editor2.create();

    subject.addEditor(editor1);
    subject.update();

    assertTrue(subject.isEditor(editor1));
    assertFalse(subject.isEditor(editor2));

    subject.removeEditor(editor1);
    subject.update();
    assertFalse(subject.isEditor(editor1));

    assertTrue(subject.isEditor(admin));

  }

}
