package cn.sd.entity.b2b;

import java.util.List;

public class ModuleEntity {
	private String id;
	private String parentId;
	private boolean leaf;
	private boolean expanded;
	private String text;
	//private String href;
	private String url;
	private String iconCls;
	private String glyph;
	private String isUse;
	private boolean  checked;
	private List<ModuleEntity> children;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(int leaf) {
		this.leaf = leaf==1;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(int expanded) {
		this.expanded = expanded==1;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public List<ModuleEntity> getChildren() {
		return children;
	}
	public void setChildren(List<ModuleEntity> children) {
		this.children = children;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked==1;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getGlyph() {
		return glyph;
	}
	public void setGlyph(String glyph) {
		this.glyph = glyph;
	}
}
