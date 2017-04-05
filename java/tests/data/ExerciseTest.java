package data;

import static org.junit.Assert.assertEquals;

import base.BaseTest;
import data.dao.ExerciseDAO;
import data.exercise.Exercise;
import org.junit.Test;

public class ExerciseTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    Exercise exercise1 = ExerciseDAO.getInstance().findById(exercise.getId());
    assertEquals(exercise, exercise1);
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

}
