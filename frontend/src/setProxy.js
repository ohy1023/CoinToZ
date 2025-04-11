// // src/main/frontend/src/setProxy.js

// const { createProxyMiddleware } = require('http-proxy-middleware');

// module.exports = function (app) {
//   app.use(
//     '/api', // 프론트엔드에서 '/api'로 요청을 보내면, 백엔드인 8080포트(=target)로 요청이 도착
//     createProxyMiddleware({
//       target: 'http://localhost:8089', // 서버 URL or localhost:설정한 포트번호
//       changeOrigin: true,
//     })
//   );
// };
