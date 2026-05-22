const { login } = require('../../api/user.js')

Page({
  data: {
    loading: false
  },

  doLogin() {
    if (this.data.loading) return;
    this.setData({ loading: true });
    wx.login({
      success: async (res) => {
        try {
          const result = await login(res.code);
          wx.setStorageSync('token', result.data.token);
          wx.setStorageSync('userInfo', result.data.userInfo);
          wx.showToast({ title: '登录成功' });
          wx.switchTab({ url: '/pages/index/index' });
        } catch (err) {
          wx.showToast({ title: '登录失败', icon: 'none' });
        } finally {
          this.setData({ loading: false });
        }
      },
      fail: () => {
        this.setData({ loading: false });
        wx.showToast({ title: '微信登录失败', icon: 'none' });
      }
    });
  }
})
