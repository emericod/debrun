package controllers;

import dao.OfflineDao;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.OfflineService;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * StatusBarController is controller class of Statusbar on Home window.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class StatusBarController{
    /** Network status variable.
     * SimpleStringProperty of network status.
     */
    public SimpleStringProperty netw;

    /** Network status label in GUI.
     * Label of network status.
     */
    @FXML
    private Label networkstatus;

    /** Applicant counter in GUI.
     * Label of applicant counter.
     */
    @FXML
    private Label applicantCounter;

    /** The today label.
     * Label of right bottom.
     */
    @FXML
    private Label labelRight;

    /** Autorun method.
     * Initialize is first method what runs automatically when the gui components are loaded.
     */
    @FXML
    private void initialize() {
        LocalDate today = LocalDate.now() ;
        labelRight.setText(today.toString());
        OfflineDao dao = null;
        try {
            dao = new OfflineDao();
            OfflineService service = new OfflineService(dao);
            applicantCounter.setText("Összesen " + service.countSqlLiteApplicants() + " nevező");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Newtwork status changer method.
     * This method runs when the network status has changed.
     * This change the network connection status by the parameter.
     * @param content is network status text content ("Kapcsolódva" or "Nincs kapcsolat").
     */
    public void changeStatus(String content){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                networkstatus.setText(content);
            }
        });
    }

    /** Setter.
     * Setter of StatusBarNetwork.
     * @param statusBarNetworkStatus is network status text content ("Kapcsolódva" or "Nincs kapcsolat").
     */
    public void setStatusBarNetworkStatus(String statusBarNetworkStatus) {
        if(netw == null){
            this.netw = new SimpleStringProperty("");
        }
        netw.set(statusBarNetworkStatus);
    }
}
