const { getCamera } = require('../../api/land');

Page({
  data: { id: '', videoUrl: '', cameraName: '', error: '' },
  onLoad(options) {
    this.setData({ id: options.id });
    this.load();
  },
  load() {
    getCamera(this.data.id)
      .then((data) => this.setData({ videoUrl: data.videoUrl || data.url || '', cameraName: data.cameraName || '地块摄像头' }))
      .catch((err) => this.setData({ error: err.message || '摄像头加载失败' }));
  }
});
