import styles from "../pages/Mypage/Mypage.module.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Api from "../pages/util/customApi";
import { useNavigate } from "react-router-dom";

const PasswordModiftForm = () => {

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
      .post('/api/v1/users/password/validation', postData)
      .then(function (response) {
        alert("비밀번호 변경 완료.")
        navigate('/mypage');
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
                  />
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    name="newPassword"
                    label="새 비밀번호"
                    type="newPassword"
                    id="newPassword"
                    autoFocus
                  />
                  <TextField
                    margin="normal"
                    required
                    fullWidth
                    name="reNewPassword"
                    label="새 비밀번호 확인"
                    type="reNewPassword"
                    id="reNewPassword"
                    autoFocus
                  />
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
