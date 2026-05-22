const { getLandList } = require('../../api/land.js')

Page({
  data: {
    list: [],
    loading: false,
    errorText: ''
  },
  
  onLoad() {
    this.loadList();
  },

  onShow() {
    this.loadList();
  },
  
  async loadList() {
    if (this.data.loading) return;
    this.setData({ loading: true, errorText: '' });
    try {
      const res = await getLandList({ page: 1, size: 10 });
      this.setData({ list: (res.data && res.data.list) || [] });
    } catch (err) {
      this.setData({ errorText: err.msg || '土地列表加载失败' });
    } finally {
      this.setData({ loading: false });
    }
  }
})
