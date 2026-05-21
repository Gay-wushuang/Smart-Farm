import { getDeviceList, controlDevice } from '../../api/device.js'

Page({
  data: {
    devices: []
  },
  
  onLoad() {
    this.loadDevices();
  },
  
  async loadDevices() {
    const res = await getDeviceList();
    this.setData({ devices: res.data });
  },
  
  async doAction(e) {
    const { action, id } = e.currentTarget.dataset;
    await controlDevice({ deviceId: id, action });
    wx.showToast({ title: '操作成功' });
  }
})
