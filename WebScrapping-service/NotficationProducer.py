import pika
import os


class NotificationProducer:
    def __init__(self):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(host=os.environ["HOST"]))
        self.channel = self.connection.channel()
        self.channel.exchange_declare(exchange='notifications_exchange', exchange_type='fanout')

    def publish(self, message):
        self.channel.basic_publish(exchange='notifications_exchange', routing_key='', body=message)

    def close_connection(self):
        self.connection.close()
