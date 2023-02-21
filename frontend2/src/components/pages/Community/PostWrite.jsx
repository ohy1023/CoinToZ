import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {
    Avatar,
    Button,
    CssBaseline,
    TextField,
    FormControl,
    FormControlLabel,
    Checkbox,
    FormHelperText,
    Grid,
    Box,
    Typography,
    Container,
} from '@mui/material/';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import styled from 'styled-components';
import Api from '../../../functions/customApi';

// mui의 css 우선순위가 높기때문에 important를 설정 - 실무하다 보면 종종 발생 우선순위 문제
const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 700 !important;
  color: #d32f2f !important;
`;

const Boxs = styled(Box)`
  padding-bottom: 40px !important;
`;

const PostWrite = () => {
    const theme = createTheme();
    const navigate = useNavigate();

    const handleSubmit = (e) => {
        e.preventDefault();

        const data = new FormData(e.currentTarget);
        const joinData = {
            title: data.get('title'),
            body : data.get('body'),
        };
        onhandlePost(joinData);
    };

    const onhandlePost = async (data) => {
        const { title, body } = data;
        const postData = { title, body };

    // post
        await Api
        .post(`/api/v1/posts`, postData)
        .then(()=>{
            alert("게시글 등록이 완료되었습니다.");
            navigate('/community');
        })
        .catch(function (err) {
            console.log(err);
        });
    };


    return (
        <ThemeProvider theme={theme}>
            <Container component="main" >
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 7,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <h3>게시글 작성</h3>
                    <Boxs component="form" noValidate onSubmit={handleSubmit} >
                        <FormControl component="fieldset" variant="standard">
                            <Grid container justifyContent="center" marginBottom={3}>
                                <TextField fullWidth  margin="normal"
                                    id="title"
                                    name='title'
                                    label="제목을 작성해주세요."
                                    variant="outlined"
                                    required
                                />
                                <TextField fullWidth margin="normal"
                                    id="body"
                                    name='body'
                                    label="내용을 작성해주세요."
                                    variant="outlined"
                                    multiline
                                    rows={10}
                                    required
                                />
                                <Button type="submit" variant="contained" color="primary" sx={{mt:2}}>
                                    등록하기
                                </Button>
                            </Grid>
                        </FormControl>
                    </Boxs>
                </Box>
            </Container>
        </ThemeProvider>
    );
};

export default PostWrite;