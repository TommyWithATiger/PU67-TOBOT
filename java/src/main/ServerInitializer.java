package main;

import data.DataAccessObjects.SubjectDAO;
import data.DataAccessObjects.TopicDAO;
import data.DataAccessObjects.UserDAO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import server.connection.SocketHandler;

public class ServerInitializer {

  public static void main(String[] args) throws InterruptedException {

    EntityManagerFactory entityManagerFactory = setup();

    // Place all setup before this call, this will run forever
    SocketHandler server = new SocketHandler();
    server.start();

    Thread.sleep(Long.MAX_VALUE);

    server.stopServer();
    entityManagerFactory.close();

  }

  public static EntityManagerFactory setup(){
    System.setProperty("javax.xml.accessExternalDTD", "all");

    // Should only make one EntityManagerFactory, and this is the one
    // see: http://stackoverflow.com/a/4544053
    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Eclipselink_JPA");

    TopicDAO topicDAO = TopicDAO.getInstance(entityManagerFactory);
    SubjectDAO subjectDAO = SubjectDAO.getInstance(entityManagerFactory);
    UserDAO userDAO = UserDAO.getInstance(entityManagerFactory);

    // need to return in order to close after server is shut down
    return entityManagerFactory;
  }
}
