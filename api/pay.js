const { request } = require('./request');

function createPayment(payload) {
  return request({ url: '/pay/create', method: 'POST', data: payload });
}

function getPaymentStatus(paymentId) {
  return request({ url: `/pay/status/${paymentId}` });
}

module.exports = { createPayment, getPaymentStatus };
