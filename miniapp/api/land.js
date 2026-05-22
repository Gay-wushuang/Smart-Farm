const request = require('../utils/request.js')

const getLandList = (params) => {
  return request.get('/lands', params);
}

const getLandDetail = (id) => {
  return request.get(`/lands/${id}`);
}

const claimLand = (id, data) => {
  return request.post(`/lands/${id}/claim`, data, { authRequired: true });
}

const getLandMonitor = (id, params) => {
  return request.get(`/lands/${id}/monitor`, params);
}

module.exports = {
  getLandList,
  getLandDetail,
  claimLand,
  getLandMonitor
}
