// src/main/frontend/src/setProxy.js

const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
  app.use(
    '/api', // 프론트엔드에서 '/api'로 요청을 보내면, 백엔드인 8080포트(=target)로 요청이 도착
    createProxyMiddleware({
      target: 'http://ec2-52-78-23-203.ap-northeast-2.compute.amazonaws.com:8080',	// 서버 URL or localhost:설정한 포트번호
      // target:'http://localhost:8080',
      changeOrigin: true,
    })
  );
};