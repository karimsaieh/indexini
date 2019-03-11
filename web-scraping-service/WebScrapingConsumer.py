import os
import pika


class WebScrapingConsumer:

    def __init__(self, callback):
        self.callback = callback
        # disabling heartbeat: https://stackoverflow.com/questions/14572020/handling-long-running-tasks-in-pika-rabbitmq
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["HOST"],
                                                                            heartbeat_interval=0))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='web_scraping_queue')
        self.channel.basic_consume(self.callback, queue='web_scraping_queue', no_ack=True)
        self.channel.start_consuming()
