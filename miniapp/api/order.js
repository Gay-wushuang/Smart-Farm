const request = require('../utils/request.js')

const createOrder = (data) => {
  return request.post('/orders', data, { authRequired: true });
}

const getOrderList = (params) => {
  return request.get('/user/orders', params, { authRequired: true });
}

const getOrderDetail = (id) => {
  return request.get(`/orders/${id}`, {}, { authRequired: true });
}

const cancelOrder = (id) => {
  return request.put(`/orders/${id}/cancel`, {}, { authRequired: true });
}

module.exports = {
  createOrder,
  getOrderList,
  getOrderDetail,
  cancelOrder
}
