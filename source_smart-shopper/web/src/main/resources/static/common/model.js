/**
 * Registration entity
 */
var Registration = function() {

	this.id;
	this.username;
	this.password;
	this.firstname;
	this.lastname;
	this.phoneno;
	this.street;
	this.city;
	this.state;
	this.country;
	this.pincode;
	this.subUsers;

	this.toJSON = function(referralCode) {
		return {
			"id" : this.id,
			"username" : this.username,
			"password" : this.password,
			"firstName" : this.firstname,
			"lastName" : this.lastname,
			"remark" : null,
			"parentId" : referralCode != undefined && referralCode != null ? referralCode
					: null,
			"contactBody" : {
				"street" : this.street,
				"city" : this.city,
				"state" : this.state,
				"country" : this.country,
				"postalCode" : this.pincode,
				"phoneNo" : this.phoneno
			}
		};
	};

	this.fromJSON = function(data) {
		this.id = data.id;
		this.username = data.username;
		this.firstname = data.firstName;
		this.lastname = data.lastName;
		this.phoneno = Number(data.contactMeta.phoneNo);
		this.street = data.contactMeta.street;
		this.city = data.contactMeta.city;
		this.state = data.contactMeta.state;
		this.country = data.contactMeta.country;
		this.pincode = data.contactMeta.postalCode;
		this.subUsers=data.subUsers;
	};
};