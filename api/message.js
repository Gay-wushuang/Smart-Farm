const { request } = require('./request');

function getNotifications(params) {
  return request({ url: '/notifications', data: params });
}

function readNotification(notificationId) {
  return request({ url: `/notifications/${notificationId}/read`, method: 'PUT' });
}

function readAllNotifications() {
  return request({ url: '/notifications/read-all', method: 'PUT' });
}

module.exports = { getNotifications, readNotification, readAllNotifications };
