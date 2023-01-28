import * as React from 'react';
import { Button } from 'react-bootstrap';
import Api from './customApi';


export default function Test() {

  const getInfo = async () => {
    await Api.get("/api/v1/users/test")
    .then(function (response) {
      alert(response.data)
    })
    .catch(function (err) {
      console.log(err);
      alert("로그인하고 테스트해주세요")
    });
    
  }


  return (
    <>
      <Button
      variant="primary"
      onClick={getInfo}
      type="submit"
      sx={{ mt: 3, mb: 2 }}
      >
        Test
      </Button>
    </>
    
  );

}