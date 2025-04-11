import { publicApi } from '../utils/http-common';

export const userTokenRefresh = async () => {
  return await publicApi.get('/api/v1/users/reissuance', {
    headers: {
      'X-User-Email': localStorage.getItem('email'),
    },
  });
};
