import { useState, useEffect } from "react";
import styles from "./Mypage.module.css";
import Api from "../util/customApi";

const Mypage = () => {
  const [account, setAccount] = useState([])

  const getInfo = async () => {
    await Api.get("/api/v1/users/info")
      .then(function (response) {
        setAccount(response.data.result)
      })
      .catch(function (err) {
        console.log(err);
        alert("유저 정보 조회 실패");
      })
  };

  useEffect(() => {
    getInfo();
  }, []);

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}><div className={styles.MemberSide}>
            <div className={[styles.MemberCard, styles.Clearfix].join(" ")}>
              <div className={styles.MemberCardBody}>
                <div className={[styles.Avatar, styles.TwMb2].join(" ")}>
                  {account.imageUrl !== null ? <img src={account.imageUrl} alt="profile_image" />:<img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png" alt="profile_image" />}
                </div>
                <div>
                  <div className={[styles.TwFontBold, styles.TwMb1].join(" ")}>{account.userName}</div>
                </div>
              </div>
            </div>  <div className={[styles.MemberCard, styles.MemberMenu].join(" ")}>
              <ul>
                <li className={styles.Active}>
                  <a href="/mypage">회원정보 보기</a>
                </li><li>
                  <a href="/index.php?act=dispMemberOwnDocument&amp;mid=index">작성 글 보기</a>
                </li><li>
                  <a href="/index.php?act=dispMemberOwnComment&amp;mid=index">좋아요 누른 글 보기</a>
                </li><li>
                  <a href="/index.php?act=dispMemberOwnComment&amp;mid=index">작성 댓글 보기</a>
                </li></ul>
            </div></div><div className={styles.MemberContent}>
              <div className={styles.MemberCard}>
                <div className={styles.MemberCardHeader}>
                  <h1>회원 정보</h1>
                </div>

                <div className={styles.MemberCardBody}>

                  <ul className={styles.MemberInfoList}>
                    <li>
                      <label htmlFor="email_address">이메일 주소<span className={styles.Required}>필수</span></label>

                      <div>{account.email}</div>
                    </li>
                    <li>
                      <label htmlFor="nick_name">닉네임<span className={styles.Required}>필수</span></label>

                      <div>{account.userName}</div>                  
                    </li>
                    <li>
                        <label htmlFor="profile_image">프로필 사진</label>
                        <div>{account.imageUrl !== null ? account.imageUrl : 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png'}</div>
                    </li>
                    <li>
                      <label>가입일</label>
                      <div>{account.createAt}</div>
                    </li>
                  </ul>

                  <div className={[styles.TwFlex, styles.TwFlewWrap].join(" ")}>
                    <a href="/index.php?mid=index&amp;act=dispMemberModifyInfo" className={[styles.Link, styles.TwMr3].join(" ")}>회원정보 변경</a>

                    <a href="/index.php?mid=index&amp;act=dispMemberModifyPassword" className={[styles.Link, styles.TwMr3].join(" ")}>비밀번호 변경</a>

                    <a href="/index.php?mid=index&amp;act=dispMemberModifyEmailAddress" className={[styles.Link, styles.TwMr3].join(" ")}>이메일 주소 변경</a>
                    <div className={styles.TwFlex1}></div>

                    <a href="/index.php?mid=index&amp;act=dispMemberLeave" className={[styles.Link, styles.TwTextDanger].join(" ")}>탈퇴</a>
                  </div>    </div>
              </div>
            </div>
          </section>      </div>
      </main>
    </div>
  );
}



export default Mypage;
