/**
 * @licence http://www.gnu.org/licenses/gpl.txt GPLv3
 * @author Éric Ortega <eric@planysphere.fr>
 * @since 2013-07-25 02:10
 *
 * @version 2013-07-28 18:38
 * Small performance tweak (using vanilla loops instead of each methods).
 */
Ext.define('Ext.ux.grid.column.RadioColumn', {
	extend: 'Ext.grid.column.CheckColumn'

	,alternateClassName: 'Ext.ux.RadioColumn'

	,alias: 'widget.radiocolumn'

	/**
	 * @cfg {String} groupField
	 *
	 * Name of the field used for radio groups. If left undefined, this will default to the store's
	 * {@link Ext.data.Store#groupField}, and if this is undefined as well, then the whole data set
	 * will be considered as one and only group.
	 */
	,groupField: undefined

	/**
	 * @cfg {Boolean}
	 *
	 * True to allow unchecking an item by click on it when it is selected. If left to false, then
	 * an item can only be deselected by selecting another one in the group.
	 */
	,allowUncheck: false

	,renderer : function(value, meta) {
		var cssPrefix = Ext.baseCSSPrefix,
			cls = [
				cssPrefix + 'form-radio-default', // for radio image
				cssPrefix + 'form-field' // for disabled style
			];

		if (this.disabled) {
			meta.tdCls += ' ' + this.disabledCls;
		}
		if (value) {
			meta.tdCls += ' ' + cssPrefix + 'form-cb-checked';
		}

		return '<img class="' + cls.join(' ') + '" src="' + Ext.BLANK_IMAGE_URL + '"/>';
	}

	,initComponent: function() {

		this.callParent(arguments);

		this.on({
			scope: this
			,checkchange: this.onCheckChange
			,beforecheckchange: this.onBeforeCheckChange
		});
	}

	// private
	,onBeforeCheckChange: function(col, index, checked) {
		if (!checked && !this.allowUncheck) {
			return false;
		}
	}

	// private
	,onCheckChange: function(col, index, checked) {

		if (!checked) {
			return;
		}

		var dataIndex = this.dataIndex,
			grid = this.up('tablepanel'),
			store = grid.getStore(),
			record = store.getAt(index),
			groupField = this.groupField || store.groupField,
			group = groupField && record.get(groupField) || undefined,
			groupItems = group
				? store.query(groupField, group).items
				: store.getRange(),
			i = groupItems.length,
			r;
		while (i--) {
			r = groupItems[i];
			if (r !== record) {
				r.set(dataIndex, false);
			}
			r.commit();
		}

		this.fireEvent('radiocheckchange', this, index, record, group);
	}

});
