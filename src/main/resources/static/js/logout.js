const logout = {
    init: function () {
        const _this = this;
        $('#logout-but').on('click', function () {
            _this.logout();
        });

    },
    logout: function () {
        localStorage.removeItem('access');
        localStorage.removeItem('refresh');
        alert('로그아웃에 성공하셨습니다.');
    }
}
logout.init();