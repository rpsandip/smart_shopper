/**
 * Category entity
 */
var Category = function() {
	this.id;
	this.name;
	this.remark;
	this.createdMeta;
	this.dateMeta;
	this.isEdit = false;

	this.categories = [];

	this.toJSON = function() {
		return {
			"id" : this.id,
			"name" : this.name,
			"remark" : this.remark
		};
	};

	this.fromJSON = function(data) {
		this.id = data.id;
		this.name = data.name;
		this.remark = data.remark;
		this.createdmeta = data.createdMeta;
		this.dateMeta = data.dateMeta;

		this.addCategory(this.toRows(this));
	};

	this.toRows = function(category) {
		return {
			ID : category.id,
			NAME : category.name,
			REMARK : category.remark,
			CREATED_META : category.items,
			DATE_META : category.dateMeta
		}
	};

	this.addCategory = function(category) {
		if (this.categories.indexOf(category) == -1) {
			this.categories.push(category);
		}
	};

	this.editCategory = function(row) {
		if (!row) {
			return;
		}
		this.id = row.ID;
		this.name = row.NAME;
		this.remark = row.REMARK;
		this.createdMeta = row.CREATED_META;
		this.dateMeta = row.DATE_META;
		this.isEdit = true;
	};

	this.clear = function() {
		while (this.categories.length > 0) {
			this.categories.pop();
		}
	};

};