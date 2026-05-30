const { bindPhone } = require('../../api/auth');

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
  getPhoneNumber(e) {
    if (!e.detail.code && !e.detail.encryptedData) {
      wx.showToast({ title: '未授权手机号', icon: 'none' });
      return;
    }
    this.setData({ loading: true });
    bindPhone(e.detail)
      .then(() => goTo(this.data.redirect))
      .catch((err) => wx.showToast({ title: err.message || '绑定失败', icon: 'none' }))
      .finally(() => this.setData({ loading: false }));
  }
});
