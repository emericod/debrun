package models;

import javax.persistence.*;

/**
 * Client is class of client's model class who use this application
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

@Entity
@Table(name = "clients")
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "username")
    @Basic
    private String username;

    @Column(name = "firstName")
    @Basic
    private String firstName;

    @Column(name = "lastName")
    @Basic
    private String lastName;

    @Column(name = "password")
    @Basic
    private String password;

    @Column(name = "status")
    @Basic
    private int status;

    @Column(name = "enabledStatus")
    @Basic
    private boolean enabledStatus;

    public Client(){
        super();
    }

    /**
     * Constructor of Client Class
     * @param username is user name
     * @param password is password of user
     */
    public Client(String username, String password){
        this.id = 1;
        this.username = username;
        this.password = password;
        this.firstName = "";
        this.lastName = "";
        this.status = 1;
    }

    /**
     * Constructor of Client Class
     * @param username is user name
     * @param firstName is first name of user
     * @param lastName is last name of user
     * @param password is password of user
     * @param status is logged in status of user
     * @param enabledStatus is enabled status of user (if the user has a login datas, but the admin doesn't allow with the enabled status, the user login is denied)
     */
    public Client(String username, String firstName, String lastName, String password, int status, boolean enabledStatus) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.status = status;
        this.enabledStatus = enabledStatus;
    }

    /**
     * Getter of id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter of username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter of firstName
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter of lastName
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter of password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter of status
     * @return status
     */
    public int getStatus() {
        return status;
    }

    /**
     * Setter of id
     * @param id is ID of user
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter of username
     * @param username is user name
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter of firstName
     * @param firstName is first name of user
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Setter of lastName
     * @param lastName is last name of user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Setter of password
     * @param password is password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Setter of status
     * @param status is logged in status of user
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Getter of enabledStatus
     * @return enabedStatus
     */
    public boolean getEnabledStatus() {
        return enabledStatus;
    }

    /**
     * Setter of enabledStatus
     * @param enabledStatus is admin allowed the login of user
     */
    public void setEnabledStatus(boolean enabledStatus) {
        this.enabledStatus = enabledStatus;
    }

    @Override
    public String toString() {
        return "Client{" + "username='" + username + '}';
    }
}


