import { Box, TextField, Button } from "@mui/material";
import React, { ChangeEvent, useState } from "react";
import styled from "styled-components";
import styles from "../pages/Mypage/Mypage.module.css";
import Api from "../pages/util/customApi";
import { useNavigate } from "react-router-dom";
import { useSetRecoilState } from 'recoil';
import { removeCookie } from "../pages/util/cookie";
import { userState } from "../pages/util/GlobalState";

const SImageUploaderWrapper = styled.div`
    padding: 7% 15%;
    box-sizing: border-box;
`;

const SImageUploaderFrame = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    border: 1px dashed #1e1e1e;
    padding: 15px 20px;
    flex-direction: column;
    box-sizing: border-box;
`;

const STitle = styled.div`
    font-size: 18px;
    color: #363636;
    text-align: center;
    padding: 50px 0 0 0;
    box-sizing: border-box;
`;

const SCustomButtonWrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 25px 0;
    box-sizing: border-box;
    flex-direction: column;
`;

const SCustomButton = styled.div`
    text-align: center;
    width: 150px;
    padding: 10px 15px;
    border-radius: 7px;
    cursor: pointer;
    margin: 0 0 20px 0;
    box-sizing: border-box;
    background-color: ${({ btnValue }) =>
    btnValue === "primary" ? "rgb(50, 111, 233)" : "rgb(238, 134, 131)"};
    color: #fff;
`;

const SCustomInput = styled.input`
    display: none;
`;

const SImageWrapper = styled.div`
    width: 50%;
    height: 50%;
    text-align: center;
    padding: 50px 15px;
`;

const SImageArea = styled.img`
    width: inherit;
    height: inherit;
`;

const SLoading = styled.div`
    padding: 20px;
    text-align: center;
`;

export default function UserModifyForm(): JSX.Element {
  const navigate = useNavigate();
  const setUser = useSetRecoilState(userState);
  const [imageFile, setImageFile] = useState({
    imageFile: localStorage.getItem("imageUrl"),
    viewUrl: localStorage.getItem("imageUrl")
  });

  const [loaded, setLoaded] = useState(false);

  let imageRef: any;

  const onChangeUploadHandler = (e: ChangeEvent<HTMLInputElement> | any): void => {
    console.log("사진 업로드 버튼 클릭");
    e.preventDefault();

    const fileReader = new FileReader();
    if (e.target.files[0]) {
      setLoaded(true);
      fileReader.readAsDataURL(e.target.files[0]);
    }
    fileReader.onload = () => {
      setImageFile({
        imageFile: e.target.files[0],
        viewUrl: fileReader.result
      });
      setLoaded(true);
    };

    console.log(imageFile.viewUrl);
    console.log(loaded);
  };

  const onClickDeleteHandler = (): void => {
    console.log("사진 삭제 버튼 클릭");
    setImageFile({
      imageFile: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
      viewUrl: "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png"
    });
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

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const joinData = {
      userName: data.get('userName'),
      imageUrl: data.get('imageUrl')
    };
    console.log(joinData);

    onhandlePost(joinData);

  };

  const onhandlePost = (data) => {
    const { userName, imageUrl } = data;
    const putData = { userName, imageUrl };


    // post
    Api
      .put('/api/v1/users', putData)
      .then(function (response) {
        alert("수정 완료 다시 로그인 해주세요.")
        logoutUser();
      })
      .catch(function (err) {
        console.log(err);
        alert("수정 실패");
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
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
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
                  <br/><br/>
                  <TextField
                    required
                    fullWidth
                    id="userName"
                    name="userName"
                    label="이름"
                    defaultValue={localStorage.getItem("userName")}
                    autoFocus
                  />
                  <br/><br/>
                  <TextField
                    required
                    fullWidth
                    id="imageUrl"
                    name="imageUrl"
                    label="이미지"
                    value={imageFile.viewUrl}
                  />
                  <SImageUploaderWrapper>
                    <SImageUploaderFrame>
                      <STitle>너무 큰 용량 올리면 서버 터짐 !</STitle>
                      <SCustomButtonWrapper>
                        <SImageWrapper>
                          {imageFile.imageFile !== "" ? (
                            <SImageArea src={imageFile.viewUrl} />
                          ) : (
                            <SLoading>Loading...</SLoading>
                          )}
                          <SCustomInput
                            type="file"
                            accept="image/*"
                            ref={(refer) => (imageRef = refer)}
                            onChange={onChangeUploadHandler}
                          />
                        </SImageWrapper>
                        <SCustomButton
                          btnValue={"primary"}
                          onClick={() => imageRef.click()}
                        >
                          사진 업로드
                        </SCustomButton>
                        <SCustomButton
                          btnValue={"danger"}
                          onClick={onClickDeleteHandler}
                        >
                          사진 삭제
                        </SCustomButton>
                      </SCustomButtonWrapper>
                    </SImageUploaderFrame>

                  </SImageUploaderWrapper>
                  <Button
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
}