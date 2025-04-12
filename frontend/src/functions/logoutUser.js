import { privateApi } from '../utils/http-common';
import { accessTokenState } from '../recoil/authAtom';
import { setRecoil } from 'recoil-nexus';
import { navigate } from '../hooks/navigate';

export const logoutUser = async () => {
  try {
    await privateApi.get('/api/v1/users/logout');

    // 클라이언트 상태 초기화
    localStorage.clear();
    sessionStorage.clear();
    setRecoil(accessTokenState, '');
    navigate('/');
  } catch (err) {
    console.error('로그아웃 실패:', err);
  }
};
