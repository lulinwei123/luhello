
$(function() {
    validateRule();
	//背景效果
	$('.header').circleMagic({
					radius: 36,
					density: .1,
					color: 'rgba(255, 248, 220, 0.8)',
					clearOffset: .9
				});
});

$.validator.setDefaults({
    submitHandler: function() {
		login();
    }
});

//ajax提交
// function login() {
// 	$.modal.loading($("#btnSubmit").data("loading"));//打开遮罩层
// 	var username = $.common.trim($("input[name='username']").val());//$.trim 删除字符串开始和末尾的空格
//     var password = $.common.trim($("input[name='password']").val());
//   $.ajax({
//       type: "post",
//       cache: true,
//       async:false,
//       datatype:"json",
//       url: "/main",
//       data: {
//           "username": username,
//           "password": password
//       },
//       success: function(data) {
//           if (data.code == 0) {
//
//           }
//       }
//   });
// }
function login() {
    $.ajax({
        dataType: 'json', // 注意：这里是指希望服务端返回json格式的数据-->
        cache: true,
        async: false,
        type: 'POST',
        data: $('#signupForm').serialize(),
        url: '/main',
        error: function () {
            alert('请求失败');
        },
        success: function (data) {
            if (data.code == 0) {
                window.location.href = '/toPageMain';
            }

        }
    });
}

if (window != top)
    top.location.href = location.href;


//表单验证
function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true,
                rangelength:[3,10]
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
                rangelength: icon + "密码介于3-10位数字"
            }
        }
    })
}

