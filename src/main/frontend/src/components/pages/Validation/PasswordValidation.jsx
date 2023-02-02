import styles from "../Mypage/Mypage.module.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Api from "../util/customApi";
import { useNavigate } from "react-router-dom";

const PasswordValidation = () => {

  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const joinData = {
      password: data.get('password')
    };

    onhandlePost(joinData)

  };

  const onhandlePost = async (data) => {
    const postData = data

    // post
    await Api
      .post('/api/v1/users/password', postData)
      .then(function (response) {
        alert("비밀번호 일치.")
        navigate('/mypage/info/modify');
      })
      .catch(function (error) {
        console.log(error.response.data.result.message);
        alert(error.response.data.result.message)
      });
  };

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}>
            <div className={styles.MemberCard}>
              <div className={styles.MemberCardHeader}>
                <h1>비밀번호 재확인</h1>
              </div>
              <div className={styles.MemberCardBody}>
                <p className="tw-text-gray-900 tw-mb-4 tw-text-sm">회원의 정보를 안전하게 보호하기 위해 비밀번호를 다시 한번 확인 합니다.</p>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                  <TextField
                    margin="normal"
                    aria-readonly
                    fullWidth
                    id="email"
                    value={localStorage.getItem("email")}
                    name="email"
                    autoComplete="email"
                  >
                  </TextField>
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    autoFocus
                    autoComplete="current-password"
                  />
                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                  >
                    확인
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

export default PasswordValidation;
