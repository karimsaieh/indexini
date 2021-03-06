package tn.insat.pfe.filemanagementservice.mq;

public class Constants {
    private Constants() {}
    public static final String FILES_FOUND_QUEUE = "files_found_queue";
    public static final String WEB_SCRAPING_QUEUE = "web_scraping_queue";
    public static final String FTP_EXPLORER_QUEUE = "ftp_explorer_queue";
    public static final String FILE_DELETE_QUEUE = "file_delete_queue";
    public static final String FILE_DB_UPDATE_QUEUE = "file_db_update_queue";

    public static final String NOTIFICATIONS_EXCHANGE = "notifications_exchange";
    public static final String FILE_DOWNLOADED = "File downloaded";
}
