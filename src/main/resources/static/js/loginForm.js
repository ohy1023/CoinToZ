const login = {
    init: function () {
        const _this = this;
        $('#login-but').on('click', function () {
            _this.login();
        });

    },
    login: function () {
        const data = {
            email: $('#email').val(),
            password: $('#password').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/users/login',
            contentType: 'application/json',
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