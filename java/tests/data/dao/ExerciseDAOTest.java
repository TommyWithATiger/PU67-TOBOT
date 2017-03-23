package data.dao;

import base.BaseTest;
import data.Topic;
import data.exercise.Exercise;
import data.exerciseattempthistory.ExerciseAttemptHistory;
import data.exerciserating.ExerciseRating;
import data.exerciserating.ExerciseRatingEnum;
import data.user.User;
import org.junit.Before;

public class ExerciseDAOTest extends BaseTest{

  private static User user1;
  private static User user2;

  private static Topic topic1;
  private static Topic topic2;

  private static Exercise exercise1;
  private static Exercise exercise2;

  private static ExerciseRating exerciseRating11;
  private static ExerciseRating exerciseRating21;
  private static ExerciseRating exerciseRating12;
  private static ExerciseRating exerciseRating22;

  private static ExerciseAttemptHistory eah11;
  private static ExerciseAttemptHistory eah12;
  private static ExerciseAttemptHistory eah21;
  private static ExerciseAttemptHistory eah22;

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

    exerciseRating11 = new ExerciseRating(user1.getId(), exercise1.getId(), ExerciseRatingEnum.Easy);
    exerciseRating21 = new ExerciseRating(user2.getId(), exercise1.getId(), ExerciseRatingEnum.Hard);
    exerciseRating11.create();
    exerciseRating21.create();

    topic1 = new Topic("Programming", "Make the computer do stuff");
    topic2 = new Topic("Philosophy 101", "Think and argue");
    topic1.addExercise(exercise1);
    topic1.addExercise(exercise2);
    topic2.addExercise(exercise1);
    topic2.addExercise(exercise2);
    topic1.create();
    topic2.create();

    eah11 = new ExerciseAttemptHistory(user1, exercise1, false);
    eah12 = new ExerciseAttemptHistory(user1, exercise2, false);
    eah21 = new ExerciseAttemptHistory(user2, exercise1, true);
    eah22 = new ExerciseAttemptHistory(user2, exercise2, true);

    eah11.create();
    eah12.create();
    eah21.create();
    eah22.create();

  }

}
