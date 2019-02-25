import pika
import os


class FileProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='files_queue')

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key='files_queue', body=message)

    def close_connection(self):
        self.connection.close()








