import pika
import os
import RabbitMqConstants


class LdaTopicsDescriptionProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["RABBITMQ_HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=RabbitMqConstants.LDA_TOPICS_DESCRIPTION_QUEUE)

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key=RabbitMqConstants.LDA_TOPICS_DESCRIPTION_QUEUE, body=message)

    def close_connection(self):
        self.connection.close()

