package data;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import base.BaseTest;
import data.dao.ExerciseDAO;
import data.exercise.Exercise;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONObject;
import org.junit.Test;

public class ExerciseTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    Exercise exercise1 = ExerciseDAO.getInstance().findById(exercise.getId());
    assertEquals(exercise, exercise1);
    assertEquals(exercise.getText(), "<html stuff/>");
    assertEquals(exercise.getSolution(), "<html stuff solution/>");
    assertEquals(exercise.getTitle(), "Title");
  }

  @Test
  public void testDelete() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    exercise.delete();

    Exercise exercise1 = ExerciseDAO.getInstance().findById(exercise.getId());
    assertEquals(null, exercise1);
  }

  @Test
  public void testUpdate() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    exercise.setSolution("<html stuff new solution/>");
    exercise.update();

    Exercise exercise1 = ExerciseDAO.getInstance().findById(exercise.getId());
    assertEquals("<html stuff new solution/>", exercise1.getSolution());
  }

  @Test
  public void testAddToTopics() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    Topic topic1 = new Topic("Title1", "description1");
    topic1.create();
    Topic topic2 = new Topic("Title2", "description2");
    topic2.create();
    List<Topic> topics = new ArrayList<>();
    topics.add(topic1);
    topics.add(topic2);

    exercise.addToTopics(topics);
    exercise.update();

    Exercise exercise1 = ExerciseDAO.getInstance().findById(exercise.getId());
    assertTrue(CollectionUtils.isEqualCollection(topic1.getExercises(), Collections.singletonList(exercise1)));
    assertTrue(CollectionUtils.isEqualCollection(topic2.getExercises(), Collections.singletonList(exercise1)));
  }


  @Test
  public void testCreateAbout() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    Topic topic1 = new Topic("Title1", "description1");
    topic1.create();
    Topic topic2 = new Topic("Title2", "description2");
    topic2.create();
    List<Topic> topics = new ArrayList<>();
    topics.add(topic1);
    topics.add(topic2);
    exercise.addToTopics(topics);

    JSONObject jsonExercise = ExerciseDAO.getInstance().findById(exercise.getId()).createAbout();
    assertTrue(jsonExercise.has("id"));
    assertEquals("Title", jsonExercise.getString("title"));
    assertEquals("<html stuff/>", jsonExercise.getString("text"));
    assertEquals("<html stuff solution/>", jsonExercise.getString("solution"));
    assertEquals(2, jsonExercise.getJSONArray("tags").length());
  }

}
