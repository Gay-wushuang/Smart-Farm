const { getLandDetail, claimLand } = require('../../api/land.js')

Page({
  data: {
    land: {}
  },
  
  onLoad(options) {
    this.loadDetail(options.id);
  },
  
  async loadDetail(id) {
    const res = await getLandDetail(id);
    this.setData({ land: res.data });
  },
  
  async createOrder() {
    await claimLand(this.data.land.landId, { duration: 12 });
    wx.showToast({ title: '认领订单成功' });
  }
})
