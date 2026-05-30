const { getNotifications, readNotification } = require('../../api/message');
const { normalizeList } = require('../../utils/format');

Page({
  data: {
    tabs: [{ label: '全部', value: '' }, { label: '系统', value: 'SYSTEM' }, { label: '服务', value: 'SERVICE' }, { label: '支付', value: 'PAY' }],
    active: '',
    messages: []
  },
  onShow() { this.load(); },
  switchType(e) {
    this.setData({ active: e.currentTarget.dataset.value });
    this.load();
  },
  load() {
    getNotifications({ page: 1, size: 50, type: this.data.active })
      .then((data) => this.setData({ messages: normalizeList(data) }));
  },
  open(e) {
    const item = e.currentTarget.dataset.item;
    if (item.notificationId) readNotification(item.notificationId);
    wx.navigateTo({ url: `/pages/message-detail/index?title=${encodeURIComponent(item.title || '消息')}&content=${encodeURIComponent(item.content || item.message || '')}` });
  }
});
