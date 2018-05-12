package interfaces;

import models.Applicant;
import models.Client;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MysqlDaiServiceInterface is class of mysqldao service interface.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 * @param <T> is actual parameter.
 */
public interface MysqlServiceInterface<T> {

    /** User authentication method in remoted database.
     * Authenticate user in the local SqlLite database.
     * @param username user name.
     * @param password password of user.
     * @return true, if the authentication has succeed, else return false.
     */
    public boolean checkUserLogedIn(String username, String password);

    /** Getting all clients from remoted database.
     * This methos get all applicants from remoted database.
     * @return list of Client.
     */
    public List<Client> getAllClientsFromMysql();

    /** Search client by username from remoted database.
     * Getting user by username parameter.
     * @param username is user name.
     * @return result of Client.
     */
    public Client getUserByUsername(String username);

    /** Getting all applicants from remoted database.
     * This method list all Applicants from the remoted database.
     * @return list of applicant results.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException;

    /** Count applicants in remoted database.
     * This method counting Applicants in remoted database.
     * @return number of counted applicants.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public int countMysqlApplicants() throws SQLException;

    /** Sending applicants for remoted database.
     * Sending applicants from local SqlLite database for remoted database.
     * @return true, if the operation is succeed, else returns false.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public boolean sendApplicantsToMysql() throws SQLException;

    /** Database synchronizer method (both side).
     * Synchronizing last modified Applicants between the local SqlLite database and the remoted database.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public void syncDatabase() throws SQLException;

    /** Applicant updater method.
     * Update applicant by parameter in the remotes parameter.
     * @param applicant_id is ID of applicant.
     * @param status status of user (true if user already logged in, else is false).
     * @param modified is modified datetime.
     * @throws SQLException if there is any problem with remoted SQL database.
     */
    public void updateApplicant(String applicant_id, int status, LocalDateTime modified) throws SQLException;
}
