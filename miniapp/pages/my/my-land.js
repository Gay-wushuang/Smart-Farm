Page({
  data: {
    list: []
  },
  
  onLoad() {
    
  },
  
  goMonitor() {
    wx.navigateTo({ url: '/pages/monitor/monitor' });
  },
  
  goControl() {
    wx.navigateTo({ url: '/pages/device/control' });
  }
})
