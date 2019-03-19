import pika
import os


class FileIndexProducer:
    def __init__(self):

        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["HOST"]))
        self.connection = pika.BlockingConnection(pika.ConnectionParameters("localhost"))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='index_queue')

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key='index_queue', body=message)

    def close_connection(self):
        self.connection.close()

