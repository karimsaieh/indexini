import pika
import os
from constants import RabbitMqConstants


class FilesFoundProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["RABBITMQ_HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=RabbitMqConstants.FILES_FOUND_QUEUE)

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key=RabbitMqConstants.FILES_FOUND_QUEUE, body=message,
                                   properties=pika.BasicProperties(
                                       headers={'content_type': 'application/json'}
                                   ))

    def close_connection(self):
        self.connection.close()








