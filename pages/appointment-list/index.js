const { getMyOrders } = require('../../api/order');
const { normalizeList, orderStatusName } = require('../../utils/format');
Page({ data: { orders: [] }, onShow() { this.load(); }, load() { getMyOrders({ page: 1, size: 50 }).then((data) => this.setData({ orders: normalizeList(data).map((item) => ({ ...item, statusText: orderStatusName(item) })) })); } });
