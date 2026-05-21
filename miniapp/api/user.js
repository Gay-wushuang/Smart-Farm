import request from '../utils/request.js'

export const login = (code) => {
  return request.post('/api/user/login', { code });
}

export const getUserInfo = () => {
  return request.get('/api/user/info');
}
