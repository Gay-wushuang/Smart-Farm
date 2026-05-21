Page({
  data: {
    monitorUrl: '',
    updateTime: ''
  },
  
  onLoad() {
    this.refresh();
  },
  
  refresh() {
    this.setData({
      updateTime: new Date().toLocaleString()
    });
  }
})
