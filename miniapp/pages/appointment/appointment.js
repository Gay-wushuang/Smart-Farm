Page({
  data: {
    landId: null,
    serviceType: 'plant'
  },

  onLoad(options) {
    this.setData({ landId: options.landId || null });
  },

  submit() {
    wx.showToast({ title: '预约功能待接入', icon: 'none' });
  }
})
