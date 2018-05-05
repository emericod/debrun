package services;

import dao.MysqlDao;
import interfaces.MysqlServiceInterface;
import models.Applicant;
import models.Client;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is implements the service of mysql database.
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */

public class MysqlService implements MysqlServiceInterface<Applicant>{

    /**
     * Online DAO.
     */
    private MysqlDao dao;

    /**
     * Constructor of MysqlService.
     */
    public MysqlService() {
        super();
    }

    /**
     * Constructor of MysqlService.
     * @param dao is Mysql DAO.
     */
    public MysqlService(MysqlDao dao) {
        this.dao = dao;
    }

    /**
     * This method authenticate user when try logging in.
     * @param username is user name.
     * @param password is password of user.
     * @return true, if the authentication has succeed, else return false.
     */
    @Override
    public boolean checkUserLogedIn(String username, String password) {
        return dao.checkUserLogedIn(username, password);
    }

    /**
     * Getting user by username parameter.
     * @param username is user name.
     * @return result of Client.
     */
    @Override
    public Client getUserByUsername(String username) {
        return dao.getUserByUsername(username);
    }

    /**
     * Getting all applicants from remoted database.
     * @return list of applicant results.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public List<Applicant> getAllApplicantsFromMysql() throws SQLException {
        return dao.getAllApplicantsFromMysql();    }

    /**
     * Counting Applicants from remoted database.
     * @return number of counted applicants.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public int countMysqlApplicants() throws SQLException {
        return dao.countMysqlApplicants();
    }

    /**
     * Sending all Applicants for remoted database.
     * @return true, if the operation is succeed, else returns false.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public boolean sendApplicantsToMysql() throws SQLException {
        return dao.sendApplicantsToMysql();
    }

    /**
     * Synchronizing last modified Applicants between the local SqlLite database and the remoted database.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void syncDatabase() throws SQLException {
        dao.syncDatabase();
    }

    /**
     * Update applicant by parameter in the remotes parameter.
     * @param applicant_id is ID of applicant.
     * @param status is logged in status of applicant.
     * @param modified is modify datetime of applicant.
     * @throws SQLException if there is any problem with SQL database.
     */
    @Override
    public void updateApplicant(String applicant_id, int status, LocalDateTime modified) throws SQLException {
        dao.updateApplicant(applicant_id, status, modified);
    }
}
