Page({
  data: {
    orderId: null
  },

  onLoad(options) {
    this.setData({ orderId: options.orderId || null });
  },

  submit() {
    wx.showToast({ title: '续租功能待接入', icon: 'none' });
  }
})
