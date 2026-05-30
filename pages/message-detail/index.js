Page({ data: { title: '', content: '' }, onLoad(options) { this.setData({ title: decodeURIComponent(options.title || '消息详情'), content: decodeURIComponent(options.content || '') }); } });
