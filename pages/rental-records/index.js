const { getMyOrders } = require('../../api/order');
const { normalizeList, money } = require('../../utils/format');
Page({ data: { orders: [] }, onShow() { getMyOrders({ page: 1, size: 50 }).then((data) => this.setData({ orders: normalizeList(data).map((item) => ({ ...item, amountText: money(item.amount) })) })); } });
