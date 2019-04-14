import json
from FtpExplorerConsumer import FtpExplorerConsumer
from FilesFoundProducer import FilesFoundProducer
from NotificationProducer import NotificationProducer
from FtpExplorer import FtpExplorer
import LogstashLogger


class Main:
    def __init__(self):
        self.files_found_producer = None
        self.notification_producer = None

    def publish_method(self, file_payload, notification_payload):
        self.files_found_producer.publish(json.dumps(file_payload))
        self.notification_producer.publish(json.dumps(notification_payload))

    def web_scraping_request_callback(self, ch, method, properties, body):
        LogstashLogger.info(" [x] Received %r" % body)
        self.files_found_producer = FilesFoundProducer()
        self.notification_producer = NotificationProducer()
        msg = json.loads(body.decode("utf-8"))
        ftp_explorer = FtpExplorer(self.publish_method, msg)
        ftp_explorer.start()
        LogstashLogger.info("Done")
        self.files_found_producer.close_connection()
        self.notification_producer.close_connection()

    def main(self):
        LogstashLogger.warning("running ftp explorer ...")
        ftp_explorer_consumer = FtpExplorerConsumer(self.web_scraping_request_callback)


main_object = Main()
main_object.main()
