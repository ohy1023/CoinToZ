import { useEffect } from "react";
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from 'axios';
import { useNavigate, useLocation } from 'react-router-dom';
import KakaoBut from '../../../assets/signIn/kakao_login_medium_wide.png'
import { setCookie } from "../../../functions/cookie";
import { useSetRecoilState } from 'recoil';
import { userState } from '../../../functions/GlobalState';
import queryString from 'query-string';
import Api from "../../../functions/customApi";
import moment from "moment";

const theme = createTheme();


export default function SignIn({ location }) {
  const setUser = useSetRecoilState(userState);
  const navigate = useNavigate();
  const { search } = useLocation();
  const paths = '/';

  useEffect(() => {

    const handleQuery = () => {
      const query = queryString.parse(search);
      const { accessToken, refreshToken, email } = query;
      

      if (accessToken) {
        const expires =  moment().add('3','days').toDate()
        setCookie("access", accessToken,{paths,expires});
        setCookie("refresh", refreshToken,{paths,expires});
        localStorage.setItem("email", email);
        sessionStorage.setItem("temp",0);
        setUser(localStorage.getItem("email"));
        getInfo();
        alert("로그인이 완료되었습니다.");
      }
    }

    if (search) {
      handleQuery();
      navigate('/');
    }
  }, []);

  const getInfo = async () => {
    await Api.get("/api/v1/users")
      .then(function (response) {
        console.log(response.data.result);
        localStorage.setItem("userName", response.data.result.userName);
        localStorage.setItem("imageUrl", response.data.result.imageUrl);
        localStorage.setItem("createAt", response.data.result.createAt);
      })
      .catch(function (err) {
        console.log(err);
        alert("유저 정보 조회 실패");
      })
  };


  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const joinData = {
      email: data.get('email'),
      password: data.get('password')
    };

    onhandlePost(joinData)

  };

  const onhandlePost = async (data) => {
    const { email, password } = data;
    const postData = { email, password };



    // post
    await axios
      .post('/api/v1/users/login', postData)
      .then(function (response) {
        const expires =  moment().add('3','days').toDate()
        setCookie("access", response.headers.get("Authorization"),{paths,expires});
        setCookie("refresh", response.headers.get("Authorization-refresh"),{paths,expires});
        localStorage.setItem("email", email);
        sessionStorage.setItem("temp",0);
        setUser(localStorage.getItem("email"));
        getInfo();
        alert("로그인이 완료되었습니다.")
        navigate('/');
      })
      .catch(function (err) {
        console.log(err);
        alert("로그인에 실패했습니다.(이메일 또는 비밀번호를 확인해주세요.)")
      });
  };

  return (

    <ThemeProvider theme={theme} >
      <Container component="main" maxWidth="xs" >
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            paddingBottom :'11.8rem'
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            로그인
          </Typography>
          <br />
          <Link href="http://ec2-52-78-23-203.ap-northeast-2.compute.amazonaws.com:8080/oauth2/authorization/kakao">
            <img style={{}} alt="kakao" src={KakaoBut} />
          </Link>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="이메일 주소"
              name="email"
              autoComplete="email"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="비밀번호"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              로그인
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="/temp" variant="body2">
                  비밀번호를 잊으셨나요?
                </Link>
              </Grid>
              <Grid item>
                <Link href="/join" variant="body2">
                  {"회원가입 하러 가기"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>

    </ThemeProvider>
  );

}