import json
from WebScrappingConsumer import WebScrappingConsumer
from FileProducer import FileProducer
from NotficationProducer import NotificationProducer
from WebScraper import WebScraper


class Main:
    def __init__(self):
        self.file_producer = None
        self.notification_producer = None

    def publish_method(self, file_payload, notification_payload):
        self.file_producer.publish(json.dumps(file_payload))
        self.notification_producer.publish(json.dumps(notification_payload))

    def web_scrapping_request_callback(self, ch, method, properties, body):
        print(" [x] Received %r" % body)
        self.file_producer = FileProducer()
        self.notification_producer = NotificationProducer()
        msg = json.loads(body.decode("utf-8"))
        web_scrapper = WebScraper(self.publish_method, tuple(msg["file_types"]), msg["depth"], msg["page_url"], msg["bulk_save_operation_timestamp"], msg["bulk_save_operation_uuid"])
        web_scrapper.start()
        self.file_producer.close_connection()
        self.notification_producer.close_connection()

    def main(self):
        web_scrapping_consumer = WebScrappingConsumer(self.web_scrapping_request_callback)


main_object = Main()
main_object.main()
