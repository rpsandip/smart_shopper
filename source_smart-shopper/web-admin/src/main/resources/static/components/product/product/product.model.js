/**
 * Item entity
 */
var Product = function() {
	this.id;
	this.name;
	this.remark;
	this.dateMeta;
	this.createdMeta;
	this.price;
	this.points;
	this.path;
	this.imageSrc;
	this.category;
	this.categories = [];
	this.selectedCategory;
	this.isEdit = false;

	this.deleted = false;
	this.products = [];

	this.toJSON = function() {
		return {
			"id" : this.id,
			"name" : this.name,
			"remark" : this.remark,
			"price" : this.price,
			"points" : this.points,
			"categoryId" : this.selectedCategory.ID
		};
	};

	this.fromJSON = function(data) {
		this.id = data.id;
		this.name = data.name;
		this.remark = data.remark;
		this.createdmeta = data.createdMeta;
		this.dateMeta = data.dateMeta;
		this.price = data.price;
		this.points = data.points;
		this.deleted = data.deleted;
		this.imageSrc = data.productURL;
		this.category = data.category;

		this.addProduct(this.toRows(this));
	};

	this.toRows = function(product) {
		return {
			ID : product.id,
			NAME : product.name,
			REMARK : product.remark,
			DELETED : product.deleted,
			CREATED_META : product.items,
			DATE_META : product.dateMeta,
			PRICE : product.price,
			POINTS : product.points,
			IMAGE_URL : product.imageSrc,
			CATEGORY : product.category,
			OBJECT : product
		}
	};

	this.editProduct = function(row) {
		if (!row) {
			return;
		}

		var category = new Category();
		this.id = row.ID;
		this.name = row.NAME;
		this.remark = row.REMARK;
		this.createdMeta = row.CREATED_META;
		this.dateMeta = row.DATE_META;
		this.price = row.PRICE;
		this.points = row.POINTS;
		this.imageSrc = row.IMAGE_URL;
		category.fromJSON(row.CATEGORY);
		this.selectedCategory = category.toRows(category);
		this.isEdit = true;
	};

	this.addProduct = function(product) {
		if (this.products.indexOf(product) == -1) {
			this.products.push(product);
		}
	};

	this.clear = function() {
		while (this.products.length > 0) {
			this.products.pop();
		}
	};

};