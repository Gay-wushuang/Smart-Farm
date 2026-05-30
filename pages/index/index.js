const { getLands } = require('../../api/land');
const { normalizeList } = require('../../utils/format');

Page({
  data: {
    banners: ['/assets/images/banner-farm.svg', '/assets/images/land-default.svg'],
    keyword: '',
    filters: { status: '', minPrice: '', maxPrice: '', minArea: '', maxArea: '', zone: '' },
    statusOptions: ['全部状态', '可租', '已租', '维护中'],
    statusValues: ['', 'AVAILABLE', 'RENTED', 'MAINTENANCE'],
    lands: [],
    loading: false,
    error: ''
  },

  onLoad() { this.loadLands(); },
  onPullDownRefresh() { this.loadLands().finally(() => wx.stopPullDownRefresh()); },

  onSearchInput(e) { this.setData({ keyword: e.detail.value }); },
  onFilterInput(e) { this.setData({ [`filters.${e.currentTarget.dataset.key}`]: e.detail.value }); },
  onStatusChange(e) { this.setData({ 'filters.status': this.data.statusValues[e.detail.value] }); this.loadLands(); },
  onSearchConfirm() { this.loadLands(); },

  loadLands() {
    this.setData({ loading: true, error: '' });
    const { keyword, filters } = this.data;
    return getLands({ page: 1, size: 20, keyword, ...filters })
      .then((data) => this.setData({ lands: normalizeList(data) }))
      .catch((err) => this.setData({ error: err.message || '土地加载失败', lands: [] }))
      .finally(() => this.setData({ loading: false }));
  },

  goDetail(e) {
    const land = e.detail;
    wx.navigateTo({ url: `/pages/land-detail/index?id=${land.landId || land.id}` });
  }
});
