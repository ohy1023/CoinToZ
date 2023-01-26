import * as React from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import NaverBut from './img/btnG_축약형.png'
import GoogleBut from './img/btn_google_signin_dark_focus_web@2x.png'
import KakaoBut from './img/kakao_login_small.png'

const theme = createTheme();

// 쿠키 생성하기
const setCookie = (name, value, expiredays) => {
  const today = new Date();
  today.setDate(today.getDate() + expiredays);
  document.cookie = `${name}=${escape(value)}; path=/; expires=${today.toGMTString()};`;
};


export default function SignIn() {
  const navigate = useNavigate();

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
        setCookie("access", response.headers.get("Authorization"));
        setCookie("refresh", response.headers.get("Authorization-refresh"));
        alert("로그인이 완료되었습니다.")
        navigate('/');
      })
      .catch(function (err) {
        console.log(err);
        alert("로그인에 실패했습니다.(이메일 또는 비밀번호를 확인해주세요.)")
      });
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <br></br>
          <Grid>
              <p class="lead fw-normal mb-0 me-3">Sign in with
                <Link href="/oauth2/authorization/naver">
                  <img style={{display: 'inline-block', width : "60px", height: "30px", marginLeft: '5px', marginRight: '5px', verticalAlign: '-1px'}} className="phoneImage" alt="naver" src={NaverBut} />
                </Link>
                <Link href="/oauth2/authorization/kakao">
                  <img style={{display: 'inline-block', marginLeft: '5px', marginRight: '5px', verticalAlign: '-1px'}} className="phoneImage" alt="kakao" src={KakaoBut} />
                </Link>
                <Link href="/oauth2/authorization/google">
                  <img style={{display: 'inline-block',height:'30px', marginLeft: '5px', marginRight: '5px', verticalAlign: '-1px'}} className="phoneImage" alt="google" src={GoogleBut} />
                </Link>
                </p>
            </Grid>
          <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
            />
            <FormControlLabel
              control={<Checkbox value="remember" color="primary" />}
              label="Remember me"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link href="#" variant="body2">
                  Forgot password?
                </Link>
              </Grid>
              <Grid item>
                <Link href="/join" variant="body2">
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
      
    </ThemeProvider>
  );

}