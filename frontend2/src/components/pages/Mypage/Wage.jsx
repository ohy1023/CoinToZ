import styles from "./Wage.module.css";
import { Box, TextField, Button, MenuItem } from "@mui/material";
import Api from "../../../functions/customApi";
import { useEffect, useState, useCallback } from "react";
import Select, { SelectChangeEvent } from '@mui/material/Select';
import { useNavigate } from "react-router-dom";


const Wage = () => {

  const userName = localStorage.getItem('userName');
  const imageUrl = localStorage.getItem('imageUrl');
  const [amount, setAmount] = useState('');
  const [twoMethod, setTwoMethod] = useState('2차 인증 방식');
  const [money, setMoney] = useState(0);
  const [count, setCount] = useState(0);
  const navigate = useNavigate();

  function setting(data) {
    if (data.currency === "KRW") {
      setMoney(data.balance);
    }
  }

  const handleChangeCount = () => {
    setCount(count + 1);
  };

  const handleChangeMethod = (event: SelectChangeEvent) => {
    setTwoMethod(event.target.value);
  };

  const handleChangeAmount = (event: SelectChangeEvent) => {
    setAmount(event.target.value);
  };

  const onhandleSubmit = async () => {


    console.log(typeof amount);
    console.log(twoMethod);

    // post
    await Api
      .post(`/api/v1/upbit/deposit/${amount}/${twoMethod}`)
      .then(function (response) {
        console.log(response.data);
        alert("인증 요청을 보냈습니다.");
      })
      .catch(function (err) {
        console.log(err.response);
        alert("인증 요청 실패!");
      });

  };

  const getInfo = useCallback(async () => {
    await Api.get("/api/v1/upbit/acount")
      .then(function (response) {
        console.log(response.data);
        (response.data).map(data => setting(data));
      })
      .catch(function (err) {
        console.log(err);
        alert("해당 서비스를 이용할려면 업비트 키를 등록해주세요");
        navigate('/mypage/upbitKey');
      })
  }, [navigate]);

  useEffect(() => {
    getInfo();
  }, [count, getInfo]);


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
                  <li>
                    <a href="/mypage/upbitkey">업비트 키 등록 / 수정</a>
                  </li>
                  <li className={styles.Active}>
                    <a href="/mypage/wage">입금 하기</a>
                  </li>
                  <li>
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
                    <h1 style={{ display: 'inline', marginRight: '740px' }}>원화 입금</h1>
                    <Button onClick={handleChangeCount} type="button">새로고침</Button>
                  </Box>
                </div>
                <div className={styles.MemberCardBody}>
                  <h6>👀 주의사항</h6>
                  <ul>
                    <li style={{ marginLeft: '5px' }}>✔ 업비트에서 계좌인증 후 사용가능합니다.</li>
                    <li style={{ marginLeft: '5px' }}>✔ 최소 5000원 부터 입금 가능합니다.</li>
                    <li style={{ marginLeft: '5px' }}>✔ 2차 인증 성공 후 입금 진행됩니다.</li>
                    <li style={{ marginLeft: '5px' }}>✔ 입금까지 약 1분 소요됩니다.</li>
                    <li style={{ marginLeft: '5px' }}>✔ 보유 금액 : {money} KRW</li>
                  </ul>
                  <Box sx={{ mt: 1 }}>
                    <TextField
                      margin="normal"
                      required
                      fullWidth
                      name="amount"
                      label="입금 금액(최소 5000원 이상)"
                      type="amount"
                      id="amount"
                      onChange={handleChangeAmount}
                      autoFocus
                    />

                    <Select
                      labelId="demo-simple-select-label"
                      id="demo-simple-select"
                      fullWidth
                      value={twoMethod}
                      label="wfajlkwe"
                      onChange={handleChangeMethod}
                    >
                      <MenuItem disabled value='2차 인증 방식'>2차 인증 방식</MenuItem>
                      <MenuItem value='kakao_pay'>Kakao-Pay</MenuItem>
                      <MenuItem value='naver'>Naver</MenuItem>
                    </Select>
                    <Button
                      type="button"
                      fullWidth
                      onClick={onhandleSubmit}
                      variant="contained"
                      sx={{ mt: 3, mb: 2 }}
                    >
                      입금 요청
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



export default Wage;
