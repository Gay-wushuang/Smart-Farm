const { getLandDetail, claimLand } = require('../../api/land');
const { createPayment } = require('../../api/pay');
const { money } = require('../../utils/format');

Page({
  data: { id: '', land: {}, price: '0.00', submitting: false },
  onLoad(options) {
    this.setData({ id: options.id });
    getLandDetail(options.id).then((land) => this.setData({ land, price: money(land.price) }));
  },
  pay() {
    this.setData({ submitting: true });
    claimLand(this.data.id, { duration: 12 })
      .then((claim) => createPayment({ orderId: claim.orderId || claim.claimId, amount: this.data.land.price, payType: 'LAND' }))
      .then((pay) => new Promise((resolve, reject) => wx.requestPayment({ ...(pay.wxPayParams || pay), success: resolve, fail: reject })))
      .then(() => wx.redirectTo({ url: '/pages/pay-success/index?type=LAND' }))
      .catch((err) => wx.showToast({ title: err.message || '支付未完成', icon: 'none' }))
      .finally(() => this.setData({ submitting: false }));
  }
});
