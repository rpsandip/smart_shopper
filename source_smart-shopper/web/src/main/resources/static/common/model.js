/**
 * Registration entity
 */
var Registration = function() {

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

	this.toJSON = function(referralCode) {
		return {
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
};