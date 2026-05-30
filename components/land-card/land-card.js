const { landStatusName, money } = require('../../utils/format');
Component({
  properties: { land: { type: Object, value: {} } },
  data: { statusText: '', statusType: 'default', price: '0.00' },
  observers: {
    land(land) {
      const status = land.status;
      const statusType = status === 'AVAILABLE' ? 'available' : status === 'EXPIRING' ? 'warning' : status === 'EXPIRED' ? 'danger' : status === 'MAINTENANCE' ? 'warning' : 'rented';
      this.setData({ statusText: landStatusName(land), statusType, price: money(land.price) });
    }
  },
  methods: { onTap() { this.triggerEvent('tap', this.properties.land); } }
});
