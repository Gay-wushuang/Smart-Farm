const { createServiceOrder } = require('../../api/order');
const { createPayment } = require('../../api/pay');
const { getLandDetail } = require('../../api/land');
const { money } = require('../../utils/format');

Page({
  data: { id: '', land: {}, quantity: 1, submitting: false, unitPrice: 0 },
  onLoad(options) {
    this.setData({ id: options.id });
    getLandDetail(options.id).then((land) => this.setData({ land, unitPrice: land.fertilizePrice || land.fertilizerPrice || 0 }));
  },
  minus() { if (this.data.quantity > 1) this.setData({ quantity: this.data.quantity - 1 }); },
  plus() { this.setData({ quantity: this.data.quantity + 1 }); },
  pay() {
    const amount = this.data.unitPrice * this.data.quantity;
    this.setData({ submitting: true });
    createServiceOrder({ landId: Number(this.data.id), serviceType: 'FERTILIZE', quantity: this.data.quantity, remark: '购买化肥' })
      .then((order) => createPayment({ orderId: order.orderId, amount, payType: 'SERVICE' }))
      .then((pay) => new Promise((resolve, reject) => wx.requestPayment({ ...(pay.wxPayParams || pay), success: resolve, fail: reject })))
      .then(() => wx.redirectTo({ url: '/pages/pay-success/index?type=FERTILIZER' }))
      .catch((err) => wx.showToast({ title: err.message || '支付未完成', icon: 'none' }))
      .finally(() => this.setData({ submitting: false }));
  },
  total() { return money(this.data.unitPrice * this.data.quantity); }
});
