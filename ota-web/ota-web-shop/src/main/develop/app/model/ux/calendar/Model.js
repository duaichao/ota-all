Ext.define('Weidian.model.ux.calendar.Model', {
	extend: 'Weidian.model.Base',
	config: {
		fields: [
	        {name: 'date', type: 'date'},
	        {name: 'today', type: 'boolean'},
	        {name: 'unselectable', type: 'boolean'},
	        {name: 'selected', type: 'boolean'},
	        {name: 'prevMonth', type: 'boolean'},
	        {name: 'nextMonth', type: 'boolean'},
	        {name: 'weekend', type: 'boolean'},
	        'timeSlots'
        ]
    }
});