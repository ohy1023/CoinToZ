const join = {
    init: function () {
        const _this = this;
        $('#join-but').on('click', function () {
            _this.join();
        });

    },
    join: function () {
        const data = {
            userName: $('#userName').val(),
            password: $('#password').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/users/join',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('회원가입에 성공하셨습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

join.init();