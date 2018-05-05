package controllers;

import dao.OfflineDao;
import javafx.concurrent.Task;
import main.MainApp;
import services.OfflineService;

/**
 * SyncDatabase is class of scheduled synchronizing database on new thread.
 * The sync period time is configurabled in preferences
 * @author Ilyés Imre
 * @version 1.0
 * @since 2018-04-08
 */
public class SyncDatabase extends Task<Integer> {

    /**
     * This method implemented Task abstract method.
     * @return 0, if the thread cancelled.
     * @throws Exception if there is any problem with local SQL database.
     */
    @Override
    protected Integer call() throws Exception {
        OfflineDao dao = new OfflineDao();
        OfflineService service = new OfflineService(dao);

        while(!isCancelled()){
            if(HomeWindowController.networkStatus){
                MainApp.logger.info("Scheduled synchronizing is processing...");
                service.sendApplicantsToMysql();
            }
            Thread.sleep(Integer.valueOf(service.getPreference_by_key("autosync_period").getConfigValue()));
        }
        if(isCancelled()){
            return 0;
        }
        return 1;
    }
}
