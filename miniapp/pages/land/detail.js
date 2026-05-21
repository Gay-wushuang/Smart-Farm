import { getLandDetail } from '../../api/land.js'
import { createOrder } from '../../api/order.js'

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
    await createOrder({ landId: this.data.land.id });
    wx.showToast({ title: '创建订单成功' });
  }
})
