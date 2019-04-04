import pika
import os
import RabbitMqConstants


class FileIndexProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["RABBITMQ_HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=RabbitMqConstants.FILE_INDEX_QUEUE)

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key=RabbitMqConstants.FILE_INDEX_QUEUE, body=message)

    def close_connection(self):
        self.connection.close()

