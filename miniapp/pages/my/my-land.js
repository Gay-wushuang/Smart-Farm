const { getMyLands } = require('../../api/user.js')

Page({
  data: {
    list: []
  },
  
  onLoad() {
    this.loadList();
  },

  async loadList() {
    const res = await getMyLands();
    this.setData({ list: res.data.list || [] });
  },
  
  goMonitor(e) {
    wx.navigateTo({ url: `/pages/monitor/monitor?landId=${e.currentTarget.dataset.landId}` });
  },
  
  goControl(e) {
    wx.navigateTo({ url: `/pages/device/control?landId=${e.currentTarget.dataset.landId}` });
  }
})
