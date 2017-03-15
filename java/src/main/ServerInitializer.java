package main;

import data.dao.ExerciseDAO;
import data.dao.RatingDAO;
import data.dao.SubjectDAO;
import data.dao.TopicDAO;
import data.dao.UserDAO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import server.connection.SocketHandler;

public class ServerInitializer {

  public static void main(String[] args) throws InterruptedException {

    EntityManagerFactory entityManagerFactory = setup("Eclipselink_JPA");

    // Place all setup before this call, this will run forever
    SocketHandler server = new SocketHandler();
    server.start();

    Thread.sleep(Long.MAX_VALUE);

    // All shutdown calls should go here
    server.stopServer();
    entityManagerFactory.close();

  }

  public static EntityManagerFactory setup(String persistenceUnitName){
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // Should only make one EntityManagerFactory, and this is the one
    // see: http://stackoverflow.com/a/4544053
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

    TopicDAO.initialize(entityManagerFactory);
    SubjectDAO.initialize(entityManagerFactory);
    UserDAO.initialize(entityManagerFactory);
    RatingDAO.initialize(entityManagerFactory);
    ExerciseDAO.initialize(entityManagerFactory);

    // need to return in order to close after server is shut down
    return entityManagerFactory;
  }
}
