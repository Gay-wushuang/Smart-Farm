const { getLandMonitor } = require('../../api/land.js')

Page({
  data: {
    landId: null,
    monitor: {},
    updateTime: ''
  },
  
  onLoad(options) {
    this.setData({ landId: options.landId || 1 });
    this.refresh();
  },
  
  async refresh() {
    const res = await getLandMonitor(this.data.landId);
    this.setData({
      monitor: res.data.latest || {},
      updateTime: new Date().toLocaleString()
    });
  }
})
