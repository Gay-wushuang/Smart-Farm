const { getLandDetail } = require('../../api/land');
const { money, landStatusName } = require('../../utils/format');
const { requireLogin } = require('../../utils/auth');

Page({
  data: { id: '', land: {}, price: '0.00', statusText: '', canRent: false, error: '' },
  onLoad(options) { this.setData({ id: options.id }); this.loadDetail(); },
  loadDetail() {
    getLandDetail(this.data.id).then((land) => {
      this.setData({ land, price: money(land.price), statusText: landStatusName(land), canRent: land.status === 'AVAILABLE' });
    }).catch((err) => this.setData({ error: err.message || '详情加载失败' }));
  },
  goOrder() {
    if (requireLogin()) wx.navigateTo({ url: `/pages/order-confirm/index?id=${this.data.id}` });
  }
});
