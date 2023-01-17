const login = {
    init: function () {
        const _this = this;
        $('#login-but').on('click', function () {
            _this.login();
        });

    },
    login: function () {
        const data = {
            userName: $('#userName').val(),
            password: $('#password').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/users/login',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('로그인에 성공하셨습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

login.init();