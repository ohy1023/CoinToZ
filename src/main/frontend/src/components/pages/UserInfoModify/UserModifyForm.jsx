import { Box, TextField, Button } from "@mui/material";
import React, { useState } from "react";
import styles from "../Mypage/Mypage.module.css";
import Api from "../util/customApi";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from 'recoil';
import { removeCookie } from "../util/cookie";
import { userState } from "../util/GlobalState";

const UserModifyForm = (): JSX.Element => {
  const navigate = useNavigate();
  const setUser = useSetRecoilState(userState);
  const [userName,setUserName] = useState(localStorage.getItem('userName'));

  const onChangeUserName = (e) => {
    setUserName(e.target.value);
  };

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
  const fileList: File[] = []; // 업로드한 파일들을 저장하는 배열

  const onSaveFiles = (e: React.ChangeEvent<HTMLInputElement>) => {
    const uploadFiles = Array.prototype.slice.call(e.target.files); // 파일선택창에서 선택한 파일들

    uploadFiles.forEach((uploadFile) => {
      fileList.push(uploadFile);
    });
  };

  const onFileUpload = async () => {
    const formData = new FormData();

    fileList.forEach((file) => {
      console.log(file);
      formData.append('image', file);
    });

    formData.append('userName', userName);

    await Api.post('/api/v1/users/modify', formData)
    .then(function(response){
      alert(" 회원 정보가 수정되었습니다.\n 다시 로그인 해주세요.");
      logoutUser();
    })
    .catch(function(error){
      alert("실패");
    });
  };

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}>
            <div className={styles.MemberCard}>
              <div className={styles.MemberCardHeader}>
                <h1>회원정보 수정/조회</h1>
              </div>
              <div className={styles.MemberCardBody}>
                <Box noValidate sx={{ mt: 1 }}>
                  <TextField
                    required
                    aria-readonly="true"
                    fullWidth
                    type="email"
                    id="email"
                    name="email"
                    label="이메일"
                    defaultValue={localStorage.getItem("email")}
                  />
                  <br /><br />
                  <TextField
                    required
                    fullWidth
                    id="userName"
                    name="userName"
                    label="이름"
                    defaultValue={localStorage.getItem("userName")}
                    onChange={onChangeUserName}
                    autoFocus
                  />
                  <br /><br />
                  <p>프로필 변경</p>
                  <div>
                    <input type="file" onChange={onSaveFiles} />
                  </div>

                  <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    onClick={onFileUpload}
                    sx={{ mt: 3, mb: 2 }}
                  >
                    회원정보 수정
                  </Button>
                </Box>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  );
};

export default UserModifyForm;