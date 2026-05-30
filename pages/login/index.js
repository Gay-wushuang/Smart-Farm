const { wxLogin } = require('../../api/auth');
const { setAuth } = require('../../utils/auth');

const TAB_PAGES = ['/pages/index/index', '/pages/my-land/index', '/pages/message/index', '/pages/profile/index'];

function goTo(url) {
  const path = (url || '').split('?')[0];
  if (TAB_PAGES.includes(path)) {
    wx.switchTab({ url: path });
    return;
  }
  wx.redirectTo({ url });
}

Page({
  data: { redirect: '/pages/index/index', loading: false },

  onLoad(options) {
    if (options.redirect) this.setData({ redirect: decodeURIComponent(options.redirect) });
  },

  login() {
    this.setData({ loading: true });
    wx.login({
      success: ({ code }) => {
        wxLogin(code).then((data) => {
          setAuth(data.token || data.accessToken, data.user || data.userInfo || {});
          if (data.needBindPhone || data.boundPhone === false) {
            wx.redirectTo({ url: `/pages/bind-phone/index?redirect=${encodeURIComponent(this.data.redirect)}` });
          } else {
            goTo(this.data.redirect);
          }
        }).catch((err) => {
          wx.showToast({ title: err.message || '登录失败', icon: 'none' });
          console.error('[Login]', err);
        }).finally(() => this.setData({ loading: false }));
      },
      fail: () => {
        this.setData({ loading: false });
        wx.showToast({ title: '微信登录失败，请稍后重试', icon: 'none' });
      }
    });
  }
});
