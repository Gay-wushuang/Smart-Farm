const { createOrder } = require('../../api/order.js')

Page({
  data: {
    landId: null,
    submitting: false
  },

  onLoad(options) {
    this.setData({ landId: options.landId || null });
  },

  async submit() {
    if (!this.data.landId) {
      wx.showToast({ title: '缺少土地ID', icon: 'none' });
      return;
    }
    this.setData({ submitting: true });
    try {
      await createOrder({ landId: Number(this.data.landId), serviceType: 'WATER', quantity: 1, remark: '' });
      wx.showToast({ title: '订单创建成功' });
      wx.switchTab({ url: '/pages/my/my-land' });
    } finally {
      this.setData({ submitting: false });
    }
  }
})
