## 팀 구성원, 개인 별 역할

---

아이디어톤 팀 구성원을 기재해 주시고, 그 주의 팀원이 어떤 역할을 맡아서 개발을 진행했는지 구체적으로 작성해 주세요. 🙂 

- **강동연 : PM**
- **강수빈 : 인프라**
- **김상호 : 기획**
- **오형상 : CTO**
- **손세열 : 개발1**
- **임학준 : 개발2**

## 팀 내부 회의 진행 회차 및 일자

---

- **1회차(2023.01.20) Discord 음성 채널 회의 진행**

## 현재까지 개발 과정 요약 (최소 500자 이상)

---

현재까지 개발을 진행하면서 ‘기술적으로 새로 알게된 점, 어려웠던 점, 아쉬운 점' 등을 요약하여 작성해 주세요 🙂

팀원 각자 현재 구현하고 있는 것을 적어주세요. :)

- **강동연**
    - **업비트 가상화폐 API 값 가져와 View 구현**
    - Open API를 `FeignClient` 를 통해 가져오는 작업을 처음 해보아서 어려웠다.
    - View 페이지는 동적으로 표현하는데 어려움을 겪고 있다. 부트스트랩만으로 UI를 구현하는데 한계가 있을 것 같아서 걱정이 있다.
- **강수빈**
    - **커뮤니티 게시판 기능 구현**
    - **업비트 가상화폐 API 값 가져와 View 구현 (체결표, 호가정보표, 코인상세정보표)**
- **김상호**
    - **커뮤니티 게시판 기능 및 UI 구현**
    - 기술적으로 새로 알게된 점
        - split을 통해 자동으로 로그인 되어있는 이메일 정보에서 유저 아이디 분리
    - 어려웠던 점
        - js와 view 간의 연결이 아직 조금 서툴러서 조금 더 공부를 해야 할것 같았다.
- **손세열**
    - **메인 페이지 UI 구현**
- **오형상**
    - **회원가입, 로그인 UI**
    - **제체 회원가입 ← DB에 저장**
    - **자체** **로그인 ← 응답 헤더에 accessToken, refreshToken 담고 refreshToken mysql db에 저장**
    - **Ouath2 기능 구현 (네이버, 카카오, 구글)  ← 응답 헤더에 accessToken,refreshToken 담고 refreshToken mysql db에 저장**
    - **로그인 유지 (응답 헤더에서 토큰 꺼내와 localStorage에 저장)**
    
    어려운 점
    
    - **현재 localStorage에 refreshToken도 저장되어있는데 보안상 안 좋다고 하여 refreshToken을 쿠키에 보관할지 index값으로 보관할지 고민 중**
    - **accessToken이 만료되기 1분 전 요청 헤더에  refreshToken을 담아 토큰 재발급 요청 후 localStroage에 토큰 값 갱신**

- **임학준**
    - **Service Test 구현, 커뮤니티 게시판 UI 구현**
    - 댓글은 로그인 한 사용자만 달 수 있다
    - 댓글의 수정/삭제는 작성자만이 할 수 있다
    - 좋아요 싫어요 UI구현
    - 유저 로그인 정보와 연동하는것, Ajax를 적용하는 것이 어려웠다

## 개발 과정에서 나왔던 질문 (최소 200자 이상)

---

개발을 진행하며 나왔던 질문 중 핵심적인 것을 요약하여 작성해 주세요 🙂

질의응답 과정 중 해결되지 않은 질문을 정리하여도 좋습니다.

- **강동연**
    - 업비트 API 가져오는데 null 값 뜨는 상황 → 변수명을 camelCase에서 snake 표기법으로 바꾸면 해결,,🥲
    - ‘Support multiple urls in `@RequestMapping` on FeingClient’ 이슈를 해결하였다.
- **강수빈**
    - 업비트 API 가져오는데 null 값 뜨는 상황 → 변수명을 camelCase에서 snake 표기법으로 바꾸면 해결
    - 업비트 API 데이터 가공 과정에서 중복되는 코드 제거 필요
    - 업비트 API 데이터를 통해 그래프 만드는 법 (Bootstrap 지원 안함)
- **손세열**
    - UI 구현 방법 탐색

## 개발 결과물 공유

---

### **GitLab Repository URL**

[people of development / financial-final-project · GitLab](https://gitlab.com/people-of-development/financial-final-project)

- 팀원들과 함께 찍은 인증샷(온라인 만남시 스크린 캡쳐)도 함께 업로드 해주세요 🙂

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/154bae57-cb65-4587-8023-ca0722be2e0b/Untitled.png)
