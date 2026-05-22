const { getDeviceList, controlDevice } = require('../../api/device.js')

Page({
  data: {
    landId: null,
    monitor: {}
  },
  
  onLoad(options) {
    this.setData({ landId: options.landId || 1 });
    this.loadDevices();
  },
  
  async loadDevices() {
    const res = await getDeviceList(this.data.landId);
    this.setData({ monitor: res.data.latest || {} });
  },
  
  async doAction(e) {
    const { action } = e.currentTarget.dataset;
    await controlDevice({ landId: this.data.landId, action });
    wx.showToast({ title: '操作成功' });
  }
})
