package data.dao;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import base.BaseTest;
import data.Topic;
import data.exercise.Exercise;
import data.exerciseattempthistory.ExerciseAttemptHistory;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.user.User;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ExerciseDAOTest extends BaseTest{

  private static User user1;
  private static User user2;

  private static Topic topic1;
  private static Topic topic2;

  private static Exercise exercise1;
  private static Exercise exercise2;

  @Before
  public void populate(){
    user1 = new User("John", "john@email.com", "hunter2");
    user2 = new User("Jane", "jane@email.com", "hunter2");
    user1.create();
    user2.create();

    exercise1 = new Exercise("Exercise 1", "Do stuff");
    exercise2 = new Exercise("Exercise 2", "Do other stuff");
    exercise1.create();
    exercise2.create();

    ExerciseRating exerciseRating11 = new ExerciseRating(user1.getId(), exercise1.getId(), ExerciseRatingEnum.Easy);
    ExerciseRating exerciseRating21 = new ExerciseRating(user2.getId(), exercise1.getId(), ExerciseRatingEnum.Hard);
    exerciseRating11.create();
    exerciseRating21.create();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy 101", "Think and argue");
    topic1.addExercise(exercise1);
    topic1.addExercise(exercise2);
    topic2.addExercise(exercise2);
    topic1.create();
    topic2.create();

    ExerciseAttemptHistory eah11 = new ExerciseAttemptHistory(user1, exercise1, false);
    ExerciseAttemptHistory eah12 = new ExerciseAttemptHistory(user1, exercise2, false);
    ExerciseAttemptHistory eah21 = new ExerciseAttemptHistory(user2, exercise1, true);
    ExerciseAttemptHistory eah22 = new ExerciseAttemptHistory(user2, exercise2, false);

    eah11.create();
    eah12.create();
    eah21.create();
    eah22.create();

  }

  @Test
  public void testGetNextExercises(){
    List<Exercise> exercises = ExerciseDAO.getInstance().getNextExercises(user1, topic1, 1000);
    assertEquals(exercise1, exercises.get(0));
    assertEquals(exercise2, exercises.get(1));
    assertEquals(2, exercises.size());

    exercises = ExerciseDAO.getInstance().getNextExercises(user2, topic1, 1000);
    assertEquals(exercise2, exercises.get(0));
    assertEquals(exercise1, exercises.get(1));
    assertEquals(2, exercises.size());

    exercises = ExerciseDAO.getInstance().getNextExercises(user1, topic2, 1000);
    assertEquals(exercise2, exercises.get(0));
    assertEquals(1, exercises.size());

    exercises = ExerciseDAO.getInstance().getNextExercises(user2, topic2, 1000);
    assertEquals(exercise2, exercises.get(0));
    assertEquals(1, exercises.size());

    assertEquals(1, ExerciseDAO.getInstance().getNextExercises(user1, topic1, 1).size());
  }

  @Test
  public void testFindExerciseByTopic(){
    List<Exercise> exercises = ExerciseDAO.getInstance().findExerciseByTopic(topic1);
    assertTrue(exercises.contains(exercise1));
    assertTrue(exercises.contains(exercise2));
    assertEquals(2, exercises.size());

    exercises = ExerciseDAO.getInstance().findExerciseByTopic(topic2);
    assertTrue(exercises.contains(exercise2));
    assertEquals(1, exercises.size());
  }

}
