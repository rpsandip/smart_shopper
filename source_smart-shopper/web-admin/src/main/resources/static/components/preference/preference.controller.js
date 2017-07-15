app.controller('PreferenceController', function($http, $scope, $rootScope,
		$state, $location, $element, $window, DTDefaultOptions,
		DTOptionsBuilder, DTColumnDefBuilder, DefaultConstant, UtilityService,
		UserFactory, UserService, ContactFactory) {

	$scope.isContactList = true;

	var contact = new Contact();

	// for data table
	var vm = this;
	$scope.contact = contact;
	vm.contacts = contact.contacts;
	ContactFactory.set(contact);

	vm.dtOptions = DTOptionsBuilder.newOptions().withDisplayLength(100)
			.withDOM('ftp');

	$scope.onAddContactClick = function($event) {
		var contact = new Contact();
		$scope.contact = contact;
		$scope.isContactList = false;
	};

	$scope.onCancelContactClick = function($event) {
		$scope.isContactList = true;
	};

	$scope.onEditClick = function($event, row) {
		if (row == undefined || row == null) {
			UtilityService.showError("Please select any row.");
			return;
		}
		contact.edit(row);
		$scope.contact = contact;
		$scope.isContactList = false;
	};

	$scope.onSave = function($event, contact1) {
		if (contact1 == undefined || contact1 == null) {
			UtilityService.showError("Required values can not be null.");
			return;
		}
		$scope.isLoading = true;
		UserService.addPreferenceContact(contact1.toJSON(contact1.id),
				function(response, status) {
					$scope.isLoading = false;
					if (status == 401) {
						UtilityService.showError(response.message);
						return;
					}
					if (status != 200) {
						UtilityService.showError(response.message);
						return;
					}
					$window.location.reload();

				});
	};

	$scope.onTncSave = function($event, tnc) {
		if (tnc == undefined || tnc == null) {
			UtilityService.showError("Terms and condition can not be empty.");
			return;
		}

		var payload = {
			"tnc" : tnc
		};

		$scope.isLoading = true;
		UserService.updatePreferenceTnc(payload, function(response, status) {
			$scope.isLoading = false;
			if (status == 401) {
				UtilityService.showError(response.message);
				return;
			}
			if (status != 200) {
				UtilityService.showError(response.message);
				return;
			}
			$scope.tnc = response.tnc;
			
			new PNotify({
				title : 'Updated successfully',
				addclass : 'bg-success',
				text : "Terms & conditions updated."
			});
		});

	};

	$scope.isLoading = true;
	UserService.preferenceContacts(function(response, status) {
		$scope.isLoading = false;
		if (status == 401) {
			UtilityService.showError(response.message);
			return;
		}
		if (status != 200) {
			UtilityService.showError(response.message);
			return;
		}
		for (i in response) {
			contact.fromJSON(response[i]);
		}
	});

	$scope.isLoading = true;
	UserService.preferenceTnc(function(response, status) {
		$scope.isLoading = false;
		if (status == 401) {
			UtilityService.showError(response.message);
			return;
		}
		if (status != 200) {
			UtilityService.showError(response.message);
			return;
		}
		$scope.tnc = response.tnc;
	});

});

app.factory('ContactFactory', function() {
	var factory = {};
	var contact;

	factory.set = function(value) {
		contact = value;
	};

	factory.get = function() {
		return contact;
	};

	return factory;
});

var Contact = function() {

	this.id;
	this.contactName;
	this.contactNo;
	this.contactEmail;

	this.isEdit = false;

	this.contacts = [];

	this.fromJSON = function(data) {
		this.id = data.id;
		this.contactName = data.name;
		this.contactNo = data.phoneNo;
		this.contactEmail = data.emailId;
		this.add(this.toRows(this));
	};

	this.toJSON = function(id) {
		return {
			"id" : id,
			"name" : this.contactName,
			"phoneNo" : this.contactNo,
			"emailId" : this.contactEmail
		};
	};

	this.toRows = function(contact) {
		return {
			ID : contact.id,
			CONTACT_NAME : contact.contactName,
			CONTACT_NO : contact.contactNo,
			CONTACT_EMAIL : contact.contactEmail,
		}
	};

	this.edit = function(row) {
		if (!row) {
			return;
		}
		this.id = row.ID;
		this.contactName = row.CONTACT_NAME;
		this.contactNo = row.CONTACT_NO;
		this.contactEmail = row.CONTACT_EMAIL;
		this.isEdit = true;
	};

	this.add = function(contact) {
		if (this.contacts.indexOf(contact) == -1) {
			this.contacts.push(contact);
		}
	};

	this.clear = function() {
		while (this.contacts.length > 0) {
			this.contacts.pop();
		}
	};
};