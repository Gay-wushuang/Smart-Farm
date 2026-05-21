import request from '../utils/request.js'

export const getLandList = (params) => {
  return request.get('/api/land/list', params);
}

export const getLandDetail = (id) => {
  return request.get(`/api/land/detail/${id}`);
}
