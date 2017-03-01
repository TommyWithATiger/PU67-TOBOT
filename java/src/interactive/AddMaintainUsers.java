package interactive;

import data.DataAccessObjects.UserDAO;
import data.User;

import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.exceptions.PersistenceUnitLoadingException;

public class AddMaintainUsers {

  /**
   * Interactively create/update/delete Users from the database.
   */
  public static void main(String[] args) {
    System.setProperty("javax.xml.accessExternalDTD", "all");


    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    try {
      EntityManagerFactory entityManagerFactory = Persistence
          .createEntityManagerFactory("Eclipselink_JPA");
      UserDAO.initialize(entityManagerFactory);
      UserDAO userDAO = UserDAO.getInstance();

      System.out.print("Enter R to remove, U to update, anything else to create: ");
      String choice = bufferedReader.readLine();
      if (choice.equals("R")) {
        System.out.print("Enter username to delete: ");
        User user = userDAO.findUserByUsername(bufferedReader.readLine());

        user.delete();
        System.out.println("User removed successfully!");
      } else if (choice.equals("U")) {
        System.out.print("Enter username to update: ");
        User user = userDAO.findUserByUsername(bufferedReader.readLine());
        System.out.print("Enter new username: ");
        user.setUsername(bufferedReader.readLine());
        System.out.print("Enter new email: ");
        user.setEmail(bufferedReader.readLine());
        System.out.print("Enter new password: ");
        user.setPassword(bufferedReader.readLine());

        user.update();
        System.out.println("User updated successfully!");
      } else {
        User user = new User();
        System.out.print("Enter username: ");
        user.setUsername(bufferedReader.readLine());
        System.out.print("Enter email: ");
        user.setEmail(bufferedReader.readLine());
        System.out.print("Enter password: ");
        user.setPassword(bufferedReader.readLine());

        user.create();
        System.out.println("User creation successful!");
        entityManagerFactory.close();
      }
    } catch (PersistenceUnitLoadingException e) {
      System.out.println("Could not load persistence unit");
    } catch (DatabaseException e) {
      System.out.println("Database connection failed");
    } catch (IOException e) {
      System.out.println("Unable to create user");
    }
  }
}
