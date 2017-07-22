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

	this.deleted = false;
	this.products = [];

	this.toJSON = function() {
		return {
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
			CREATED_META : product.items,
			DATE_META : product.dateMeta,
			DELETED : product.deleted,
			PRICE : product.price,
			POINTS : product.points,
			IMAGE_URL : product.imageSrc,
			CATEGORY : product.category,
			OBJECT : product
		}
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