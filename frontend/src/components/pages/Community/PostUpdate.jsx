import { useLocation, useNavigate } from "react-router-dom";
import {  useState } from "react";
import Api from '../../../functions/customApi';

function PostUpdate(){

    const navigate = useNavigate();

    const location = useLocation();
    const { board } = location.state;

    const [title, setTitle] = useState(board.title);
    const [body, setBody] = useState(board.body);

    const changeTitle = (event) => {
        setTitle(event.target.value);
    }

    const changeBody = (event) => {
        setBody(event.target.value);
    }

    const postUpdate = async () => {

        const req = {
            title: title,
            body: body
        }

        await Api.put(`/api/v1/posts/${board.postId}`, req)
            .then((response) => {
                console.log("[BbsUpdate.js] updateBbs() success :D");
                console.log(response.data);

                navigate(`/postDetail/${board.postId}`); // 글 상세로 이동

            })
            .catch((err) => {
                console.log(err);
                alert("본인이 작성한 글만 수정할 수 있습니다")
            });
    }

    return (

        <div className="root">
            <h3>게시글 수정</h3>
            <table className="table">
                <tbody>
                <tr className="table-striped">
                    <th className="th">작성자</th>

                    <td><input type="text" className="form-control"  value={board.id} size="50px" readOnly /></td>
                </tr>

                <tr>
                    <th className="th">제목</th>
                    <td><input type="text" className="form-control" value={title} onChange={changeTitle} size="50px" /></td>
                </tr>

                <tr>
                    <th className="th">내용</th>
                    <td><textarea className="form-control" value={body} onChange={changeBody} rows="10" ></textarea></td>
                </tr>
                </tbody>
            </table>

            <div className="my-3 d-flex justify-content-center">
                <button className="btn btn-dark" onClick={postUpdate}><i className="fas fa-pen"></i> 수정하기</button>
            </div>
        </div>

    );

}
export default PostUpdate;