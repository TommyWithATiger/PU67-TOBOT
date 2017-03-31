package data.exerciserating;

import static org.junit.Assert.assertEquals;

import base.BaseTest;
import data.dao.ExerciseRatingDAO;
import data.exercise.Exercise;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.exerciserating.ExerciseRatingKey;
import data.user.User;
import org.junit.Before;
import org.junit.Test;

public class ExerciseRatingTest extends BaseTest {

  private User user;
  private Exercise exercise;
  private ExerciseRatingKey id;

  @Before
  public void populate(){
    user = new User("test", "test@test.com", "password");
    exercise = new Exercise("Test", "Test");
    user.create();
    exercise.create();
    id = new ExerciseRatingKey(user, exercise);
  }

  @Test
  public void testCreate() throws Exception {
    ExerciseRating exerciserating = new ExerciseRating(user, exercise, ExerciseRatingEnum.Hard);
    exerciserating.create();

    ExerciseRating exerciserating1 = ExerciseRatingDAO.getInstance().findById(id);
    assertEquals(exerciserating, exerciserating1);
  }

  @Test
  public void testDelete() throws Exception {
    ExerciseRating exerciserating = new ExerciseRating(user, exercise, ExerciseRatingEnum.Hard);
    exerciserating.create();

    exerciserating.delete();

    ExerciseRating exerciserating1 = ExerciseRatingDAO.getInstance().findById(id);
    assertEquals(null, exerciserating1);
  }

  @Test
  public void testUpdate() throws Exception {
    ExerciseRating exerciserating = new ExerciseRating(user, exercise, ExerciseRatingEnum.Hard);
    exerciserating.create();

    exerciserating.setExerciseRating(ExerciseRatingEnum.Easy);
    exerciserating.update();

    ExerciseRating exerciserating1 = ExerciseRatingDAO.getInstance().findById(id);
    assertEquals(ExerciseRatingEnum.Easy.value(), exerciserating1.getRating(), 0.0001);
  }

}
