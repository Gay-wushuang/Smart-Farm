let mockToken = 'mock_token_' + Date.now();
let notificationsRead = {};

const LANDS = [
  { landId: 1, landNo: 'L2024001', landName: '南坡向阳地块A1', name: '南坡向阳地块A1', image: '/assets/images/land-default.svg', area: 120, areaUnit: '㎡', price: 2800, priceUnit: '元/年', status: 'AVAILABLE', zone: '南坡', remainDays: 365, remainingDays: 365, claimExpireTime: '', expireTime: '', soilType: '沙壤土', fertilizePrice: 180, fertilizerPrice: 180 },
  { landId: 2, landNo: 'L2024002', landName: '北山梯田B2', name: '北山梯田B2', image: '/assets/images/land-default.svg', area: 85, areaUnit: '㎡', price: 1800, priceUnit: '元/年', status: 'AVAILABLE', zone: '北山', remainDays: 365, remainingDays: 365, claimExpireTime: '', expireTime: '', soilType: '黏土', fertilizePrice: 150, fertilizerPrice: 150 },
  { landId: 3, landNo: 'L2024003', landName: '西河平原C3', name: '西河平原C3', image: '/assets/images/land-default.svg', area: 200, areaUnit: '㎡', price: 4200, priceUnit: '元/年', status: 'RENTED', zone: '西河', remainDays: 180, remainingDays: 180, claimExpireTime: '2026-11-30', expireTime: '2026-11-30', soilType: '黑土', fertilizePrice: 200, fertilizerPrice: 200 },
  { landId: 4, landNo: 'L2024004', landName: '东岭果园D4', name: '东岭果园D4', image: '/assets/images/land-default.svg', area: 150, areaUnit: '㎡', price: 3500, priceUnit: '元/年', status: 'AVAILABLE', zone: '东岭', remainDays: 365, remainingDays: 365, claimExpireTime: '', expireTime: '', soilType: '红壤', fertilizePrice: 160, fertilizerPrice: 160 },
  { landId: 5, landNo: 'L2024005', landName: '中谷大棚E5', name: '中谷大棚E5', image: '/assets/images/land-default.svg', area: 300, areaUnit: '㎡', price: 6000, priceUnit: '元/年', status: 'MAINTENANCE', zone: '中谷', remainDays: 0, remainingDays: 0, claimExpireTime: '', expireTime: '', soilType: '混合土', fertilizePrice: 220, fertilizerPrice: 220 },
  { landId: 6, landNo: 'L2024006', landName: '南坡向阳地块A2', name: '南坡向阳地块A2', image: '/assets/images/land-default.svg', area: 95, areaUnit: '㎡', price: 2200, priceUnit: '元/年', status: 'AVAILABLE', zone: '南坡', remainDays: 365, remainingDays: 365, claimExpireTime: '', expireTime: '', soilType: '沙壤土', fertilizePrice: 180, fertilizerPrice: 180 }
];

const USER_PROFILE = {
  userId: 1,
  nickname: '农户张三',
  nickName: '农户张三',
  phone: '138****2026',
  avatar: '',
  boundPhone: true
};

const MY_LANDS = [
  { landId: 3, landNo: 'L2024003', landName: '西河平原C3', name: '西河平原C3', image: '/assets/images/land-default.svg', area: 200, areaUnit: '㎡', price: 4200, status: 'RENTED', zone: '西河', remainDays: 180, remainingDays: 180, claimExpireTime: '2026-11-30', expireTime: '2026-11-30', soilType: '黑土', fertilizePrice: 200, fertilizerPrice: 200, statusName: '已租' }
];

const NOTIFICATIONS = [
  { notificationId: 1, type: 'SYSTEM', title: '系统维护通知', content: '系统将于今晚 02:00-04:00 进行例行维护，届时部分功能暂不可用。', message: '系统将于今晚 02:00-04:00 进行例行维护。', createTime: '2026-05-30 10:00', read: false },
  { notificationId: 2, type: 'PAY', title: '支付成功', content: '您已成功支付西河平原C3土地租赁费用 ¥4,200.00。', message: '您已成功支付西河平原C3土地租赁费用 ¥4,200.00。', createTime: '2026-05-28 14:30', read: true },
  { notificationId: 3, type: 'SERVICE', title: '施肥服务完成', content: '西河平原C3地块施肥服务已完成，请确认。', message: '西河平原C3地块施肥服务已完成，请确认。', createTime: '2026-05-25 09:00', read: false },
  { notificationId: 4, type: 'ALERT', title: '租期即将到期', content: '西河平原C3地块租期将于2026年11月30日到期，请及时续租。', message: '西河平原C3地块租期将于2026年11月30日到期。', createTime: '2026-05-20 08:00', read: true },
  { notificationId: 5, type: 'SYSTEM', title: '新功能上线', content: '实时监控功能已上线，您可以在“我的土地”中查看地块摄像头画面。', message: '实时监控功能已上线。', createTime: '2026-05-15 12:00', read: false }
];

const ORDERS = [
  { orderId: 101, orderNo: 'ORD20260530001', type: 'LAND', landId: 3, landName: '西河平原C3', amount: 4200, serviceType: null, serviceTypeName: null, status: 'PAID', statusName: '已支付', createTime: '2026-05-30 10:00', remark: '', quantity: 1 },
  { orderId: 102, orderNo: 'ORD20260525002', type: 'SERVICE', landId: 3, landName: '西河平原C3', amount: 200, serviceType: 'FERTILIZE', serviceTypeName: '施肥', status: 'COMPLETED', statusName: '已完成', createTime: '2026-05-25 09:00', remark: '施肥服务', quantity: 1 },
  { orderId: 103, orderNo: 'ORD20260520003', type: 'SERVICE', landId: 3, landName: '西河平原C3', amount: 150, serviceType: 'WEEDING', serviceTypeName: '除草', status: 'PROCESSING', statusName: '服务中', createTime: '2026-05-20 15:00', remark: '2026-05-28 上午 除草', quantity: 1 }
];

let nextOrderId = 104;

function delay(ms) {
  return new Promise(resolve => setTimeout(resolve, ms || 200));
}

function filterLands(params) {
  let list = [...LANDS];
  if (params.keyword) {
    const kw = params.keyword.toLowerCase();
    list = list.filter(l => l.landName.toLowerCase().includes(kw) || l.landNo.toLowerCase().includes(kw) || l.zone.toLowerCase().includes(kw));
  }
  if (params.status) list = list.filter(l => l.status === params.status);
  if (params.zone) list = list.filter(l => l.zone.includes(params.zone));
  if (params.minPrice) list = list.filter(l => l.price >= Number(params.minPrice));
  if (params.maxPrice) list = list.filter(l => l.price <= Number(params.maxPrice));
  if (params.minArea) list = list.filter(l => l.area >= Number(params.minArea));
  if (params.maxArea) list = list.filter(l => l.area <= Number(params.maxArea));
  return list;
}

function paginate(list, page, size) {
  const start = ((page || 1) - 1) * (size || 20);
  return list.slice(start, start + (size || 20));
}

function mockRequest(options) {
  const method = (options.method || 'GET').toUpperCase();
  const url = options.url;
  const data = options.data || {};

  return delay().then(() => {
    if (method === 'POST' && url === '/auth/wx-login') {
      mockToken = 'mock_token_' + Date.now();
      return { token: mockToken, accessToken: mockToken, user: { ...USER_PROFILE }, userInfo: { ...USER_PROFILE }, needBindPhone: false, boundPhone: true };
    }
    if (method === 'POST' && url === '/auth/bind-phone') {
      USER_PROFILE.boundPhone = true;
      return { success: true };
    }
    if (url === '/user/profile') {
      return { ...USER_PROFILE };
    }

    if (method === 'GET' && url === '/lands') {
      const filtered = filterLands(data);
      return { records: paginate(filtered, data.page, data.size), total: filtered.length };
    }

    const landDetailMatch = url.match(/^\/lands\/(\d+)$/);
    if (method === 'GET' && landDetailMatch) {
      const land = LANDS.find(l => l.landId === Number(landDetailMatch[1]));
      if (!land) return rejectErr('土地不存在');
      return { ...land };
    }

    const claimMatch = url.match(/^\/lands\/(\d+)\/claim$/);
    if (method === 'POST' && claimMatch) {
      const land = LANDS.find(l => l.landId === Number(claimMatch[1]));
      if (!land) return rejectErr('土地不存在');
      if (land.status !== 'AVAILABLE') return rejectErr('该土地当前不可租赁');
      land.status = 'RENTED';
      const order = { orderId: nextOrderId++, claimId: nextOrderId - 1, landId: land.landId, amount: land.price, status: 'PENDING_PAY' };
      return order;
    }

    const cameraMatch = url.match(/^\/lands\/(\d+)\/camera$/);
    if (method === 'GET' && cameraMatch) {
      return { videoUrl: '', url: '', cameraName: '地块摄像头（模拟数据，待后端返回真实视频流）' };
    }

    const renewMatch = url.match(/^\/lands\/(\d+)\/renew$/);
    if (method === 'POST' && renewMatch) {
      const renewLand = LANDS.find(l => l.landId === Number(renewMatch[1]));
      return { orderId: nextOrderId++, landId: Number(renewMatch[1]), amount: (renewLand && renewLand.price) || 0, status: 'PENDING_PAY' };
    }

    if (url === '/user/lands') {
      return { records: MY_LANDS, total: MY_LANDS.length };
    }

    if (url === '/notifications') {
      let list = NOTIFICATIONS.map(n => ({ ...n, read: notificationsRead[n.notificationId] || n.read }));
      if (data.type) list = list.filter(n => n.type === data.type);
      return { records: paginate(list, data.page, data.size), total: list.length };
    }

    const readMatch = url.match(/^\/notifications\/(\d+)\/read$/);
    if (method === 'PUT' && readMatch) {
      notificationsRead[Number(readMatch[1])] = true;
      return { success: true };
    }
    if (method === 'PUT' && url === '/notifications/read-all') {
      NOTIFICATIONS.forEach(n => { notificationsRead[n.notificationId] = true; });
      return { success: true };
    }

    if (method === 'POST' && url === '/orders') {
      const foundLand = LANDS.find(l => l.landId === data.landId);
      const order = { orderId: nextOrderId++, orderNo: 'ORD' + Date.now(), type: 'SERVICE', landId: data.landId, landName: (foundLand && foundLand.landName) || '', amount: 0, serviceType: data.serviceType, status: 'PENDING_PAY', createTime: new Date().toISOString(), remark: data.remark || '', quantity: data.quantity || 1 };
      ORDERS.unshift(order);
      return order;
    }

    const orderDetailMatch = url.match(/^\/orders\/(\d+)$/);
    if (method === 'GET' && orderDetailMatch) {
      const order = ORDERS.find(o => o.orderId === Number(orderDetailMatch[1]));
      if (!order) return rejectErr('订单不存在');
      return order;
    }

    if (url === '/user/orders') {
      let list = [...ORDERS];
      if (data.serviceType) list = list.filter(o => o.serviceType === data.serviceType);
      return { records: paginate(list, data.page, data.size), total: list.length };
    }

    if (method === 'POST' && url === '/pay/create') {
      return { wxPayParams: { timeStamp: String(Date.now()), nonceStr: 'mock_nonce', package: 'prepay_id=mock_prepay_' + Date.now(), signType: 'MD5', paySign: 'mock_sign' } };
    }

    const payStatusMatch = url.match(/^\/pay\/status\/(\d+)$/);
    if (method === 'GET' && payStatusMatch) {
      return { paymentId: Number(payStatusMatch[1]), status: 'SUCCESS' };
    }

    return rejectErr('Mock: 未匹配的接口 ' + method + ' ' + url);
  });
}

function rejectErr(msg) {
  const err = new Error(msg);
  err.mock = true;
  throw err;
}

module.exports = { mockRequest };
