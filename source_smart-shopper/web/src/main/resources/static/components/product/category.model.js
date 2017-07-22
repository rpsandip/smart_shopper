/**
 * Category entity
 */
var Category = function() {
	this.id;
	this.name;
	this.remark;
	this.createdMeta;
	this.dateMeta;
	this.superCategory;

	this.categories = [];
	this.products = [];

	this.toJSON = function() {
		return {
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
		this.products = data.products;
		this.superCategory = data.superCategory;
		this.addCategory(this.toRows(this));
	};

	this.toRows = function(category) {
		return {
			ID : category.id,
			NAME : category.name,
			REMARK : category.remark,
			CREATED_META : category.items,
			DATE_META : category.dateMeta,
			PRODUCTS : category.products,
			SUPER_CATEGORY : category.superCategory
		}
	};

	this.addCategory = function(category) {
		if (this.categories.indexOf(category) == -1) {
			this.categories.push(category);
		}
	};

	this.clear = function() {
		while (this.categories.length > 0) {
			this.categories.pop();
		}
	};

};