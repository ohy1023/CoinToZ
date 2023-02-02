import axios from "axios";
import moment from "moment";
import { setCookie, getCookie, removeCookie } from "./cookie";
import jwt_decode from "jwt-decode";


const refresh = async (config) => {
  const refreshToken = getCookie("refresh");
  const accessToken = getCookie("access");
  const decodePayload = jwt_decode(accessToken, { payload: true });
  const expireAt = new Date(decodePayload.exp) * 1000;


  // 토큰이 만료되었다면
  if (moment(expireAt).diff(moment()) < 0 && refreshToken) {
    console.log("토큰을 재발급합니다!");

    //재발급 요청
    const res = await axios.get("/api/v1/users/reissuance", {
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Authorization-refresh": `Bearer ${refreshToken}`,
        email: localStorage.getItem("email")
      }
    });
    console.log("재발급 성공");
    setCookie("access", res.headers.get("Authorization"));
    setCookie("refresh", res.headers.get("Authorization-refresh"));

    config.headers["Authorization"] = `Bearer ${getCookie("access")}`; // 토큰 교체

  }
  else {
    config.headers["Authorization"] = `Bearer ${accessToken}`;
  }

  return config;
};

const refreshErrorHandle = () => {
  removeCookie("refresh");
};

export { refresh, refreshErrorHandle };