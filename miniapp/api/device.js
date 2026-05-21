import request from '../utils/request.js'

export const getDeviceList = (landId) => {
  return request.get('/api/device/list', { landId });
}

export const controlDevice = (data) => {
  return request.post('/api/device/control', data);
}

export const getMonitorUrl = (deviceId) => {
  return request.get(`/api/device/monitor/${deviceId}`);
}
