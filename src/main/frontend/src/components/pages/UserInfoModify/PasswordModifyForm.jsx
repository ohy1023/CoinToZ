import styles from "../Mypage/Mypage.module.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import FormHelperTexts from '@mui/material/FormHelperText';
import Api from "../util/customApi";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { removeCookie } from "../util/cookie";
import { useSetRecoilState } from "recoil";
import { userState } from "../util/GlobalState";

const PasswordModiftForm = () => {

  const [passwordState, setPasswordState] = useState('');
  const [newPasswordState, setNewPasswordState] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const setUser = useSetRecoilState(userState);
  const navigate = useNavigate();

  const logoutUser = async () => {
    await Api.get("/api/v1/users/logout")
      .then(function (response) {
        removeCookie('access');
        removeCookie('refresh');
        localStorage.removeItem('email');
        localStorage.removeItem('userName');
        localStorage.removeItem('imageUrl');
        localStorage.removeItem('createAt');
        setUser('');
        alert("로그아웃이 완료되었습니다.");
        navigate('/');
      })
      .catch(function (err) {
        console.log(err);
        alert("로그아웃 실패!");
      });
  };


  const onhandlePost = async (data) => {
    const { password, newPassword, reNewPassword } = data;
    const putData = { password, newPassword, reNewPassword };

    // post
    await Api
      .put('/api/v1/users/password', putData)
      .then(function (response) {
        alert(response.data.result)
        logoutUser();
      })
      .catch(function (err) {
        console.log(err);
        alert(err.response.data.result.message);
      });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const joinData = {

      password: data.get('password'),
      newPassword: data.get('newPassword'),
      reNewPassword: data.get('reNewPassword'),
    };
    const { password, newPassword, reNewPassword } = joinData;


    // 비밀번호 유효성 체크
    const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    if (!passwordRegex.test(password))
      setPasswordState('숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!');
    else setPasswordState('');

    if (!passwordRegex.test(newPassword))
      setNewPasswordState('숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!');
    else setNewPasswordState('');

    // 비밀번호 같은지 체크
    if (newPassword !== reNewPassword) setPasswordError('비밀번호가 일치하지 않습니다.');
    else setPasswordError('');



    if (
      passwordRegex.test(password) &&
      passwordRegex.test(newPassword) &&
      passwordRegex.test(reNewPassword) &&
      newPassword === reNewPassword
    ) {
      onhandlePost(joinData);
    }
  };

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}>
            <div className={styles.MemberCard}>
              <div className={styles.MemberCardHeader}>
                <h1>비밀번호 변경</h1>
              </div>
              <div className={styles.MemberCardBody}>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="password"
                      label="현재 비밀번호"
                      type="password"
                      id="password"
                      autoFocus
                      error={passwordState !== '' || false}
                    />
                    <FormHelperTexts>{passwordState}</FormHelperTexts>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="newPassword"
                      label="새 비밀번호"
                      type="password"
                      id="newPassword"
                      autoFocus
                      error={newPasswordState !== '' || false}
                    />
                    <FormHelperTexts>{newPasswordState}</FormHelperTexts>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="reNewPassword"
                      label="새 비밀번호 확인"
                      type="password"
                      id="reNewPassword"
                      error={passwordError !== '' || false}
                      autoFocus
                    />
                    <FormHelperTexts>{passwordError}</FormHelperTexts>
                    <Button
                      type="submit"
                      fullWidth
                      variant="contained"
                      sx={{ mt: 3, mb: 2 }}
                    >
                      비밀번호 변경
                    </Button>
                </Box>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}

export default PasswordModiftForm;
