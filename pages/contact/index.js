Page({
  data: { phone: '400-800-2026', wechat: 'land-service' },
  call() {
    wx.makePhoneCall({ phoneNumber: this.data.phone });
  }
});
