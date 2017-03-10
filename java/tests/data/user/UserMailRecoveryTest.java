package data.user;

import static org.junit.Assert.*;

import base.BaseTest;
import data.dao.UserDAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import org.junit.Before;
import org.junit.Test;

public class UserMailRecoveryTest extends BaseTest {

  private User user;
  private Store store;

  @Before
  public void setup() throws MessagingException, IOException {

    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.store.protocol", "imaps");

    List<String> loginDetails = null;
    loginDetails = Files
        .readAllLines(Paths.get(System.getProperty("user.dir") + "/src/META-INF/mail_login.txt"));

    String username = loginDetails.get(0);
    String password = loginDetails.get(1);

    store = Session.getInstance(props,
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        }).getStore("imaps");
    store.connect("imap.gmail.com", username, password);

    user = new User("Username", username, "Password");
    user.create();
  }

  @Test
  public void testSendRecoveryMail() throws IOException, MessagingException {
    UserMailRecovery.getInstance().sendRecoveryMail(user);

    User user = UserDAO.getInstance().findUserByUsername("Username");

    Folder folder = store.getFolder("INBOX");
    folder.open(Folder.READ_WRITE);

    Message message = folder.getMessage(folder.getMessageCount());
    String resetToken = message.getContent().toString().split(
        "Someone requested a password reset for your TOBOT account. Your reset link http:\\/\\/localhost:5032\\/reset\\/\\?token=")[1]
        .substring(0, 20).replace("\r", "").replace("\n", "");

    // Cleanup
    message.setFlag(Flag.DELETED, true);
    folder.close(true);

    assertTrue(user.checkPasswordResetToken(resetToken));

  }

}