const { getLandMonitor } = require('./land.js')
const request = require('../utils/request.js')

const getDeviceList = (landId) => {
  return getLandMonitor(landId);
}

const controlDevice = (data) => {
  return request.post('/orders', {
    landId: data.landId,
    serviceType: data.action === 'fertilizer' ? 'FERTILIZE' : 'WATER',
    quantity: 1,
    remark: ''
  }, { authRequired: true });
}

const getMonitorUrl = (deviceId) => {
  return getLandMonitor(deviceId);
}

module.exports = {
  getDeviceList,
  controlDevice,
  getMonitorUrl
}
