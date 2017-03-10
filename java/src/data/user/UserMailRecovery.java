package data.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UserMailRecovery {

  private Session session;
  private String username;
  private static UserMailRecovery instance;

  private UserMailRecovery() {
    session = setupSession();
  }

  private Session setupSession() {
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    List<String> loginDetails = null;
    try {
      loginDetails = Files
          .readAllLines(Paths.get(System.getProperty("user.dir") + "/src/META-INF/mail_login.txt"));
    } catch (IOException e) {
      System.exit(1);
    }

    username = loginDetails.get(0);
    String password = loginDetails.get(1);

    return Session.getInstance(props,
        new Authenticator() {
          @Override
          protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
          }
        });
  }


  public static UserMailRecovery getInstance() {
    if (instance == null) {
      instance = new UserMailRecovery();
    }
    return instance;
  }

  public void sendRecoveryMail(User user) {
    try {
      String passwordResetToken = user.generatePasswordResetToken();
      user.update();

      Message message = new MimeMessage(session);

      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
      message.setSubject("Requested password recovery from TOBOT");
      message.setText("Someone requested a password reset for your TOBOT account. Your reset link http://localhost:5032/reset/?token=" + passwordResetToken);

      Transport.send(message);
    } catch (MessagingException me) {
      me.printStackTrace();
      System.out.println("Failure");
    }
  }

}
