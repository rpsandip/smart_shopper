/**
 * Item entity
 */
var Item = function() {
	this.code;
	this.name;
	this.desc;
	this.image;
	this.imageSrc;
	this.price
	this.isEdit = false;
	this.deleted = false;

	this.items = [];

	this.fromJSON = function(data) {
		this.code = data.code;
		this.name = data.name;
		this.desc = data.description;
		this.imageSrc = data.imageSrc;
		this.price = data.price;
		this.deleted = data.deleted;
		if (!this.isEdit) {
			this.addItems(this.toRows(this));
		} else {
			this.updateItem(this.toRows(this));
		}
	};

	this.toJSON = function() {
		return {
			"code" : !this.isEdit ? "ITM-" + this.code : this.code,
			"name" : this.name,
			"description" : this.desc,
			"price" : this.price,
			"deleted" : this.deleted
		};
	};

	this.toRows = function(item) {
		return {
			CODE : item.code,
			NAME : item.name,
			DESC : item.desc,
			PRICE : item.price,
			IMAGE : item.imageSrc,
			DELETED : item.deleted
		}
	};

	this.editRow = function(row) {
		if (!row) {
			return;
		}
		this.code = row.CODE;
		this.name = row.NAME;
		this.desc = row.DESC;
		this.price = row.PRICE;
		this.imageSrc = row.IMAGE;
		this.deleted = row.DELETED;
		this.isEdit = true;
	};

	this.addItems = function(item) {
		if (this.items.indexOf(item) == -1) {
			this.items.push(item);
		}
	};

	this.updateItem = function(item) {
		for (i in this.items) {
			if (this.items[i].CODE === item.CODE) {
				this.items[i].CODE = item.CODE;
				this.items[i].NAME = item.NAME;
				this.items[i].DESC = item.DESC;
				this.items[i].PRICE = item.PRICE;
				this.items[i].IMAGE = item.IMAGE;
				this.items[i].DELETED = item.DELETED;
				this.items[i].IMAGE = item.IMAGE;
				break;
			}
		}
	};

	this.clear = function() {
		while (this.items.length > 0) {
			this.items.pop();
		}
	};

};