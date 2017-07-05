
function loginRequest(username, password) {
	var loginInfo = new Object();
	loginInfo.username = username;
	loginInfo.password = password;
	$.ajax({
		url : "http://localhost:8080/login/do",
		async : true, // 请求是否异步，默认为异步，这也是ajax重要特性

		dataType : 'json',
		contentType : 'application/json',
		type : 'POST',
		data : JSON.stringify(loginInfo),
		success : function(data) {
			if ("0" == data.code) {
				//TODO  跳转功能页
				window.location.href = "views/main.html";
//				alert(data.desc)
			} else {
				alert(data.desc + "(" + data.code + ")");
			}

		},
		error : function() {
			alert("error");
		}
	});
}
function login() {

	var username = document.getElementById("username");
	var pass = document.getElementById("password");

	if (username.value == "") {

		alert("请输入用户名");

	} else if (pass.value == "") {

		alert("请输入密码");

		// } else if (username.value == "admin" && pass.value == "123456") {
		//
		// // window.location.href = "views/main.html";

	} else {
		loginRequest(username.value, pass.value);

		// alert("请输入正确的用户名和密码！")

	}
}
