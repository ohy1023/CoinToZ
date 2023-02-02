import styles from "./Mypage.module.css";
import { useState } from "react";
import DeleteUser from "./DelteUser";

const Mypage = () => {

  const email = localStorage.getItem('email');
  const userName = localStorage.getItem('userName');
  const createAt = localStorage.getItem('createAt');
  const imageUrl = localStorage.getItem('imageUrl'); 

  const [deletemodalShow, setDeleteModalShow] = useState(false);

  function deleteHandler() {
    setDeleteModalShow(true)
  }

  return (
    <div className={styles.Layout}>
      <main className={[styles.Content, styles.Clearfix].join(" ")}>
        <div className={styles.Clearfix}>
          <section className={styles.Member}><div className={styles.MemberSide}>
            <div className={[styles.MemberCard, styles.Clearfix].join(" ")}>
              <div className={styles.MemberCardBody}>
                <div className={[styles.Avatar, styles.TwMb2].join(" ")}>
                  {imageUrl === 'null' ? <img src="https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png" alt="profile_image" />:<img src={imageUrl} alt="profile_image" />}
                </div>
                <div>
                  <div className={[styles.TwFontBold, styles.TwMb1].join(" ")}>{userName}</div>
                </div>
              </div>
            </div>  <div className={[styles.MemberCard, styles.MemberMenu].join(" ")}>
              <ul>
                <li className={styles.Active}>
                  <a href="/mypage">회원정보 보기</a>
                </li><li>
                  <a href="/mypage/myboard">작성 글 보기</a>
                </li><li>
                  <a href="/mypage/myboard">좋아요 누른 글 보기</a>
                </li><li>
                  <a href="/mypage/myboard">작성 댓글 보기</a>
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

                      <div>{email}</div>
                    </li>
                    <li>
                      <label htmlFor="nick_name">닉네임<span className={styles.Required}>필수</span></label>

                      <div>{userName}</div>                  
                    </li>
                    <li>
                        <label htmlFor="profile_image">프로필 사진</label>
                        <div>{imageUrl === 'null' ? <img src='https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png' alt="이미지"/> : <img src={imageUrl} alt="이미지"/>}</div>
                    </li>
                    <li>
                      <label>가입일</label>
                      <div>{createAt}</div>
                    </li>
                  </ul>

                  <div className={[styles.TwFlex, styles.TwFlewWrap].join(" ")}>
                    <a href="mypage/password/validation" className={[styles.Link, styles.TwMr3].join(" ")}>회원정보 변경</a>

                    <a href="mypage/password/modify" className={[styles.Link, styles.TwMr3].join(" ")}>비밀번호 변경</a>

                    <div className={styles.TwFlex1}></div>

                    <a href="#/" onClick={deleteHandler} className={[styles.Link, styles.TwTextDanger].join(" ")}>탈퇴</a>
                    <DeleteUser
                      show={deletemodalShow}
                      onHide={() => setDeleteModalShow(false)}
                    />
                  </div>    </div>
              </div>
            </div>
          </section>      </div>
      </main>
    </div>
  );
}



export default Mypage;
