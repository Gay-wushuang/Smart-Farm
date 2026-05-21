import { getLandList } from '../../api/land.js'

Page({
  data: {
    list: []
  },
  
  onLoad() {
    this.loadList();
  },
  
  async loadList() {
    const res = await getLandList();
    this.setData({ list: res.data });
  }
})
