const BASE_URL = 'http://localhost:8080'

function request(options) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token');
    
    wx.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data);
          } else if (res.data.code === 0) {
            resolve(res.data);
          } else {
            wx.showToast({
              title: res.data.msg || '请求失败',
              icon: 'none'
            });
            reject(res.data);
          }
        } else if (res.statusCode === 401) {
          wx.removeStorageSync('token');
          wx.showToast({
            title: '请先登录',
            icon: 'none'
          });
          if (options.authRequired) {
            const pages = getCurrentPages();
            const currentRoute = pages.length ? pages[pages.length - 1].route : '';
            if (currentRoute !== 'pages/login/login') {
              wx.navigateTo({ url: '/pages/login/login' });
            }
          }
          reject(res);
        } else {
          wx.showToast({
            title: '网络错误',
            icon: 'none'
          });
          reject(res);
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络连接失败',
          icon: 'none'
        });
        reject(err);
      }
    });
  });
}

module.exports = {
  get(url, data, config) {
    return request({ url, method: 'GET', data, ...(config || {}) });
  },
  post(url, data, config) {
    return request({ url, method: 'POST', data, ...(config || {}) });
  },
  put(url, data, config) {
    return request({ url, method: 'PUT', data, ...(config || {}) });
  },
  delete(url, data, config) {
    return request({ url, method: 'DELETE', data, ...(config || {}) });
  }
}
