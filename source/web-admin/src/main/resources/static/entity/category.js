/**
 * Category entity
 */
var Category = function() {
	this.code;
	this.name;
	this.desc;
	this.items = [];
	this.isEdit = false;
	this.deleted = false;

	this.categories = [];

	this.fromJSON = function(data) {
		this.code = data.code;
		this.name = data.name;
		this.desc = data.description;

		var item = new Item();
		for (i = 0; i < data.items.length; i++) {
			item.fromJSON(data.items[i]);
		}
		this.items = item.items;
		this.deleted = data.deleted;

		if (!this.isEdit) {
			this.addCategory(this.toRows(this));
		} else {
			this.updateCategory(this.toRows(this));
		}

	};

	this.toJSON = function() {
		var itemCodes = [];
		for (i in this.items) {
			itemCodes.push(this.items[i].CODE);
		}

		return {
			"code" : !this.isEdit ? "CAT-" + this.code : this.code,
			"name" : this.name,
			"description" : this.desc,
			"items" : itemCodes,
			"deleted" : this.deleted
		};
	};

	this.toRows = function(category) {
		return {
			CODE : category.code,
			NAME : category.name,
			DESC : category.desc,
			ITEMS : category.items,
			DELETED : category.deleted
		}
	};

	this.editRow = function(row) {
		if (!row) {
			return;
		}
		this.code = row.CODE;
		this.name = row.NAME;
		this.desc = row.DESC;
		this.items = row.ITEMS;
		this.imageSrc = row.IMAGE;
		this.deleted = row.DELETED;
		this.isEdit = true;
	};

	this.addCategory = function(item) {
		if (this.categories.indexOf(item) == -1) {
			this.categories.push(item);
		}
	};

	this.updateCategory = function(item) {
		for (i in this.categories) {
			if (this.categories[i].CODE == item.CODE) {
				this.categories[i].CODE = item.CODE;
				this.categories[i].NAME = item.NAME;
				this.categories[i].DESC = item.DESC;
				this.categories[i].ITEMS = item.ITEMS;
				this.categories[i].DELETED = item.DELETED;
				this.categories[i].IMAGE = item.IMAGE;
				break;
			}
		}
	};

	this.clear = function() {
		while (this.categories.length > 0) {
			this.categories.pop();
		}
	};

};