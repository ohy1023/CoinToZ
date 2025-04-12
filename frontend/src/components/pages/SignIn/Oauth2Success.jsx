import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { accessTokenState } from '../../../recoil/authAtom';
import { userTokenRefresh } from '../../../functions/userTokenRefresh';
import { Box, Typography, CircularProgress, Paper } from '@mui/material';
import { privateApi } from '../../../utils/http-common';

const OAuth2Success = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const setAccessToken = useSetRecoilState(accessTokenState);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const email = params.get('email');

    if (email) {
      localStorage.setItem('email', email);
    }

    const reissue = async () => {
      try {
        const res = await userTokenRefresh();
        const newAccessToken = res.data.result.accessToken;

        setAccessToken(newAccessToken); // 메모리 저장
      } catch (err) {
        console.error('토큰 재발급 실패', err);
        navigate('/login');
      }
    };

    const getInfo = async () => {
      await privateApi
        .get('/api/v1/users')
        .then(function (response) {
          console.log(response.data.result);
          localStorage.setItem('userName', response.data.result.userName);
          localStorage.setItem('imageUrl', response.data.result.imageUrl);
          localStorage.setItem('createAt', response.data.result.createAt);
          sessionStorage.setItem('temp', '0');
          navigate('/'); // 홈으로 이동
        })
        .catch(function (err) {
          console.log(err);
          alert('유저 정보 조회 실패');
        });
    };

    reissue();
    getInfo();
  }, [location.search, navigate, setAccessToken]);

  return (
    <Box
      height="100vh"
      display="flex"
      alignItems="center"
      justifyContent="center"
      bgcolor="#f9f9f9"
    >
      <Paper
        elevation={3}
        sx={{ padding: 6, textAlign: 'center', borderRadius: 4 }}
      >
        <CircularProgress size={48} sx={{ mb: 3 }} />
        <Typography variant="h5" component="h2" gutterBottom>
          로그인 처리 중입니다...
        </Typography>
        <Typography variant="body2" color="text.secondary">
          잠시만 기다려주세요
        </Typography>
      </Paper>
    </Box>
  );
};

export default OAuth2Success;
