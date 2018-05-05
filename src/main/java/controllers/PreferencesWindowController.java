package controllers;

import dao.OfflineDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.MainApp;
import models.Setting;
import services.OfflineService;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PreferencesWindowController is controller class of Preferences window.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class PreferencesWindowController {

    /**
     * Offline DAO.
     */
    private OfflineDao dao;

    /**
     * Offline service of Offline DAO.
     */
    private OfflineService service;

    /**
     * ComboBox of connection type (http or https).
     */
    @FXML
    private ComboBox connectType;

    /**
     * Label of error message.
     */
    @FXML
    private Label errormessage;

    /**
     * TextField of host.
     */
    @FXML
    private TextField host;

    /**
     * TextField of database.
     */
    @FXML
    private TextField database;

    /**
     * TextField of username to access host.
     */
    @FXML
    private TextField username;

    /**
     * TextField of password to access host.
     */
    @FXML
    private TextField password;

    /**
     * TextField of network checking period time.
     */
    @FXML
    private TextField checknetworkperiod;

    /**
     * TextField of autosync period time.
     */
    @FXML
    private TextField syncdbperiod;

    /**
     * Button of save settings.
     */
    @FXML
    private Button saveButton;

    /**
     * Button of close settings dialog.
     */
    @FXML
    public Button cancelBtn;

    /**
     * Initialize is first method what runs automatically when the gui components is loaded.
     */
    @FXML
    private void initialize() {

        try {
            connectType.getItems().add("http://");
            connectType.getItems().add("https://");
            connectType.setValue(dao.getPreference_by_key("connected_type").getConfigValue());
            host.setText(dao.getPreference_by_key("remote_db_host").getConfigValue());
            database.setText(dao.getPreference_by_key("remote_database").getConfigValue());
            username.setText(dao.getPreference_by_key("remote_db_user").getConfigValue());
            password.setText(dao.getPreference_by_key("remote_db_password").getConfigValue());

            int checkconnectionperiod = Integer.valueOf(dao.getPreference_by_key("check_connection_period").getConfigValue())/1000;
            checknetworkperiod.setText(String.valueOf(checkconnectionperiod));

            int autosyncperiod = Integer.valueOf(dao.getPreference_by_key("autosync_period").getConfigValue())/1000;
            syncdbperiod.setText(String.valueOf(autosyncperiod));

        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }

    }

    /**
     * Controller of PreferencesWindowController's class.
     */
    public PreferencesWindowController() {
        try {
            dao = new OfflineDao();
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
        service = new OfflineService(dao);
    }

    /**
     * This method is storing settings in the local SqlLite database when the user clicked to the modify button.
     * @param event is left mouse click event on modify button on preferences dialog.
     */
    public void saveconnectionBtnLeftMouseClick(ActionEvent event){

        List<Setting> settings = new ArrayList<>();

        Setting mysqlHostType = new Setting("connected_type", connectType.getValue().toString());
        settings.add(mysqlHostType);

        Setting mysqlHost = new Setting("remote_db_host", host.getText());
        settings.add(mysqlHost);

        Setting mysqlDatabase = new Setting("remote_database", database.getText());
        settings.add(mysqlDatabase);

        Setting mysqlUser = new Setting("remote_db_user", username.getText());
        settings.add(mysqlUser);
        Setting mysqlPassword = new Setting("remote_db_password", password.getText());
        settings.add(mysqlPassword);

        boolean numberFieldIsValidated = isValidNumberField(checknetworkperiod.getText()) && isValidNumberField(syncdbperiod.getText());

        MainApp.logger.info("Validating period of network checking...");
        if(isValidNumberField(checknetworkperiod.getText())) {
            Setting checkConnectionPeriod = new Setting("check_connection_period", String.valueOf(Integer.valueOf(checknetworkperiod.getText()) * 1000));
            settings.add(checkConnectionPeriod);
            MainApp.logger.info("Period of network checking is validated...");
        }
        else{
            errormessage.setText("A kapcsolat ellenőrzési periódusához nem számot adott meg!");
            MainApp.logger.info("Period of network checking field is not number");
        }

        MainApp.logger.info("Validating period of autosynchronizing...");
        if(isValidNumberField(syncdbperiod.getText())) {
            Setting autosyncPeriod = new Setting("autosync_period", String.valueOf(Integer.valueOf(syncdbperiod.getText()) * 1000));
            settings.add(autosyncPeriod);
            MainApp.logger.info("Period of autosynchronizing is validated...");
        }
        else{
            errormessage.setText("Az autoszinkronizálás időintervallumához nem számot adott meg!");
            MainApp.logger.info("Period of autosynchronizing field is not number");
        }

        if(numberFieldIsValidated) {
            try {
                service.savePreferences(settings);
                errormessage.setText("A módosítások sikeresen rögzítve!");
            } catch (SQLException e) {
                MainApp.logger.error("Source of error: " + e.getMessage());
            }
        }
    }


    /**
     * This method validate the network checking and authosync fields.
     * If the field is not number, showing an error message for the user in the dialog panel.
     * else the field storing in the local SqlLite database.
     * @param textContent is a string of network checking period and autosync period fields.
     * @return boolean, true if the parameter is number, and false if the parameter is not number.
     */
    public static boolean isValidNumberField(String textContent){
        boolean isValid = false;
        try{
            int d = Integer.valueOf(textContent);
            isValid = true;
        }
        catch(NumberFormatException e){
            isValid = false;
        }
        return isValid;
    }
}
