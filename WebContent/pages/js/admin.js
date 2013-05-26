$(document).ready(function() {

	$("#create_user").click(function() {
		var email = $("#email").val();
		var password = $("#password").val();
		var firstName = $("#firstName").val();
		var lastName = $("#lastName").val();
		if (!email || !password) {
			alert("Fill email and password fields");
			return;
		}
		var user = {
			email : email,
			password : password,
			firstName : firstName,
			lastName : lastName
		};
		$.ajax({
			url : "users",
			type : "POST",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(user),
			dataType : "json",
			success : function(data) {
				refreshUsersTable();
			},
			error : function(data) {
				alert(data.responseText);
			}
		});
	});
	refreshUsersTable();
});

function refreshUsersTable() {
	var usersTable = $("#users");
	usersTable.empty();
	usersTable
			.append("<tr><th>Email</th><th>First name</th><th>Last name</th></tr>");
	$.getJSON("users", function(users) {
		$.each(users, function(id, user) {
			usersTable.append("<tr><td>" + user.email + "</td><td>"
					+ user.firstName + "</td><td>" + user.lastName
					+ "</td></tr>");
		});
	});
}