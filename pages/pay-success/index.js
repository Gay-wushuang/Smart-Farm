Page({
  goHome() {
    wx.switchTab({ url: '/pages/index/index' });
  },
  goMine() {
    wx.switchTab({ url: '/pages/my-land/index' });
  }
});
