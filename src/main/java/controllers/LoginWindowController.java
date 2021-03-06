package controllers;

import dao.MysqlDao;
import dao.OfflineDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.MainApp;
import org.bouncycastle.util.encoders.Hex;
import services.MysqlService;
import services.OfflineService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import static main.MainApp.*;

/**
 * LoginViewController is controller class of Login window.
 * In this method the login service can communicate with the login view.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class LoginWindowController {

    /** Applicants number.
     * Counted number of applicants in remoted database.
     */
    public static int countedMysqlApplicants;

    /** Username input field.
     * TextField of username of user.
     */
    @FXML
    private TextField userName;

    /** Password input field.
     * TextField of password of user.
     */
    @FXML
    private PasswordField password;

    /** Login button.
     * Login button on login panel.
     */
    @FXML
    private Button loginBtn;

    /** Label of login message.
     * Error or success messagebox in the login panel.
     */
    @FXML
    private Label loginMessage;

    /** Autorun method.
     * This method first running automatic, when loading the form.
     */
    @FXML
    protected void initialize(){
    }

    /** Constructor.
     * Constructor of LoginWindowController.
     */
    public LoginWindowController() {
        countedMysqlApplicants = 0;
    }

    /** Send user authentication by left mouse click event.
     * This method runs when the user has clicked the login button by mouse's left button.
     * checkUser method calls the checkLogin method to authenticate user.
     * @param event is mouse left click on login button.
     */
    public void checkUser(ActionEvent event) {
        MainApp.logger.info("Authenticating user...");
        try {
            checkLogin();
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /** Password encrypter method.
     * encryptPassword is encoding method from string to SHA-256.
     * @param password is password of user.
     * @return SHA-256 encoded string.
     */
    static public String encryptPassword(String password){
        MessageDigest digest = null;
        try {
            MainApp.logger.info("Encoding user password...");
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        byte[] hash = digest.digest(
        password.getBytes(StandardCharsets.UTF_8));
        String sha256hex = new String(Hex.encode(hash));
        return sha256hex;
    }

    /** Send user authentication by enter key pressing event.
     * Basically same method as checkUser, this method runs when the user pressing enter key on login form.
     * This method call checkLogin method to authenticate user.
     * @param keyevent is user press ENTER on keyboard on login panel.
     */
    public void enterKeyPressed(KeyEvent keyevent){
        MainApp.logger.info("Checking user name...");
            if (keyevent.getCode().equals(KeyCode.ENTER)){
                try {
                    checkLogin();
                } catch (SQLException e) {
                    MainApp.logger.error("Source of error: " + e.getMessage());
                }
            }
    }

    /** User authentication method.
     * Checklogin method autheticate the user by username and password.
     * If the authentication is succeed, calling the home window and set the actual user.
     * else the user getting an error message with "Authentication error" message.
     * @throws SQLException if it has any SQL problem in database.
     */
     private void checkLogin() throws SQLException {
        OfflineDao sqlLiteDao = null;
        try {
            sqlLiteDao = new OfflineDao();
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        OfflineService sqlLiteService = new OfflineService(sqlLiteDao);

         if(sqlLiteService.userLoggedIn(userName.getText(), password.getText())){
             MainApp.actualUser = sqlLiteService.getUserByUsername(userName.getText());
             loginMessage.setText("");
             userName.setText("");
             password.setText("");
             SetActiveWindow(MainApp.homeWindow);
             MainApp.logger.info("User authentication is succeed, loading home window...");
         }
         else{
             loginMessage.setText("A felhasználónév vagy a jelszó hibás!");
             MainApp.logger.info("User login is denied...");
         }

    }
}
