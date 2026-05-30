Component({
  data: {
    selected: 0,
    list: [
      { pagePath: '/pages/index/index', text: '首页', icon: 'home' },
      { pagePath: '/pages/my-land/index', text: '我的土地', icon: 'land' },
      { pagePath: '/pages/message/index', text: '消息', icon: 'bell' },
      { pagePath: '/pages/profile/index', text: '我的', icon: 'user' }
    ]
  },

  lifetimes: {
    attached() {
      this.syncSelected();
    }
  },

  pageLifetimes: {
    show() {
      this.syncSelected();
    }
  },

  methods: {
    syncSelected() {
      const pages = getCurrentPages();
      const page = pages[pages.length - 1];
      if (page) {
        const idx = this.data.list.findIndex(
          item => item.pagePath === '/' + page.route
        );
        if (idx >= 0) this.setData({ selected: idx });
      }
    },

    switchTab(e) {
      const idx = e.currentTarget.dataset.index;
      const item = this.data.list[idx];
      wx.switchTab({ url: item.pagePath });
    }
  }
});
