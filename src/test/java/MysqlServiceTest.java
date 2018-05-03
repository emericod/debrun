import controllers.HomeWindowController;
import dao.MysqlDao;
import models.Applicant;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.MysqlService;

import java.sql.SQLException;
import java.util.List;

public class MysqlServiceTest {

    private MysqlDao mysqlDao;
    private MysqlService mysqlservice;
    private Applicant testApplicant;

    @Before
    public void setUp() throws SQLException {
        mysqlDao = new MysqlDao();
        mysqlservice = new MysqlService(mysqlDao);
    }

    @Test
    public void getAllApplicantsFromMysqlTest() throws SQLException {
        List<Applicant> applicantsResult = mysqlservice.getAllApplicantsFromMysql();

        Assert.assertTrue(applicantsResult != null);
    }

    @Test
    public void countMysqlApplicantsTest() throws SQLException {
        Assert.assertTrue(mysqlservice.countMysqlApplicants() >= 0 );
    }

    @Test
    public void sendApplicantsToMysqlTest() throws SQLException {
        Boolean successSend = mysqlservice.sendApplicantsToMysql();
        Assert.assertTrue(HomeWindowController.networkStatus && successSend || !HomeWindowController.networkStatus && !successSend);
    }





}
