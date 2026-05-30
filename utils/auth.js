function getToken() { return wx.getStorageSync('token') || ''; }

function isLoggedIn() { return Boolean(getToken()); }

function setAuth(token, userInfo) {
  wx.setStorageSync('token', token);
  wx.setStorageSync('userInfo', userInfo);
  const app = getApp();
  if (app) {
    app.globalData.token = token;
    app.globalData.userInfo = userInfo;
  }
}

function clearAuth() {
  wx.removeStorageSync('token');
  wx.removeStorageSync('userInfo');
  const app = getApp();
  if (app) {
    app.globalData.token = null;
    app.globalData.userInfo = null;
  }
}

function requireLogin(redirect) {
  if (isLoggedIn()) return true;
  const target = redirect || getCurrentPageUrl();
  wx.navigateTo({ url: `/pages/login/index?redirect=${encodeURIComponent(target)}` });
  return false;
}

function getCurrentPageUrl() {
  const pages = getCurrentPages();
  const page = pages[pages.length - 1];
  if (!page) return '/pages/index/index';
  const query = Object.keys(page.options || {}).map((key) => `${key}=${encodeURIComponent(page.options[key])}`).join('&');
  return `/${page.route}${query ? `?${query}` : ''}`;
}

module.exports = { getToken, isLoggedIn, setAuth, clearAuth, requireLogin };
