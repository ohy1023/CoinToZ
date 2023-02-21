import * as React from 'react';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Paper from '@mui/material/Paper';
import Chart from './Chart';
import Deposits from './Deposits';
import Orders from './Orders';
import { useRecoilValue} from 'recoil';
import { useNavigate } from 'react-router-dom';
import { useEffect} from "react";
import { userState } from '../../../functions/GlobalState';
import { Col, Row } from 'antd';


const mdTheme = createTheme();

const DashboardContent = () => {
    const isLoggedIn = useRecoilValue(userState);
    const navigate = useNavigate();

    useEffect(() => {
        if (!isLoggedIn) {
            alert("로그인이 필요한 페이지 입니다. 로그인 페이지로 이동합니다.")
            navigate('/login');
        }
    }, [isLoggedIn, navigate]);

    return (
        <ThemeProvider theme={mdTheme}>
            <Row align={"center"}>
                <Box sx={{ display: 'flex', width: '100%'}}>
                    <CssBaseline />
                        <Box
                            component="main"
                            sx={{
                                backgroundColor: (theme) =>
                                    theme.palette.mode === 'light'
                                        ? theme.palette.grey[100]
                                        : theme.palette.grey[900],
                                flexGrow: 1,
                                height: '100%',
                                // overflow: 'auto',
                            }}
                        >
                            <h3 style={{marginLeft:'180px', marginTop:'20px'}}>TRADING DIARY</h3>
                            {/* <Toolbar /> */}
                            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                                <Grid container spacing={3}>
                                    <Grid item xs={12} md={8} lg={7}>
                                        <Paper
                                            sx={{
                                                p: 2,
                                                display: 'flex',
                                                flexDirection: 'column',
                                                height: 400,
                                            }}
                                        >
                                            <Chart />
                                        </Paper>
                                    </Grid>
                                    <Grid item xs={12} md={4} lg={5}>
                                        <Paper
                                            sx={{
                                                p: 2,
                                                display: 'flex',
                                                flexDirection: 'column',
                                                height: 400,
                                            }}
                                        >
                                            <Deposits />
                                        </Paper>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Paper
                                            sx={{ p: 2, display: 'flex', flexDirection: 'column', height:800 }}>
                                            <Orders />
                                        </Paper>
                                    </Grid>
                                </Grid>
                            </Container>
                        </Box>
                </Box>
            </Row>
        </ThemeProvider>
    );
};

export default function Dashboard() {
    return <DashboardContent />;
}