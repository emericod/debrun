package models;

import org.hibernate.annotations.SQLUpdate;
import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Applicant is class of applicant's the model class
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
@Entity
@Table(name = "debrun_applicants")
public class Applicant {
    private LocalDateTime modified;
    private String applicantStatusString;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "start_number")
    @Basic
    private int start_number;

    @Column(name = "applicant_id")
    @Basic
    private String applicant_id;

    @Column(name = "order_id")
    @Basic
    private int order_id;

    @Column(name = "product_id")
    @Basic
    private int product_id;

    @Column(name = "applicant_number")
    @Basic
    private int applicant_number;

    @Column(name = "clientName")
    @Basic
    private String clientName;

    @Column(name = "clientGender")
    @Basic
    private String clientGender;

    @Column(name = "tshirtSize")
    @Basic
    private String tshirtSize;

    @Column(name = "clientBirthDate")
    @Basic
    private int clientBirthDate;

    @Column(name = "clientEmail")
    @Basic
    private String clientEmail;

    @Column(name = "qrcode")
    @Basic
    private String qrcode;

    @Column(name = "registration_date")
    @Basic
    private Timestamp registration_date;

    @Column(name = "modified_date")
    @Basic
    private LocalDate modified_date;

    @Column(name = "modified_time")
    @Basic
    private LocalTime modified_time;

    @Column(name = "notes")
    @Basic
    private String notes;

    @Column(name = "completed_status")
    @Basic
    private int completed_status;

    @Column(name = "applicant_status")
    @Basic
    @SQLUpdate(sql = "true")
    private int applicant_status;

    @Column(name = "trash_status")
    @Basic
    private int trash_status;

    @Column(name = "loggedInStatus")
    @Basic
    private int loggedInStatus;

    /**
     * Constructor of Applicant
     * @param start_number is start number of applicant
     * @param applicant_id is applicant ID
     * @param order_id is ID of order in webshop
     * @param product_id is ID of webshop product ID
     * @param applicant_number is applicant number in team
     * @param clientName is name of client
     * @param clientGender is gender of client
     * @param tshirtSize is Tshirt size
     * @param clientBirthDate is birthdate of client
     * @param clientEmail email of client
     * @param qrcode is qrcode of applicant, this qrcode is contained applicant ID
     * @param registration_date is date of registration
     * @param modified_date is modify date
     * @param modified_time is modify time
     * @param notes is note of applicant
     * @param completed_status is order completed status in webshop
     * @param applicant_status is race log in status of applicant
     * @param trash_status is webshop order status if order is deleted
     * @param loggedInStatus pre-logged applicant status if applicant log in before the race
     */
    public Applicant(int start_number, String applicant_id, int order_id, int product_id, int applicant_number, String clientName, String clientGender, String tshirtSize, int clientBirthDate, String clientEmail, String qrcode, Timestamp registration_date, LocalDate modified_date, LocalTime modified_time, String notes, int completed_status, int applicant_status, int trash_status, int loggedInStatus) {
        if(modified_date != null && modified_time != null){
            this.modified = LocalDateTime.of(modified_date, modified_time);
        }
        else{
            this.modified = null;
        }
        this.modified = modified;
        this.start_number = start_number;
        this.applicant_id = applicant_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.applicant_number = applicant_number;
        this.clientName = clientName;
        this.clientGender = clientGender;
        this.tshirtSize = tshirtSize;
        this.clientBirthDate = clientBirthDate;
        this.clientEmail = clientEmail;
        this.qrcode = qrcode;
        this.registration_date = registration_date;
        this.modified_date = modified_date;
        this.modified_time = modified_time;
        this.notes = notes;
        this.completed_status = completed_status;
        this.applicant_status = applicant_status;
        this.trash_status = trash_status;
        this.loggedInStatus = loggedInStatus;
        this.applicantStatusString = applicant_status == 1?"Belépve":"Belépésre vár";
    }

    /**
     * Getter of getModified
     * @return modified LocalDateTime
     */
    public LocalDateTime getModified() {
        return modified;
    }

    /**
     *Setter of modified
     * @param modifyDate is modify date of applicant
     * @param modifyTime is modify time of applicant
     */
    public void setModified(LocalDate modifyDate, LocalTime modifyTime) {
        this.modified = LocalDateTime.of(modifyDate,modifyTime);
    }

    /**
     * Setting modified to null
     */
    public void setModifiedToNull() {
        this.modified = null;
    }

    /**
     * Constructor of Applicant
     */
    public Applicant() {
        super();
    }

    /**
     * Getter of id
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Setter of id
     * @param id is ID of applicant
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getter of start_number
     * @return start_number
     */
    public int getStart_number() {
        return start_number;
    }

    /**
     * Setter of start_number
     * @param start_number is start number of applicant
     */
    public void setStart_number(int start_number) {
        this.start_number = start_number;
    }

    /**
     * getter of applicant_id
     * @return applicant_id
     */
    public String getApplicant_id() {
        return applicant_id;
    }

    /**
     * Setter of applicant_id
     * @param applicant_id is ID of applicant
     */
    public void setApplicant_id(String applicant_id) {
        this.applicant_id = applicant_id;
    }

    /**
     * getter of order_id
     * @return order_id
     */
    public int getOrder_id() {
        return order_id;
    }

    /**
     * Setter of order_id
     * @param order_id is ID of webshop order
     */
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    /**
     * Getter of product_id
     * @return product_id
     */
    public int getProduct_id() {
        return product_id;
    }

    /**
     * Setter of product_id
     * @param product_id is ID of webshop product
     */
    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    /**
     * Getter of applicant_number
     * @return applicant_number
     */
    public int getApplicant_number() {
        return applicant_number;
    }

    /**
     * Setter of applicant_number
     * @param applicant_number is number of Applicant of team in order
     */
    public void setApplicant_number(int applicant_number) {
        this.applicant_number = applicant_number;
    }

    /**
     * Getter of client_name
     * @return client_name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Setter of client_name
     * @param clientName is name of Client
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Getter of client_gender
     * @return client_gender
     */
    public String getClientGender() {
        return clientGender;
    }

    /**
     * Setter of client_gender
     * @param clientGender is gender of Client
     */
    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    /**
     * Getter of tshirt_size
     * @return tshirt_size
     */
    public String getTshirtSize() {
        return tshirtSize;
    }

    /**
     * Setter of tshirt_size
     * @param tshirtSize is Tshirt size of applicant
     */
    public void setTshirtSize(String tshirtSize) {
        this.tshirtSize = tshirtSize;
    }

    /**
     * Getter of client_birthdate
     * @return client_birthdate
     */
    public int getClientBirthDate() {
        return clientBirthDate;
    }

    /**
     * Setter of client_birthdate
     * @param clientBirthDate is birth year of applicant
     */
    public void setClientBirthDate(int clientBirthDate) {
        this.clientBirthDate = clientBirthDate;
    }

    /**
     * Getter of client_email
     * @return client_email
     */
    public String getClientEmail() {
        return clientEmail;
    }

    /**
     * Setter of client_email
     * @param clientEmail email of applicant
     */
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    /**
     * Getter of qrCode
     * @return qrCode
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * Setter of qrCode
     * @param qrcode is QR-code of applicant ID
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * Getter of registraion_date
     * @return registraion_date
     */
    public Timestamp getRegistration_date() {
        return registration_date;
    }

    /**
     * Setter of registraion_date
     * @param registration_date is date of registration
     */
    public void setRegistration_date(Timestamp registration_date) {
        this.registration_date = registration_date;
    }

    /**
     * Getter of modified_date
     * @return modified_date
     */
    public LocalDate getModified_date() {
        return modified_date;
    }

    /**
     * Setter of modified_date
     * @param modified_date is modify date of applicant
     */
    public void setModified_date(LocalDate modified_date) {
        this.modified_date = modified_date;
    }

    /**
     * Getter of modified_time
     * @return modified_time
     */
    public LocalTime getModified_time() {
        return modified_time;
    }

    /**
     * Setter of modified_time
     * @param modified_time is modify time of applicant
     */
    public void setModified_time(LocalTime modified_time) {
        this.modified_time = modified_time;
    }

    /**
     * Getter of notes
     * @return notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Setter of notes
     * @param notes is note of applicant
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Getter of completed_status (order payment is completed)
     * @return completed_status
     */
    public int getCompleted_status() {
        return completed_status;
    }

    /**
     * Setter of completed_status
     * @param completed_status is webshop order status when the order is completed
     */
    public void setCompleted_status(int completed_status) {
        this.completed_status = completed_status;
    }

    /**
     * Getter of applicant_status (applicant already logged in to the running race)
     * @return applicant_status
     */
    public int getApplicant_status() {
        return this.applicant_status;
    }

    /**
     * Setter of applicant_status
     * @param applicant_status is the logged in status on the day of the race
     */
    public void setApplicant_status(int applicant_status) {
        this.applicant_status = applicant_status;
        this.applicantStatusString = applicant_status == 1?"Belépve":"Belépésre vár";
    }

    /**
     * Getter of trash_status (Applicants of deleted orders)
     * @return trash_status
     */
    public int getTrash_status() {
        return trash_status;
    }

    /**
     * Setter of trash_status
     * @param trash_status is status when the order has deleted or failed
     */
    public void setTrash_status(int trash_status) {
        this.trash_status = trash_status;
    }

    /**
     * Getter of loggedInStatus
     * @return loggedInStatus
     */
    public int getLoggedInStatus() {
        return loggedInStatus;
    }

    /**
     * Setter of loggedInStatus
     * @param loggedInStatus is pre logged in status when the applicant checked in before the race
     */
    public void setLoggedInStatus(int loggedInStatus) {
        this.loggedInStatus = loggedInStatus;
    }

    /**
     * Getter of applicantStatusString
     * @return applicantStatusString
     */
    public String getApplicantStatusString() {
        return applicantStatusString;
    }

    @Override
    public String toString() {
        return "Applicant{" +
                "id=" + id +
                ", start_number=" + start_number +
                ", applicant_id=" + applicant_id +
                ", order_id=" + order_id +
                ", product_id=" + product_id +
                ", applicant_number=" + applicant_number +
                ", clientName='" + clientName + '\'' +
                ", clientGender='" + clientGender + '\'' +
                ", tshirtSize='" + tshirtSize + '\'' +
                ", clientBirthDate=" + clientBirthDate +
                ", clientEmail='" + clientEmail + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", registration_date=" + registration_date +
                ", notes='" + notes + '\'' +
                ", completed_status=" + completed_status +
                ", applicant_status=" + applicant_status +
                ", trash_status=" + trash_status +
                '}';
    }


}
