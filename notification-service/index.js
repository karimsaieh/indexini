// TODO: notif shouldn't be exchange anymore, instead pub/sub

// in production , env variables should be set directly in host => best practice
if (process.env.ENV !== 'staging') {
  require('dotenv').config(); // eslint-disable-line global-require
}

const r = require('rethinkdb');
const express = require('express');

const app = express();
const amqp = require('amqplib/callback_api');

const sse = require('./sse');
const cors = require('./cors');
require('./logger');

app.use(sse);
app.use(cors);

let connection = null;
amqp.connect(`amqp://${process.env.pfe_rabbitmq_host}`, (_errCon, conn) => {
  conn.createChannel((_errCh, ch) => {
    const ex = 'notifications_exchange';
    ch.assertExchange(ex, 'fanout', { durable: false });
    ch.assertQueue('', { exclusive: true }, (_err, q) => {
      ch.bindQueue(q.queue, ex, '');
      r.connect({ host: `${process.env.pfe_rethinkdb_host}`, port: 28015 }, (err, conn) => {
        if (err) throw err;
        connection = conn;
        r.dbList().contains('test')
          .do(databaseExists => r.branch(
            databaseExists,
            { dbs_created: 0 },
            r.dbCreate('test'),
          )).run(connection);
        r.db('test').tableCreate('notifs').run(connection, (err, result) => {
          r.db('test').table('notifs').indexCreate('timestamp').run(conn); // throws an error when recreating
        });
      });
      ch.consume(
        q.queue,
        (msg) => {
          if (msg.content) {
            // res.sseSend(JSON.parse(msg.content.toString()));
            console.log('recieved msg from queue :', JSON.parse(msg.content.toString()));
            const jsonmsg = JSON.parse(msg.content.toString());
            jsonmsg.timestamp = new Date().getTime();
            // jsonmsg.id = msg.content.toString();
            r.db('test').table('notifs').insert([
              jsonmsg,
            ]).run(connection, (err, result) => {
              if (err) throw err;
              // console.log(JSON.stringify(result, null, 2));
            });
          }
        },
        { noAck: true },
      );
    });
    // req.on('close', () => {
    //   ch.close();
    //   conn.close();
    // });

    // req.on('end', () => {
    //   ch.close();
    //   conn.close();
    // });
  });
});


app.get('/notifs-ms/api/v1/notifs/stream', (req, res) => {
  res.sseSetup();
  r.db('test').table('notifs').orderBy({ index: r.desc('timestamp') }).limit(10)
    .run(connection, (err, data) => {
      const lastXresult = [];
      data.each((errr, row) => {
        if (errr) throw errr;
        lastXresult.unshift(row);
        // console.log(row);
        // res.sseSend(row);
      }, () => {
        lastXresult.forEach((element) => {
          res.sseSend(element);
        });
      });
    });
  setInterval(() => {
    res.sseSend({ event: 'alive' }); // sinon traefik will give 502 error
  }, 6000);
  r.db('test').table('notifs').changes().run(connection, (err, cursor) => {
    console.log('hellllllllllllooooooo');
    // console.log(cursor);
    cursor.each((errr, row) => {
      if (errr) throw errr;
      console.log(row.new_val);
      res.sseSend(row.new_val);
    });
  });
});

app.get('/', (req, res) => {
  res.status(200).send('Hello World!');
});

module.exports = app;
