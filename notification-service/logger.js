let logger = require('pino')();

logger = logger.child({ serviceName: 'notification-service' });

module.exports = logger;
