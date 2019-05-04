const app = require('./index.js');
const logger = require('./logger.js');

app.listen(3010, () => {
  logger.info('node js notification working');
});
