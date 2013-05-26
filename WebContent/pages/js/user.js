$(document).ready(function() {

	var userId = $.cookie("user_id");

	$("#update_user").click(function() {
		var email = $("#email").val();
		var password = $("#password").val();
		var firstName = $("#firstName").val();
		var lastName = $("#lastName").val();
		if (!email || !password) {
			alert("Fill email and password fields");
			return;
		}
		var user = {
			id : userId,
			email : email,
			password : password,
			firstName : firstName,
			lastName : lastName
		};
		$.ajax({
			url : "users/" + userId,
			type : "PUT",
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify(user),
			dataType : "json",
			success : function(user) {
				refreshUsersTable(user);
			},
			error : function(data) {
				alert(data.responseText);
			}
		});
	});
	
	$("#delete_user").click(function(){
		if(!confirm("Are you sure?")){
			return;
		}
		$.ajax({
			url : "users/" + userId,
			type : "DELETE",
			success : function(user) {
				alert("Your account was deleted");
				location.href = "logout";
			},
			error : function(data) {
				alert(data.responseText);
			}
		});
	});
	
	$.getJSON("users/" + userId, function(user) {
		fillFields(user);
	});
});

function fillFields(user) {
	$("#user_name").html(user.email);
	$("#email").val(user.email);
	$("#firstName").val(user.firstName);
	$("#lastName").val(user.lastName);
}