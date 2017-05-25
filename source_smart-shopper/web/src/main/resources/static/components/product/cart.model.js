/**
 * Cart entity
 */
var Cart = function() {
	this.id;
	this.date;
	this.status;
	this.user;
	this.product;

	this.carts = [];

	this.toJSON = function(productId, quantity) {
		return {
			"productId" : productId,
			"quantity" : quantity
		};
	};

	this.fromJSON = function(data) {
		this.id = data.id;
		this.date = data.date;
		this.status = data.status;
		this.user = data.user;
		this.products = data.products;
		this.addRow(this.toRows(this));
	};

	this.toRows = function(category) {
		return {
			ID : category.id,
			DATE : category.date,
			USER : category.user,
			PRODUCTS : category.products
		}
	};

	this.addRow = function(cart) {
		if (this.carts.indexOf(cart) == -1) {
			this.carts.push(cart);
		}
	};

	this.clear = function() {
		while (this.carts.length > 0) {
			this.carts.pop();
		}
	};

};