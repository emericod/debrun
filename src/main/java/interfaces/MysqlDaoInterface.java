package interfaces;

import models.Applicant;
import models.Client;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is the interface of DAO for Mysql database connection.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 * @param <T> is actual parameter.
 */
public interface MysqlDaoInterface<T> {

    /** User authentication method in remoted database.
     * This method authenticate user when try logging in.
     * @param username is user name.
     * @param password is password of user.
     * @return true, if the authentication has succeed, else return false.
     */
    public boolean checkUserLogedIn(String username, String password);

    /** Getting all clients from remoted database.
     * This method is getting the client list from remoted database.
     * @return List of Client
     */
    public List<Client> getAllClientsFromMysql();

    /** Search client by username from remoted database.
     * Getting user by parameter.
     * @param username is user name.
     * @return Result of Client.
     */
    public Client getUserByUsername(String username);

    /** Getting all applicants from remoted database.
     * This method list all Applicants from the remoted database.
     * @return results of Applicants.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException;

    /** Count applicants in remoted database.
     * This method counting Applicants in remoted database.
     * @return number of applicants.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public int countMysqlApplicants() throws SQLException;

    /** Sending applicants for remoted database.
     * Sending applicants from local SqlLite database for remoted database.
     * @return true, if the operation was succeed, else returns false.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public boolean sendApplicantsToMysql() throws SQLException;

    /** Database synchronizer method (both side).
     * Synchronizing database with the latest modified applicants.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public void syncDatabase() throws SQLException;

    /** Applicant updater method.
     * Updating applicant status in the local SqlLite database.
     * @param applicant_id is ID of applicant.
     * @param status status of user (true if user already logged in, else is false ).
     * @param modified is modified datetime.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public void updateApplicant(String applicant_id, int status, LocalDateTime modified) throws SQLException;

}
