var express = require('express');
var app = express();
var sse = require('./sse');
var cors = require('./cors')
var amqp = require('amqplib/callback_api');

app.use(sse)
app.use(cors)

app.get('/stream/:routing_key', function (req, res) {
    res.sseSetup()
    //TODO: localhost in env variable
    amqp.connect('amqp://localhost', function (err, conn) {
        conn.createChannel(function (err, ch) {
            var ex = 'notifications_exchange';

            ch.assertExchange(ex, 'topic', { durable: false });

            ch.assertQueue('', { exclusive: true }, function (err, q) {
                console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q.queue);
                ch.bindQueue(q.queue, ex, req.params.routing_key);

                ch.consume(q.queue, function (msg) {
                    if (msg.content) {
                        console.log(" [x] %s", msg.content.toString());
                        res.sseSend({ event: "test_event", msg: msg.content.toString() })
                    }
                }, { noAck: true });
            });
        });
    });
})

app.listen(3000, function () {
    console.log('Listening on port 3000...')
})