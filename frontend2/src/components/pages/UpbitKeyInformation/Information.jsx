import styles from './Information.module.css';
import { useNavigate } from "react-router-dom";
import MypageImg from '../../../assets/Information/mypage.png';
import UpbitLogin from '../../../assets/Information/upbitLogin.png';
import OpenApi from '../../../assets/Information/openApi.png';
import Register from '../../../assets/Information/register.png';
import { Link } from '@mui/material';

const Information = () => {

  const navigate = useNavigate();


  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section classNamee={styles.Member}>
            <div className={styles.MemberCard}>
              <div className={styles.MemberCardHeader}>
                <h1>업비트 키 발급 안내</h1>
              </div>
              <div className={styles.MemberCardBody}>
                <div className={styles.ttArticleUseless}>
                  <h3 ><b>1️⃣ 업비트 로그인</b></h3>
                  <p >&nbsp;</p>
                  <a style={{marginLeft:'40px'}} href="https://upbit.com/home" target="_blank" rel='noopener noreferrer'>✔ 업비트 바로가기</a>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✔ 업비트는 PC환경에서 회원가입,로그인을 지원하지 않아 모바일 앱 이용 필수.</p>
                  <p >&nbsp;</p>
                  <p></p>
                  <figure >
                    <span >
                      <img src={UpbitLogin} alt="" ata-origin-width="1346" data-origin-height="474" />
                    </span>
                  </figure>
                  <p></p>

                  <h3 ><b>2️⃣ 마이페이지 &gt; Open API 관리 클릭</b></h3>

                  <p></p>
                  <p >&nbsp;</p>
                  <figure >
                    <span >
                      <img src={MypageImg} alt="" data-origin-width="1346" data-origin-height="474" />
                    </span>
                  </figure>
                  <p></p>
                  <p >&nbsp;</p>
                  <p >&nbsp;</p>
                  <h3 >
                    <b>3️⃣ 인증레벨이 낮은 경우 계좌 인증 , ARS 인증 진행</b>
                  </h3>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✔ 업비트는 케이뱅크만 지원합니다.</p>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✔ 케이뱅크 신규계좌시 타은행과 다르게 절차가 간편하여 2-3분이면 충분히 혼자 만들 수 있습니다.</p>
                  <p >&nbsp;</p>
                  <p >&nbsp;</p>
                  <h3 ><b>4️⃣ api 사용 목적에 따라 api를 이용해 할 수 있는 행위들을 선택</b></h3>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✔ 전부 체크 권장</p>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✨ Ip 주소 : 52.78.23.203</p>
                  <p >&nbsp;</p>
                  <p></p>
                  <figure >
                    <span >
                      <img src={OpenApi} alt="" data-origin-width="880" data-origin-height="380" />
                    </span>
                  </figure>
                  <p></p>
                  <p >&nbsp;</p>
                  <p >&nbsp;</p>
                  <h3 ><b>5️⃣ 2차 인증까지 거치면 API Key 발급 완료&nbsp;</b></h3>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✔ 카카오페이 인증과 네이버 인증으로 발급 가능합니다.</p>
                  <p >&nbsp;</p>
                  <p style={{marginLeft:'40px'}}>✨ API Key는 절대 외부에 노출하지 않도록 주의합니다.</p>
                  <p >&nbsp;</p>
                  <p></p>
                  <figure style={{marginLeft:'40px'}}>
                    <span >
                      <img src="https://blog.kakaocdn.net/dn/cQWGxs/btqYkbDAvp8/namxwPyYExr9QzjIKWxZG1/img.png" alt="" data-origin-width="961" data-origin-height="795" />
                    </span>
                  </figure>
                  <p></p>
                  <p >&nbsp;</p>
                  <p >&nbsp;</p>
                  <h3 ><b>6️⃣ 발급받은 API Key 사이트 등록</b></h3>
                  <p >&nbsp;</p>
                  <Link style={{marginLeft:'40px'}} onClick={() => { navigate('/mypage/upbitkey') }}>✔ 등록하러 가기 (로그인 필수)</Link>
                  <p >&nbsp;</p>
                  <p></p>
                  <figure>
                    <span >
                      <img src={Register} alt="" data-origin-width="880" data-origin-height="271" />
                    </span>
                  </figure>
                  <p></p>
                  <p >&nbsp;</p>
                  <p >&nbsp;</p>
                </div>
              </div>
            </div>
          </section>
        </div>
      </main>
    </div>
  );
}

export default Information;
