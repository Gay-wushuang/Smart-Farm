const { SERVICE_TYPES } = require('../../utils/format');
const { createServiceOrder } = require('../../api/order');

Page({
  data: { landId: '', serviceTypes: SERVICE_TYPES, serviceIndex: 0, date: '', time: '', remark: '', submitting: false },
  onLoad(options) { this.setData({ landId: options.id || '' }); },
  onServiceChange(e) { this.setData({ serviceIndex: Number(e.detail.value) }); },
  onDateChange(e) { this.setData({ date: e.detail.value }); },
  onTimeChange(e) { this.setData({ time: e.detail.value }); },
  onRemarkInput(e) { this.setData({ remark: e.detail.value }); },
  submit() {
    const service = this.data.serviceTypes[this.data.serviceIndex];
    if (!this.data.landId || !this.data.date || !this.data.time) {
      wx.showToast({ title: '请补全预约信息', icon: 'none' });
      return;
    }
    this.setData({ submitting: true });
    createServiceOrder({ landId: Number(this.data.landId), serviceType: service.value, quantity: 1, remark: `${this.data.date} ${this.data.time} ${this.data.remark}` })
      .then(() => { wx.showToast({ title: '预约已提交', icon: 'success' }); wx.redirectTo({ url: '/pages/appointment-list/index' }); })
      .catch((err) => wx.showToast({ title: err.message || '提交失败', icon: 'none' }))
      .finally(() => this.setData({ submitting: false }));
  }
});
