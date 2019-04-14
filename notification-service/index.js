// in production , env variables should be set directly in host => best practice
if (process.env.ENV !== 'staging') {
  require('dotenv').config(); // eslint-disable-line global-require
}

const express = require('express');

const app = express();
const amqp = require('amqplib/callback_api');
const logger = require('./logger.js');
const sse = require('./sse');
const cors = require('./cors');
require('./logger');

app.use(sse);
app.use(cors);

app.get('/notifs-ms/api/v1/notifs/stream', (req, res) => {
  res.sseSetup();
  setInterval(() => {
    res.sseSend({ event: 'alive' }); // sino traefik will give 502 error
  }, 60000);
  amqp.connect(`amqp://${process.env.pfe_rabbitmq_host}`, (_errCon, conn) => {
    conn.createChannel((_errCh, ch) => {
      const ex = 'notifications_exchange';
      ch.assertExchange(ex, 'fanout', { durable: false });
      ch.assertQueue('', { exclusive: true }, (_err, q) => {
        ch.bindQueue(q.queue, ex, '');
        ch.consume(
          q.queue,
          (msg) => {
            if (msg.content) {
              res.sseSend(JSON.parse(msg.content.toString()));
            }
          },
          { noAck: true },
        );
      });
      req.on('close', () => {
        ch.close();
        conn.close();
      });

      req.on('end', () => {
        ch.close();
        conn.close();
      });
    });
  });
});

app.listen(3010, () => {
  logger.info('node js notification working');
});
