const { isLoggedIn } = require('../../utils/auth');
const { getProfile } = require('../../api/auth');

Page({
  data: {
    logged: false,
    avatarText: '访',
    user: {},
    menus: [
      { text: '租赁记录', url: '/pages/rental-records/index' },
      { text: '费用账单', url: '/pages/bills/index' },
      { text: '消耗品订单', url: '/pages/consumable-orders/index' },
      { text: '服务预约', url: '/pages/appointment-list/index' },
      { text: '联系客服', url: '/pages/contact/index' },
      { text: '设置', url: '/pages/settings/index' }
    ]
  },
  onShow() {
    this.setData({ logged: isLoggedIn(), avatarText: isLoggedIn() ? '农' : '访' });
    if (isLoggedIn()) {
      getProfile().then((user) => {
        wx.setStorageSync('userInfo', user);
        const name = user.nickname || user.nickName || '农户';
        this.setData({ user, avatarText: name.slice(0, 1) });
      });
    }
  },
  login() { wx.navigateTo({ url: '/pages/login/index' }); },
  open(e) { wx.navigateTo({ url: e.currentTarget.dataset.url }); }
});
