/**
 * Category entity
 */
var User = function() {
	this.id;
	this.username;
	this.firstName;
	this.lastName;
	this.dateMeta;
	this.remark;
	this.referralCode;
	this.contactMeta;
	this.subUsers;
	this.activateMeta;

	this.users = [];

	this.fromJSON = function(data) {
		this.id = data.id;
		this.username = data.username;
		this.firstName = data.firstName;
		this.lastName = data.lastName;
		this.dateMeta = data.dateMeta;
		this.remark = data.remark;
		this.referralCode = data.referralCode;
		this.contactMeta = data.contactMeta;
		this.subUsers = data.subUsers;
		this.activateMeta = data.activate;
		this.addUser(this.toRows(this));
	};

	this.toRows = function(user) {
		return {
			ID : user.id,
			USERNAME : user.username,
			FIRST_NAME : user.firstName,
			LAST_NAME : user.lastName,
			DATE_META : user.dateMeta,
			REMARK : user.remark,
			REFERRAL_CODE : user.referralCode,
			CONTACT_META : user.contactMeta,
			SUB_USERS : user.subUsers,
			ACTIVATE_META : user.activateMeta
		}
	};

	this.addUser = function(user) {
		if (this.users.indexOf(user) == -1) {
			this.users.push(user);
		}
	};

	this.clear = function() {
		while (this.users.length > 0) {
			this.users.pop();
		}
	};

};