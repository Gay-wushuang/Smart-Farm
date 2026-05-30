const { request } = require('./request');

function wxLogin(code) {
  return request({ url: '/auth/wx-login', method: 'POST', data: { code } });
}

function bindPhone(payload) {
  return request({ url: '/auth/bind-phone', method: 'POST', data: payload });
}

function getProfile() {
  return request({ url: '/user/profile' });
}

module.exports = { wxLogin, bindPhone, getProfile };
