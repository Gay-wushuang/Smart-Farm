const { BASE_URL, FORCE_MOCK } = require('./config');
const { getToken } = require('../utils/auth');
const { mockRequest } = require('./mock');

const USING_PLACEHOLDER = BASE_URL.startsWith('TODO_');

function isMockMode() {
  if (USING_PLACEHOLDER || FORCE_MOCK) return true;
  const app = getApp();
  return app && app.globalData && app.globalData.useMock;
}

function buildQuery(data) {
  const pairs = Object.keys(data || {})
    .filter((key) => data[key] !== undefined && data[key] !== null && data[key] !== '')
    .map((key) => `${encodeURIComponent(key)}=${encodeURIComponent(data[key])}`);
  return pairs.length ? `?${pairs.join('&')}` : '';
}

function request(options) {
  if (isMockMode()) {
    console.log('[Mock]', options.method || 'GET', options.url, options.data || {});
    return mockRequest(options);
  }

  const token = getToken();
  const method = options.method || 'GET';
  const data = options.data || {};
  const url = method === 'GET'
    ? `${BASE_URL}${options.url}${buildQuery(data)}`
    : `${BASE_URL}${options.url}`;

  return new Promise((resolve, reject) => {
    wx.request({
      url,
      method,
      data: method === 'GET' ? undefined : data,
      header: {
        'content-type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {})
      },
      success(res) {
        if (res.statusCode === 401) {
          wx.removeStorageSync('token');
          wx.reLaunch({ url: '/pages/login/index' });
          reject(new Error('登录已过期，请重新登录'));
          return;
        }
        if (res.statusCode < 200 || res.statusCode >= 300) {
          reject(new Error(`请求失败 ${res.statusCode}`));
          return;
        }
        const body = normalizeResponse(res.data);
        if (body.error) {
          reject(body.error);
          return;
        }
        if (body.code && body.code !== 0 && body.code !== 200) {
          reject(new Error(body.message || '服务异常'));
          return;
        }
        resolve(body.data !== undefined ? body.data : body);
      },
      fail(err) {
        reject(err);
      }
    });
  });
}

function normalizeResponse(data) {
  if (!data) return {};
  if (typeof data !== 'string') return data;
  try {
    return JSON.parse(data);
  } catch (e) {
    return {
      error: new Error('接口返回非合法 JSON，请检查 api/config.js 中的 BASE_URL 是否已替换为真实后端地址')
    };
  }
}

module.exports = { request };
