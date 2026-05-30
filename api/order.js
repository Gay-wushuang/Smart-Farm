const { request } = require('./request');

function createServiceOrder(payload) {
  return request({ url: '/orders', method: 'POST', data: payload });
}

function getOrderDetail(orderId) {
  return request({ url: `/orders/${orderId}` });
}

function getMyOrders(params) {
  return request({ url: '/user/orders', data: params });
}

// [BACKEND_PENDING] Renew payment flow needs backend confirmation.
function renewLand(landId, payload) {
  return request({ url: `/lands/${landId}/renew`, method: 'POST', data: payload });
}

module.exports = { createServiceOrder, getOrderDetail, getMyOrders, renewLand };
