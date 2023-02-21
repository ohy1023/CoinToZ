import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Button from '@mui/material/Button';
import MenuItem from '@mui/material/MenuItem';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { userState } from '../../../functions/GlobalState';
import AccountPopover from './profileSelect';
import Logo from '../../../assets/appBar/logo.png';
import { createTheme } from '@mui/material';
import { ThemeProvider } from '@mui/material';


const pages = ['Exchange', 'TradingDiary', 'Community'];

const customTheme = createTheme({
  palette: {
    secondary: {
      main: "#191970",
    }
  }
});

function ResponsiveAppBar() {

  const navigate = useNavigate();

  const user = useRecoilValue(userState);

  const [anchorElNav, setAnchorElNav] = React.useState(null);

  const handleOpenNavMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorElNav(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };


  return (
    <ThemeProvider theme={customTheme}>
      <AppBar position="static" color={"secondary"}>
        <Container maxWidth="xl">
          <Toolbar disableGutters>
            <Typography
              noWrap
              component="a"
              href="/"
              sx={{
                mr: 2,
                display: { xs: 'none', md: 'flex' },
                fontFamily: 'DoHyeon',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              <img style={{ display: { xs: 'none', md: 'flex' }, width: 100, height: 90, marginRight: 40, marginTop: -6, marginBottom: -6}} src={Logo} alt="" />
            </Typography>

            <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
              <IconButton
                size="large"
                aria-label="account of current user"
                aria-controls="menu-appbar"
                aria-haspopup="true"
                onClick={handleOpenNavMenu}
                color="inherit"
              >
                <MenuIcon />
              </IconButton>
              <Menu
                id="menu-appbar"
                anchorEl={anchorElNav}
                anchorOrigin={{
                  vertical: 'bottom',
                  horizontal: 'left',
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'left',
                }}
                open={Boolean(anchorElNav)}
                onClose={handleCloseNavMenu}
                sx={{
                  display: { xs: 'block', md: 'none' },
                }}
              >
                <MenuItem key={pages[0]} onClick={() => {
                  handleCloseNavMenu()
                  navigate('/upbitMainPage')
                }}>
                  <Typography textAlign="center">{pages[0]}</Typography>
                </MenuItem>
                <MenuItem key={pages[1]} onClick={() => {
                  handleCloseNavMenu()
                  navigate('/diary')
                }}>
                  <Typography textAlign="center">{pages[1]}</Typography>
                </MenuItem>
                <MenuItem key={pages[2]} onClick={() => {
                  handleCloseNavMenu()
                  navigate('/community')
                }}>
                  <Typography textAlign="center">{pages[2]}</Typography>
                </MenuItem>
                {user ? (<Box></Box>) : (
                  <Box>
                  <MenuItem key='회원가입' onClick={() => {
                  handleCloseNavMenu()
                  navigate('/join')
                }}>
                  
                  <Typography textAlign="center">회원가입</Typography>
                </MenuItem>
                <MenuItem key='로그인' onClick={() => {
                  handleCloseNavMenu()
                  navigate('/login')
                }}>
                  
                  <Typography textAlign="center">로그인</Typography>
                </MenuItem>
                </Box>
                )}
                
              </Menu>
            </Box>
            <Typography
              noWrap
              component="a"
              href='/'
              sx={{
                mr: 2,
                display: { xs: 'flex', md: 'none' },
                flexGrow: 1,
                fontFamily: 'monospace',
                fontWeight: 700,
                letterSpacing: '.3rem',
                color: 'inherit',
                textDecoration: 'none',
              }}
            >
              <img style={{ width: 70, height: 70}} src={Logo} alt="" />
            </Typography>
            <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
              <Button
                key={pages[0]}
                onClick={() => { navigate('/upbitMainPage') }}
                sx={{ my: 2, color: 'white', display: 'block', marginRight: 1 }}
              >
                {pages[0]}
              </Button>
              <Button
                key={pages[1]}
                onClick={() => { navigate('/diary') }}
                sx={{ my: 2, color: 'white', display: 'block', marginRight: 1 }}
              >
                {pages[1]}
              </Button>
              <Button
                key={pages[2]}
                onClick={() => { navigate('/community') }}
                sx={{ my: 2, color: 'white', display: 'block', marginRight: 1 }}
              >
                {pages[2]}
              </Button>
            </Box>

            <Box sx={{ flexGrow: 0 }}>
              {user ? (
                <>
                  <AccountPopover />
                </>
              ) : (
                <>
                  <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                    <Button
                      key='join'
                      onClick={() => { navigate('/join') }}
                      sx={{ my: 2, color: 'white', display: 'block' }}
                    >
                      join
                    </Button>
                    <Button
                      key='login'
                      onClick={() => { navigate('/login') }}
                      sx={{ my: 2, color: 'white', display: 'block' }}
                    >
                      login
                    </Button>
                  </Box>
                </>)}

            </Box>
          </Toolbar>
        </Container>
      </AppBar>
    </ThemeProvider>
  );
}
export default ResponsiveAppBar;
