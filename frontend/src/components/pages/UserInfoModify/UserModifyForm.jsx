import { Box, TextField, Button } from "@mui/material";
import React, { useState } from "react";
import styles from "./UserModifyForm.module.css";
import Api from "../../../functions/customApi";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from 'recoil';
import { removeCookie } from "../../../functions/cookie";
import { userState } from "../../../functions/GlobalState";

const UserModifyForm = (): JSX.Element => {
  const navigate = useNavigate();
  const setUser = useSetRecoilState(userState);
  const [userName, setUserName] = useState(localStorage.getItem('userName'));
  const [imageFile, setImageFile] = useState(localStorage.getItem('imageUrl'));
  const [fileList,setFileList] = useState([]); // 업로드한 파일들을 저장하는 배열
  const [removeClick,setRemoveClick] = useState(0);

  const onChangeUserName = (e) => {
    setUserName(e.target.value);
  };

  const changeView = (e: React.ChangeEvent<HTMLInputElement>) => {
    const fileReader = new FileReader();

    if (fileList) {
      fileReader.readAsDataURL(e.target.files[0]);
    }
    fileReader.onload = () => {
      setImageFile(fileReader.result);
    };
  }

  const onSaveFiles = (e: React.ChangeEvent<HTMLInputElement>) => {

    const uploadFiles = Array.prototype.slice.call(e.target.files); // 파일선택창에서 선택한 파일들

    if (fileList) {
      fileList.shift();
    }

    uploadFiles.forEach((uploadFile) => {
      fileList.push(uploadFile);
      setRemoveClick(0);
      console.log(fileList);
    });
  };

  const onDeleteImage = () => {
    setImageFile('https://ohy1023.s3.ap-northeast-2.amazonaws.com/basic.png');
    setFileList([]);
    setRemoveClick(1);
    console.log(fileList);
  }

  const onFileUpload = () => {
    console.log(fileList);
    const formData = new FormData();

    fileList.forEach((file) => {
      console.log(file);
      formData.append('image', file);
    });

    formData.append('userName', userName);
    formData.append('removeClick',removeClick);

    Api.post('/api/v1/users', formData)
      .then(function (response) {
        sessionStorage.setItem("temp",sessionStorage.getItem("temp") + 1)
        localStorage.setItem("userName",response.data.result.userName);
        localStorage.setItem("imageUrl",response.data.result.imageUrl);
        alert(" 회원 정보가 수정되었습니다.");
        navigate('/mypage');
      })
      .catch(function (error) {
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
                    value={localStorage.getItem("email")}
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
                  <br/><br/>
                  <h5 className={styles.glmt0}>프로필 변경</h5>
                  <div className={styles.Avatar}>
                    <img alt="" src={imageFile} />
                  </div>
                  <br />
                  <div className={styles.gltextgray500}>허용되는 최대 파일 크기는 10MB입니다.</div>
                  <div className={[styles.gldisplayflex, styles.glalignitemscenter, styles.glmy3].join(" ")}>
                    <label className={styles.imageSaveButton} type="button" htmlFor="user_avatar-trigger">
                      <span className={styles.glbuttontext}>이미지 선택</span>
                    </label>
                    <input onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                      changeView(e)
                      onSaveFiles(e)
                    }} className={styles.hidden} accept="image/*" type="file" id="user_avatar-trigger" />
                    <label type="button" className={styles.imageDeleteButton} onClick={onDeleteImage}>
                      <span className={styles.glbuttontext}>이미지 삭제</span>
                    </label>
                  </div>
                  <Button
                    onClick={onFileUpload}
                    type="submit"
                    fullWidth
                    variant="contained"
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