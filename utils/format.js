const LAND_STATUS = {
  AVAILABLE: '可租',
  CLAIMED: '已租',
  RENTED: '已租',
  MAINTENANCE: '维护中',
  EXPIRING: '即将到期',
  EXPIRED: '已到期'
};

const ORDER_STATUS = {
  PENDING_PAY: '待支付',
  PAID: '已支付',
  PROCESSING: '服务中',
  COMPLETED: '已完成',
  CANCELLED: '已取消'
};

const SERVICE_TYPES = [
  { label: '播种', value: 'SEEDING' },
  { label: '除草', value: 'WEEDING' },
  { label: '施肥', value: 'FERTILIZE' },
  { label: '采摘', value: 'HARVEST' },
  { label: '翻土', value: 'PLOUGH' },
  { label: '病虫害处理', value: 'PEST_CONTROL' },
  { label: '浇水', value: 'WATER' }
];

function money(value) {
  return Number(value || 0).toFixed(2);
}

function landStatusName(item) {
  return item.statusName || LAND_STATUS[item.status] || item.status || '未知';
}

function orderStatusName(item) {
  return item.statusName || ORDER_STATUS[item.status] || item.status || '未知';
}

function normalizeList(data) {
  if (Array.isArray(data)) return data;
  if (!data) return [];
  return data.records || data.list || data.items || data.content || [];
}

module.exports = { LAND_STATUS, ORDER_STATUS, SERVICE_TYPES, money, landStatusName, orderStatusName, normalizeList };
