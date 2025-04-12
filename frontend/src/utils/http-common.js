import axios from 'axios';
import { getRecoil, setRecoil } from 'recoil-nexus';
import { accessTokenState } from '../recoil/authAtom';
import { httpStatusCode } from './http-status';
import { userTokenRefresh } from '../functions/userTokenRefresh';

// publicApi: 토큰 필요 없음
export const publicApi = axios.create({
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// privateApi: 토큰 필요함
export const privateApi = axios.create({
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청 인터셉터
privateApi.interceptors.request.use(
  async (config) => {
    let token = getRecoil(accessTokenState);
    const email = localStorage.getItem('email');

    if (!token && email) {
      try {
        const res = await userTokenRefresh();
        token = res.data.result.accessToken;
        setRecoil(accessTokenState, token);
      } catch (err) {
        console.error('토큰 갱신 실패');
        return Promise.reject(err);
      }
    }

    config.headers['Authorization'] = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터
privateApi.interceptors.response.use(
  (response) => response,
  async (error) => {
    const { config, response } = error;
    if (!response) return Promise.reject(error);

    const { status } = response;
    const originRequest = config;

    if (
      status === httpStatusCode.UNAUTHORIZED &&
      !originRequest._retry &&
      !originRequest.url.includes('/reissuance')
    ) {
      originRequest._retry = true;
      try {
        const res = await userTokenRefresh();
        const newToken = res.data.result.accessToken;

        setRecoil(accessTokenState, newToken);
        originRequest.headers.Authorization = `Bearer ${newToken}`;
        return axios(originRequest);
      } catch (err) {
        console.error('토큰 재발급 실패로 로그아웃 처리');
        localStorage.clear();
        sessionStorage.clear();
        setRecoil(accessTokenState, '');
        return Promise.reject(err);
      }
    }

    return Promise.reject(error);
  }
);
