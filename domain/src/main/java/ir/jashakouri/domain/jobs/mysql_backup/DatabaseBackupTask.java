package ir.jashakouri.domain.jobs.mysql_backup;

import com.smattme.MysqlExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

/**
 * @author jashakouri on 9/7/22
 * @project Digitalwallet
 * @email JaShakouri@gmail.com
 */
@Component
@EnableAsync
@Slf4j(topic = "LOG_DatabaseBackupTask")
public class DatabaseBackupTask {

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Async
    @Scheduled(cron = "@hourly")
    public void reportCurrentTime() {

        try {
            Properties properties = new Properties();
            properties.setProperty(MysqlExportService.DB_NAME, "wallet");
            properties.setProperty(MysqlExportService.DB_USERNAME, dbUsername);
            properties.setProperty(MysqlExportService.DB_PASSWORD, dbPassword);
            properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, dbUrl);

            properties.setProperty(MysqlExportService.ADD_IF_NOT_EXISTS, "true");
            properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, "com.mysql.cj.jdbc.Driver");

            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            String time = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

            properties.setProperty(MysqlExportService.SQL_FILE_NAME, String.format("%s-%s", date, time));

            //set the outputs temp dir
            var backupDir = new File("/mnt/backups");
            if (!backupDir.exists())
                if (backupDir.mkdirs()) {
                    log.info("backup dir created");
                }

            properties.setProperty(MysqlExportService.TEMP_DIR, backupDir.getPath());

            properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
            properties.setProperty(MysqlExportService.PRESERVE_GENERATED_SQL_FILE, "true");

            MysqlExportService mysqlExportService = new MysqlExportService(properties);
            log.info("creating backup");

            mysqlExportService.export();

            var backup = mysqlExportService.getGeneratedZipFile();
            if (backup != null) {
                log.info("create backup: {}", backup.getPath());
            }
        } catch (Exception ex) {
            log.error("backup error: {}", ex.getMessage());
        }
    }

}
