/** 
 * password field with strength
 * 
 * @author Jad Haddouch <jad.haddouch@topnetworksa.ch>
 * @docauthor Jad Haddouch <jad.haddouch@topnetworksa.ch>
 * @copyright Copyright 2015 Topnetwork
 */ 
Ext.define('Ext.ux.form.field.PassWord', {
    extend: 'Ext.form.field.Text',
    alias: 'widget.tnwpassword',
	
	inputType: 'password',
	
	bonus_cfg: {
		'a-z': { char: '[a-z]', bonus: 25 },
		'A-Z': { char: '[A-Z]', bonus: 25 },
		'special_char' : { char: '[\\§\\+\\-\\*\\/\\=\\:\\_\\<\\>\\$\\@\\&\\€\\!\\?\\;\\%]', bonus: 25 },
		'length_max' : { char: 8, bonus: 25, spe_treatment: true },
		'length_min' : { char: 5, bonus: 25, spe_treatment: true }
	},
	
	labelableRenderTpl: [
		// body row. If a heighted Field (eg TextArea, HtmlEditor, this must greedily consume height.
		'<tr role="presentation" id="{id}-inputRow" <tpl if="inFormLayout">id="{id}"</tpl> class="{inputRowCls}">',
	
			// Label cell
			'<tpl if="labelOnLeft">',
				'<td role="presentation" id="{id}-labelCell" style="{labelCellStyle}" {labelCellAttrs}>',
					'{beforeLabelTpl}',
					'<label id="{id}-labelEl" {labelAttrTpl}<tpl if="inputId"> for="{inputId}"</tpl> class="{labelCls}"',
						'<tpl if="labelStyle"> style="{labelStyle}"</tpl>',
						// Required for Opera
						' unselectable="on"',
					'>',
						'{beforeLabelTextTpl}',
						'<tpl if="fieldLabel">{fieldLabel}{labelSeparator}</tpl>',
						'{afterLabelTextTpl}',
					'</label>',
					'{afterLabelTpl}',
				'</td>',
			'</tpl>',
	
			// Body of the input. That will be an input element, or, from a TriggerField, a table containing an input cell and trigger cell(s)
			'<td role="presentation" class="{baseBodyCls} {fieldBodyCls} {extraFieldBodyCls}" id="{id}-bodyEl" colspan="{bodyColspan}" role="presentation">',
				'{beforeBodyEl}',
	
				// Label just sits on top of the input field if labelAlign === 'top'
				'<tpl if="labelAlign==\'top\'">',
					'{beforeLabelTpl}',
					'<div role="presentation" id="{id}-labelCell" style="{labelCellStyle}">',
						'<label id="{id}-labelEl" {labelAttrTpl}<tpl if="inputId"> for="{inputId}"</tpl> class="{labelCls}"',
							'<tpl if="labelStyle"> style="{labelStyle}"</tpl>',
							// Required for Opera
							' unselectable="on"',
						'>',
							'{beforeLabelTextTpl}',
							'<tpl if="fieldLabel">{fieldLabel}{labelSeparator}</tpl>',
							'{afterLabelTextTpl}',
						'</label>',
					'</div>',
					'{afterLabelTpl}',
				'</tpl>',
	
				'{beforeSubTpl}',
				'{[values.$comp.getSubTplMarkup(values)]}',
				'{afterSubTpl}',
				
				'<div id="pass_word_strength_{inputId}" style="background:-webkit-gradient(linear, 100% 100%, 0% 0%, from(#00FF00), to(#FF0000)); height: 2px;">',
				' <div id="cache_pass_word_strength_{inputId}" style="float: right; background-color: #FFF; height: 100%"></div>',
				'</div>',
				
			// Final TD. It's a side error element unless there's a floating external one
			'<tpl if="msgTarget===\'side\'">',
				'{afterBodyEl}',
				'</td>',
				'<td role="presentation" id="{id}-sideErrorCell" vAlign="{[values.labelAlign===\'top\' && !values.hideLabel ? \'bottom\' : \'middle\']}" ',
				'	style="{[values.autoFitErrors ? \'display:none\' : \'\']}" width="{errorIconWidth}"',
				'>',
					'<div role="presentation" id="{id}-errorEl" class="{errorMsgCls}" style="display:none"></div>',
				'</td>',
			'<tpl elseif="msgTarget==\'under\'">',
				'<div role="presentation" id="{id}-errorEl" class="{errorMsgClass}" colspan="2" style="display:none"></div>',
				'{afterBodyEl}',
				'</td>',
			'</tpl>',
		'</tr>',
		{
			disableFormats: true
		}
	],
	
	initComponent: function () {
        var me = this;
		
		me.on('change', function () {
			Ext.get('cache_pass_word_strength_' + me.getInputId()).setWidth(me.getScore() + '%');
		});
		
		me.on('afterrender', function () {
			Ext.get('cache_pass_word_strength_' + me.getInputId()).setWidth('100%');
		});


        me.callParent(arguments);
	},
	
	getScore: function () {
		var me = this,
			value = me.getValue(),
			score = 100;
			
		if (value.length > me.bonus_cfg['length_max'].char) { score -= me.bonus_cfg['length_max'].bonus; }
		if (value.length < me.bonus_cfg['length_min'].char) { score += me.bonus_cfg['length_min'].bonus; }
		
		Ext.Object.each(me.bonus_cfg, function (k, cfg) { 
			if (!cfg.spe_treatment || cfg.spe_treatment === false) {
				if (value.search(new RegExp(cfg.char)) > -1) { 
					score -= cfg.bonus; 
				}
			}
		});
		
		return score > 100 ? 100 : score;
	}
});