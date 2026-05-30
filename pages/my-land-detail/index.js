const { getLandDetail } = require('../../api/land');
const { renewLand } = require('../../api/order');
const { money, landStatusName } = require('../../utils/format');

Page({
  data: {
    id: '',
    land: {},
    price: '0.00',
    statusText: '',
    renewing: false
  },

  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },

  onShow() {
    if (this.data.id) this.load();
  },

  load() {
    getLandDetail(this.data.id).then((land) => {
      this.setData({
        land,
        price: money(land.price),
        statusText: landStatusName(land)
      });
    }).catch((err) => {
      console.error('[MyLandDetail]', err);
    });
  },

  goMonitor() {
    wx.navigateTo({ url: `/pages/monitor/index?id=${this.data.id}` });
  },

  goFertilizer() {
    wx.navigateTo({ url: `/pages/fertilizer/index?id=${this.data.id}` });
  },

  goAppointment() {
    wx.navigateTo({ url: `/pages/appointment/index?id=${this.data.id}` });
  },

  renew() {
    this.setData({ renewing: true });
    renewLand(this.data.id, {})
      .then(() => {
        wx.showToast({ title: '续租申请已提交', icon: 'success' });
      })
      .catch((err) => {
        wx.showToast({ title: err.message || '续租功能暂未开放', icon: 'none' });
      })
      .finally(() => this.setData({ renewing: false }));
  }
});
