const { clearAuth } = require('../../utils/auth');

Page({
  data: {
    notificationEnabled: true,
    theme: '清爽绿色主题'
  },

  toggleNotification() {
    this.setData({ notificationEnabled: !this.data.notificationEnabled });
    wx.showToast({
      title: this.data.notificationEnabled ? '消息提醒已开启' : '消息提醒已关闭',
      icon: 'none'
    });
  },

  logout() {
    wx.showModal({
      title: '确认退出',
      content: '退出后需要重新登录。',
      success: (res) => {
        if (res.confirm) {
          clearAuth();
          wx.showToast({ title: '已退出', icon: 'success' });
          setTimeout(() => wx.reLaunch({ url: '/pages/profile/index' }), 600);
        }
      }
    });
  }
});
