import styles from "../Mypage/Mypage.module.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import TempPassword from "./TempPassword";
import { useState } from "react";
import axios from "axios";

const NewPassword = () => {

  const [modalShow, setModalShow] = useState(false);
  const [email, setEmail] = useState('');

  function ModalHandler() {
    setModalShow(true)
  }

  function getEmail(value) {
    setEmail(value);
  }

  const emailAuth = async () => {
    await axios.get('api/v1/emails/send', {
      params: { email }
    }) // url 수정 필요
        .then(function(response){
          console.log(response.data);
          alert("인증번호가 발송되었습니다.");
          ModalHandler();
        })
        .catch(function(error){
          console.log(error);
          alert("이메일을 다시 확인해주세요.");
        })
  }

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}>
            <div className={styles.MemberCard}>
              <div className={styles.MemberCardHeader}>
                <h1>임시 비밀번호 발급</h1>
              </div>
              <div className={styles.MemberCardBody}>
                <p className="tw-text-gray-900 tw-mb-4 tw-text-sm">임시 비밀번호 발급을 위해 이메일 인증이 필요합니다.</p>
                <Box noValidate sx={{ mt: 1 }}>
                  <TextField
                    margin="normal"
                    aria-readonly
                    fullWidth
                    required
                    id="email"
                    name="email"
                    label="Email"
                    autoFocus
                    autoComplete="email"
                    onChange={(e) => {
                      getEmail(e.target.value);
                    }}
                  >
                  </TextField>
                  <Button
                    type="button"
                    onClick={emailAuth}
                    fullWidth
                    variant="contained"
                    sx={{ mt: 3, mb: 2 }}
                  >
                    인증
                  </Button>
                  <TempPassword
                    email={email}
                    show={modalShow}
                    onHide={() => setModalShow(false)}
                  />
                </Box>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}

export default NewPassword;
