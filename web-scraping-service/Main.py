import json
from WebScrapingConsumer import WebScrapingConsumer
from FilesFoundProducer import FilesFoundProducer
from NotificationProducer import NotificationProducer
from WebScraper import WebScraper


class Main:
    def __init__(self):
        self.files_found_producer = None
        self.notification_producer = None

    def publish_method(self, file_payload, notification_payload):
        self.files_found_producer.publish(json.dumps(file_payload))
        self.notification_producer.publish(json.dumps(notification_payload))

    def web_scraping_request_callback(self, ch, method, properties, body):
        print(" [x] Received %r" % body)
        self.files_found_producer = FilesFoundProducer()
        self.notification_producer = NotificationProducer()
        msg = json.loads(body.decode("utf-8"))
        web_scrapper = WebScraper(self.publish_method, msg)
        web_scrapper.start()
        print("Done")
        self.files_found_producer.close_connection()
        self.notification_producer.close_connection()

    def main(self):
        web_scraping_consumer = WebScrapingConsumer(self.web_scraping_request_callback)


main_object = Main()
main_object.main()
