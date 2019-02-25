import os
import pika


class WebScrappingConsumer:

    def __init__(self, callback):
        self.callback = callback
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='web_scrapping_queue')
        self.channel.basic_consume(self.callback, queue='web_scrapping_queue', no_ack=True)
        self.channel.start_consuming()
