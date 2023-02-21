import { useEffect, useState } from 'react';
import { alpha } from '@mui/material/styles';
import { Box, Divider, Typography, Stack, MenuItem, Avatar, IconButton, Popover } from '@mui/material';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { userState } from '../../../functions/GlobalState';
import { removeCookie } from '../../../functions/cookie';
import Api from '../../../functions/customApi';

const MENU_OPTIONS = [
  {
    label: '내 정보',
    icon: 'eva:person-fill',
  },
  {
    label: '업비트 키 발급 방법',
    icon: 'eva:settings-2-fill',
  },
  {
    label: '업비트 키 등록',
    icon: 'eva:settings-3-fill',
  },
];

// ----------------------------------------------------------------------

export default function AccountPopover() {
  const [open, setOpen] = useState(null);
  const navigate = useNavigate();
  const setUser = useSetRecoilState(userState);
  const email = localStorage.getItem('email');
  const userName = localStorage.getItem('userName');

  const [account, setAccount] = useState([]);


  const getInfo = async () => {
    await Api.get("/api/v1/users")
      .then(function (response) {
        console.log(response.data.result);
        setAccount(response.data.result);
      })
      .catch(function (err) {
        console.log(err);
        alert("유저 정보 조회 실패");
      })
  };

  useEffect(() => {
    getInfo();
  }, [sessionStorage.getItem("temp")]);

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

  const handleOpen = (event) => {
    setOpen(event.currentTarget);
  };

  const handleClose = () => {
    setOpen(null);
  };

  return (
    <>
      <IconButton
        onClick={handleOpen}
        sx={{
          p: 0,
          ...(open && {
            '&:before': {
              zIndex: 1,
              content: "''",
              width: '100%',
              height: '100%',
              borderRadius: '50%',
              position: 'absolute',
              bgcolor: (theme) => alpha(theme.palette.grey[900], 0.8),
            },
          }),
        }}
      >
        <Avatar src={account.imageUrl} />
      </IconButton>

      <Popover
        open={Boolean(open)}
        anchorEl={open}
        onClose={handleClose}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
        transformOrigin={{ vertical: 'top', horizontal: 'right' }}
        PaperProps={{
          sx: {
            p: 0,
            mt: 1.5,
            ml: 0.75,
            width: 180,
            '& .MuiMenuItem-root': {
              typography: 'body2',
              borderRadius: 0.75,
            },
          },
        }}
      >
        <Box sx={{ my: 1.5, px: 2.5 }}>
          <Typography variant="subtitle2" noWrap>
            {userName}
          </Typography>
          <Typography variant="body2" sx={{ color: 'text.secondary' }} noWrap>
            {email}
          </Typography>
        </Box>

        <Divider sx={{ borderStyle: 'dashed' }} />

        <Stack sx={{ p: 1 }}>
          {/* {MENU_OPTIONS.map((option) => (
            <MenuItem key={option.label} onClick={handleClose}>
              {option.label}
            </MenuItem>
          ))} */}
          <MenuItem key={MENU_OPTIONS[0].label} onClick={() => {
            navigate('/mypage')
            handleClose()
          }}>
            {MENU_OPTIONS[0].label}
          </MenuItem>
          {account.needUpbitKey === true ? (
            <>
            <MenuItem key={MENU_OPTIONS[1].label} onClick={() => {
              navigate('/upbit/infomation')
              handleClose()
            }}>
              {MENU_OPTIONS[1].label}
            </MenuItem>
            <MenuItem key={MENU_OPTIONS[2].label} onClick={() => {
            navigate('/mypage/upbitkey')
            handleClose()
          }}>
            {MENU_OPTIONS[2].label}
          </MenuItem>
          </>
          ) : (<></>)}
        </Stack>

        <Divider sx={{ borderStyle: 'dashed' }} />

        <MenuItem onClick={() => {
          logoutUser()
          handleClose()
        }} sx={{ m: 1 }}>
          로그아웃
        </MenuItem>
      </Popover>
    </>
  );
}


