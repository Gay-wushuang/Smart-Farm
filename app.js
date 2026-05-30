App({
  globalData: {
    userInfo: null,
    token: null,
    // Set to true to use mock data before the backend is available.
    useMock: false
  },

  onLaunch() {
    const token = wx.getStorageSync('token');
    const userInfo = wx.getStorageSync('userInfo');
    if (token) this.globalData.token = token;
    if (userInfo) this.globalData.userInfo = userInfo;

    wx.getNetworkType({
      success: (res) => {
        if (res.networkType === 'none') {
          console.warn('[App] 当前无网络连接');
        }
      }
    });
  },

  onShow() {
    this.checkLoginExpiry();
  },

  checkLoginExpiry() {
    // The request layer redirects to login when it receives 401.
  },

  getUserInfo() {
    return this.globalData.userInfo;
  },

  isLoggedIn() {
    return Boolean(this.globalData.token);
  }
});
