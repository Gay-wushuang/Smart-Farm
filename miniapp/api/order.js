import request from '../utils/request.js'

export const createOrder = (data) => {
  return request.post('/api/order/create', data);
}

export const getOrderList = (params) => {
  return request.get('/api/order/list', params);
}

export const getOrderDetail = (id) => {
  return request.get(`/api/order/detail/${id}`);
}
