package data;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import base.BaseTest;
import data.dao.ExerciseAttemptHistoryDAO;
import data.exercise.Exercise;
import data.exerciseattempthistory.ExerciseAttemptHistory;
import data.user.User;
import java.util.Date;
import org.junit.Test;

public class ExerciseAttemptHistoryTest extends BaseTest {

  @Test
  public void testCreate() throws Exception {
    User user = new User("username", "email@email.com", "password");
    user.create();
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();

    ExerciseAttemptHistory exerciseAttemptHistory = new ExerciseAttemptHistory(user, exercise,
        true);
    exerciseAttemptHistory.create();

    ExerciseAttemptHistory exerciseAttemptHistory1 = ExerciseAttemptHistoryDAO.getInstance()
        .findById(exerciseAttemptHistory.getId());
    assertEquals(exerciseAttemptHistory, exerciseAttemptHistory1);
    assertEquals(exercise, exerciseAttemptHistory1.getExercise());
    assertEquals(user, exerciseAttemptHistory1.getUser());
    assertEquals(true, exerciseAttemptHistory1.wasSuccess());
    assertTrue(exerciseAttemptHistory1.getAttemptDate().before(new Date()));
  }

  @Test
  public void testDelete() throws Exception {
    User user = new User("username", "email@email.com", "password");
    user.create();
    Exercise exercise = new Exercise("Title", "<html stuff/>", "<html stuff solution/>");
    exercise.create();
    ExerciseAttemptHistory exerciseAttemptHistory = new ExerciseAttemptHistory(user, exercise,
        true);
    exerciseAttemptHistory.create();

    exerciseAttemptHistory.delete();

    assertNull(ExerciseAttemptHistoryDAO.getInstance().findById(exerciseAttemptHistory.getId()));
  }

}
