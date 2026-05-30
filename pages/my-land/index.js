const { getMyLands } = require('../../api/land');
const { requireLogin } = require('../../utils/auth');
const { normalizeList } = require('../../utils/format');

Page({
  data: { lands: [], error: '' },
  onShow() { if (requireLogin('/pages/my-land/index')) this.load(); },
  load() {
    getMyLands({ page: 1, size: 30 })
      .then((data) => this.setData({ lands: normalizeList(data), error: '' }))
      .catch((err) => this.setData({ error: err.message || '加载失败', lands: [] }));
  },
  goDetail(e) {
    const land = e.detail;
    wx.navigateTo({ url: `/pages/my-land-detail/index?id=${land.landId || land.id}` });
  }
});
