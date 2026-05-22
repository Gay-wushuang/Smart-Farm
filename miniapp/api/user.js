const request = require('../utils/request.js')

const login = (code) => {
  return request.post('/auth/wx-login', { code });
}

const getUserInfo = () => {
  return request.get('/user/profile', {}, { authRequired: true });
}

const updateUserInfo = (data) => {
  return request.put('/user/profile', data, { authRequired: true });
}

const bindPhone = (code) => {
  return request.post('/auth/bind-phone', { code }, { authRequired: true });
}

const getMyLands = (params) => {
  return request.get('/user/lands', params, { authRequired: true });
}

module.exports = {
  login,
  getUserInfo,
  updateUserInfo,
  bindPhone,
  getMyLands
}
