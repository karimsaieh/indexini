import pika
import os


class LdaTopicsDescriptionProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["RABBITMQ_HOST"]))
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue='lda_topics_description_queue')

    def publish(self, message):
        self.channel.basic_publish(exchange='', routing_key='lda_topics_description_queue', body=message)

    def close_connection(self):
        self.connection.close()

