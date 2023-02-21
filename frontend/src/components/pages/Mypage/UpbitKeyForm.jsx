import styles from "./UpbitKeyForm.module.css";
import { Box, TextField, Button } from "@mui/material";
import Api from "../../../functions/customApi";
import { useNavigate } from "react-router-dom";


const UpbitKey = () => {
  const navigate = useNavigate();
  const userName = localStorage.getItem('userName');
  const imageUrl = localStorage.getItem('imageUrl');


  const onhandleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const postData = {
      accessKey: data.get('accessKey'),
      secretKey: data.get('secretKey'),
    };

    console.log(postData);

    // post
    await Api
      .post('/api/v1/users/UpbitToken', postData)
      .then(function (response) {
        console.log(response);
        sessionStorage.setItem("temp", sessionStorage.getItem("temp") + 1);
        alert(response.data.result);
        navigate('/mypage');
      })
      .catch(function (err) {
        console.log(err.response);
        alert("등록 실패!");
      });


  };


  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}>
            <div className={styles.MemberSide}>
              <div className={[styles.MemberCard, styles.Clearfix].join(" ")}>
                <div className={styles.MemberCardBody}>
                  <div className={[styles.Avatar, styles.TwMb2].join(" ")}>
                    {imageUrl === 'null' ? <img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png" alt="profile_image" /> : <img src={imageUrl} alt="profile_image" />}
                  </div>
                  <div>
                    <div className={[styles.TwFontBold, styles.TwMb1].join(" ")}>{userName}</div>
                  </div>
                </div>
              </div>
              <div className={[styles.MemberCard, styles.MemberMenu].join(" ")}>
                <ul>
                  <li>
                    <a href="/mypage">회원정보 보기</a>
                  </li>
                  <li className={styles.Active}>
                    <a href="/mypage/upbitkey">업비트 키 등록 / 수정</a>
                  </li>
                  <li>
                    <a href="/mypage/wage">입금 하기</a>
                  </li><li>
                    <a href="/mypage/withdraw">출금 하기</a>
                  </li>
                  <li>
                    <a href="/mypage/myboard">작성 글 보기</a>
                  </li>
                  <li>
                    <a href="/diary">매매일지 & 포트폴리오</a>
                  </li>
                </ul>
              </div>
            </div>
            <div className={styles.MemberContent}>
              <div className={styles.MemberCard}>
                <div className={styles.MemberCardHeader}>
                  <Box>
                    <h1 style={{ display: 'inline', marginRight: '650px' }}>업비트 키 등록 / 수정</h1>
                    <Button onClick={() => { navigate('/upbit/infomation') }} type="button">발급 방법</Button>
                  </Box>
                </div>
                <div className={styles.MemberCardBody}>
                  <Box component="form" onSubmit={onhandleSubmit} noValidate sx={{ mt: 1 }}>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="accessKey"
                      label="Access Key"
                      type="accessKey"
                      id="accessKey"
                      autoFocus
                    />
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="secretKey"
                      label="Secret Key"
                      type="secretKey"
                      id="secretKey"
                    />
                    <Button
                      type="submit"
                      fullWidth
                      variant="contained"
                      sx={{ mt: 3, mb: 2 }}
                    >
                      업비트 키 등록
                    </Button>
                  </Box>
                </div>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}



export default UpbitKey;
