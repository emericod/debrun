package controllers;

import dao.OfflineDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainApp;
import models.Applicant;
import services.OfflineService;
import java.sql.SQLException;

/** Controller of Applicant dialog.
 * ApplicantDialogController is controller class of applicants's dialog panel.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class ApplicantDialogController {

    /** Applicant ID.
     * Textfield of applicant's ID.
     */
    @FXML
    private TextField applicant_id;

    /** Client name.
     * TextField of applicant's name.
     */
    @FXML
    private TextField clientName;

    /** Applicant status.
     * TextField of applicant's status.
     */
    @FXML
    private ComboBox applicant_status;

    /** Applicant's race start number.
     * TextField of applicant's start number.
     */
    @FXML
    private TextField start_number;

    /** Client gender.
     * TextField of applicant's gender.
     */
    @FXML
    private TextField clientGender;

    /** Applicant tShirt size.
     * TextField of applicant's Tshirt size.
     */
    @FXML
    private TextField tshirtSize;

    /** Applicant birthdate.
     * TextField of applicant's birth year.
     */
    @FXML
    private TextField clientBirthDate;

    /** Email address.
     * TextField of applicant's email address.
     */
    @FXML
    private TextField clientEmail;

    /** Date of registration.
     * TextField of applicants's registration number.
     */
    @FXML
    private TextField registration_date;

    /** Modify button in the applicant's dialog panel.
     * Button of modify applicant's details.
     */
    @FXML
    private Button changeStatusDialog;

    /** Close button in the applicant's dialog panel.
     * Button of close dialog without any changes.
     */
    @FXML
    private Button cancelDialog;

    /** Dialog stage.
     * Stage of applicant's modify dialog.
     */
    private Stage dialogStage;

    /** Selected applicant.
     * Selected applicant in the tableview.
     */
    private Applicant selectedApplicant;

    /** Database Access Object.
     * Offline dao.
     */
    private OfflineDao dao;

    /** Service.
     * Offline service of offline dao.
     */
    private OfflineService service;

    /** Constructor of ApplicantDialogController.
     */
    public ApplicantDialogController(){
        try {
            dao = new OfflineDao();
            service = new OfflineService(dao);
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /** Autorun method.
     * Initialize is first method what runs automatically when the gui components is loaded.
     */
    @FXML
    private void initialize() {
        applicant_status.getItems().add("Beléptetve");
        applicant_status.getItems().add("Még nincs beléptetve");
        MainApp.logger.info("Add applicant statuses to combo box");
    }

    /** Setter of dialogStage.
     *
     * @param dialogStage is applicant details panel.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /** Setter of SelectedApplicant.
     *
     * @param selectedApplicant is selected applicant on tableview of home window.
     */
    public void setSelectedApplicant(Applicant selectedApplicant) {
        this.selectedApplicant = selectedApplicant;
    }

    /** Setter of Applicant.
     *
     * @param applicant is selected applicant.
     */
    public void setApplicant(Applicant applicant) {
        this.selectedApplicant = applicant;
        MainApp.logger.info("Loading applicant form");

        if (selectedApplicant.getApplicant_id() != null) {
            applicant_id.setText(selectedApplicant.getApplicant_id());
        } else {
            applicant_id.setText("Nincs adat");
        }

        if(selectedApplicant.getClientName() != null) {
            clientName.setText(selectedApplicant.getClientName());
        }
        else{
            clientName.setText("Nincs adat");
        }

        if(selectedApplicant.getApplicant_status() == 1){
            applicant_status.setValue("Beléptetve");
        }
        else{
            applicant_status.setValue("Még nincs beléptetve");
        }

        if(selectedApplicant.getStart_number() != 0){
            start_number.setText(Integer.toString(selectedApplicant.getStart_number()));
        }
        else{
            start_number.setText("Nincs rajtszám hozzárendelve");
        }

        if(selectedApplicant.getClientGender() != null){
            clientGender.setText(selectedApplicant.getClientGender());
        }
        else{
            clientGender.setText("Nincs adat");
        }

        if(selectedApplicant.getTshirtSize() != null){
            tshirtSize.setText(selectedApplicant.getTshirtSize());
        }
        else{
            tshirtSize.setText("Nincs adat");
        }

        if(Integer.toString(selectedApplicant.getClientBirthDate()) != null){
            clientBirthDate.setText(Integer.toString(selectedApplicant.getClientBirthDate()));
        }
        else{
            clientBirthDate.setText("Nincs adat");
        }

        if(selectedApplicant.getClientEmail().compareTo("null") != 0){
            clientEmail.setText(selectedApplicant.getClientEmail());
        }
        else{
            clientEmail.setText("Nincs adat");
        }

        if(selectedApplicant.getRegistration_date() != null){
            registration_date.setText(selectedApplicant.getRegistration_date().toString());
        }
        else{
            registration_date.setText("Nincs adat");
        }
    }

    /** Applicant status updater.
     * This method update applicant if any property field has changed.
     * @param event is mouse left click on modify button of applicant dialog box.
     */
    public void updateApplicantStatus(ActionEvent event){
        int status;
        if (applicant_status.getValue().equals("Beléptetve")) status = 1;
        else status = 0;
        selectedApplicant.setApplicant_status(status);
        MainApp.logger.info("Refreshing " + selectedApplicant.getClientName() + "'s properties ");
        try {
            service.updateApplicant(selectedApplicant.getApplicant_id(),status);
            this.dialogStage.close();
        } catch (SQLException e) {
            MainApp.logger.error("Source of error: " + e.getMessage());
        }
    }

    /** Closing form by left mouse click event.
     * Close dialog without saving.
     * @param event is mouse left button click on button.
     */
    public void cancelDialog(ActionEvent event){
        this.dialogStage.close();
    }
}
