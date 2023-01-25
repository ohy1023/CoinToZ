const logout = {
    init: function () {
        const _this = this;
        $('#logout-but').on('click', function () {
            _this.logout();
        });

    },
    logout: function () {
        const accessToken = localStorage.getItem("access");
        const base64Payload = accessToken.split('.')[1]; //value 0 -> header, 1 -> payload, 2 -> VERIFY SIGNATURE
        const base64 = base64Payload.replace(/-/g, '+').replace(/_/g, '/');
        const payload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        const result = JSON.parse(payload)

        if (result.exp < new Date() / 1000) {
            console.log('The token has expired');
            const refreshToken = localStorage.getItem("refresh");
            $.ajax({
                type: "GET",
                url: "/api/v1/users/logout",
                contentType: 'application/json',
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Bearer " + accessToken);//header추가
                    request.setRequestHeader("Authorization-refresh", "Bearer " + refreshToken);//header추가
                }
            }).done(function (data, textStatus, response) {
                localStorage.setItem("access", response.getResponseHeader("Authorization"));
                localStorage.setItem("refresh", response.getResponseHeader("Authorization-refresh"));
                alert("토큰이 만료되어 재설정 되었습니다. 재요청 해주세요.");
            });
        } else {
            console.log('The token is still valid');
            $.ajax({
                type: "GET",
                url: "/api/v1/users/logout",
                contentType: 'application/json',
                beforeSend: function (request) {
                    request.setRequestHeader("Authorization", "Bearer " + accessToken);//header추가
                },
                success: function (res) {
                    alert("SUCCESS");
                }
            });

        }
        localStorage.removeItem('access');
        localStorage.removeItem('refresh');
        alert('로그아웃에 성공하셨습니다.');
    }
}
logout.init();