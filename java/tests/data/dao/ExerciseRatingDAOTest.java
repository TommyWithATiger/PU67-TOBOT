package data.dao;


import static junit.framework.TestCase.assertEquals;

import base.BaseTest;
import data.exercise.Exercise;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.exerciserating.ExerciseRatingKey;
import data.user.User;
import org.junit.Before;
import org.junit.Test;

public class ExerciseRatingDAOTest extends BaseTest{

  private static User user;

  private static Exercise exercise1;
  private static Exercise exercise2;

  @Before
  public void populate(){
    user = new User("John", "john@email.com", "hunter2");
    user.create();

    exercise1 = new Exercise("Exercise 1", "Do stuff");
    exercise2 = new Exercise("Exercise 2", "Do other stuff");
    exercise1.create();
    exercise2.create();

    ExerciseRating exerciseRating11 = new ExerciseRating(user.getId(), exercise1.getId(), ExerciseRatingEnum.Easy);
    exerciseRating11.create();

  }

  @Test
  public void testCreateOrUpdate1() {
    ExerciseRatingDAO.getInstance().createOrUpdate(user, exercise1, ExerciseRatingEnum.Hard);
    assertEquals(ExerciseRatingEnum.Medium, ExerciseRatingEnum.get(ExerciseRatingDAO.getInstance()
        .findById(new ExerciseRatingKey(user, exercise1)).getRating()));

    ExerciseRatingDAO.getInstance().createOrUpdate(user, exercise2, ExerciseRatingEnum.Easy);
    assertEquals(ExerciseRatingEnum.Easy, ExerciseRatingEnum.get(ExerciseRatingDAO.getInstance()
        .findById(new ExerciseRatingKey(user, exercise2)).getRating()));
  }
}
