package data.dao;

import data.Topic;
import data.dao.util.FieldTuple;
import data.exercise.Exercise;
import data.user.User;
import java.util.List;
import javax.persistence.EntityManagerFactory;

public class ExerciseDAO extends AbstractBaseDAO<Exercise, Integer> {

  private static ExerciseDAO instance;

  /**
   * Instantiates the ExerciseDataAccessObject
   *
   * @param emFactory, the static EntityManagerFactory for the server instance
   */
  private ExerciseDAO(EntityManagerFactory emFactory) {
    super(Exercise.class, emFactory);
  }

  /**
   * Returns the exercises related to a topic
   *
   * @param topic, the Topic to find exercises for
   * @return a list of related exercises
   */
  public List<Exercise> findExerciseByTopic(Topic topic) {
    return super.find("findExerciseByTopic",
        new FieldTuple("topic", topic));
  }

  /**
   * Returns the next n exercises to do for a user, in a topic
   *
   * @param user, the User to find exercises for
   * @param topic, the Topic to find exercises for
   * @param limit, the maximum number of exercises to get
   * @return a list of exercises to do
   */
  public List<Exercise> getNextExercises(User user, Topic topic, int limit) {
    return super.findLimited("getNextExercises", limit,
        new FieldTuple("user", user),
        new FieldTuple("topic", topic));
  }

  /**
   * Returns the static ExerciseDAO instance
   *
   * @return RatingDAO, null if not instantiated
   */
  public static ExerciseDAO getInstance() {
    return instance;
  }

  /**
   * Creates the static ExerciseDAO instance
   *
   * @param emFactory, the static EntityManagerFactory of this server instance
   */
  public static void initialize(EntityManagerFactory emFactory) {
    if (instance == null || !ExerciseDAO.emFactory.isOpen()) {
      instance = new ExerciseDAO(emFactory);
    }
  }

}
