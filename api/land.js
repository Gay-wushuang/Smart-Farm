const { request } = require('./request');

function getLands(params) {
  return request({ url: '/lands', data: params });
}

function getLandDetail(landId) {
  return request({ url: `/lands/${landId}` });
}

function claimLand(landId, payload) {
  return request({ url: `/lands/${landId}/claim`, method: 'POST', data: payload });
}

function getMyLands(params) {
  return request({ url: '/user/lands', data: params });
}

// [BACKEND_PENDING] Confirm the final camera endpoint with the backend.
function getCamera(landId) {
  return request({ url: `/lands/${landId}/camera` });
}

module.exports = { getLands, getLandDetail, claimLand, getMyLands, getCamera };
