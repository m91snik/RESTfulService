$(document).ready(function() {

	$("#forgot_password").click(function() {
		var email = $("#email").val();
		if (!email) {
			alert("Fill email field");
			return;
		}
		$.ajax({
			url : "forgot_password",
			type : "GET",
			data : {email : email},
			success : function(data) {
				alert("Email was sent. Please, check your email.")
			},
			error : function(data) {
				alert(data.responseText);
			}
		});
	});
});
