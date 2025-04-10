import axios from 'axios';
import jwt_decode from 'jwt-decode';
import moment from 'moment';
import { useRecoilState } from 'recoil';
import { accessTokenState } from '../recoil/authAtom';

const useCustomAxios = () => {
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);

  const instance = axios.create({
    timeout: 10000,
    withCredentials: true, // 쿠키 포함
  });

  instance.interceptors.request.use(async (config) => {
    let token = accessToken;

    try {
      // 1. accessToken이 없거나 만료된 경우 Refresh
      let needsRefresh = false;

      if (!token) {
        needsRefresh = true;
      } else {
        const { exp } = jwt_decode(token);
        const isExpired = moment(exp * 1000).diff(moment()) < 0;
        if (isExpired) needsRefresh = true;
      }

      if (needsRefresh) {
        console.log('AccessToken 없음 또는 만료 → Refresh');

        const res = await axios.get('/api/v1/users/reissuance', {
          withCredentials: true,
        });

        const newToken = res.data.result.accessToken;
        setAccessToken(newToken);
        token = newToken;
      }

      // 2. Authorization 헤더 설정
      config.headers['Authorization'] = `Bearer ${token}`;
      return config;
    } catch (err) {
      console.error('Refresh 실패');
      // TODO: 로그아웃 로직 또는 상태 초기화 추가 가능
      return config;
    }
  });

  return instance;
};

export default useCustomAxios;
