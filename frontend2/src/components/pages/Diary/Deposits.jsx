import * as React from 'react';
import Title from './Title';
import { ResponsivePie } from '@nivo/pie'
import Api from '../../../functions/customApi';
import { useRecoilValue } from 'recoil';
import { userState } from '../../../functions/GlobalState';
import { useNavigate } from 'react-router-dom';

export default function Deposits() {

  const isLogin = useRecoilValue(userState);
  let [sumEtcCnt, setSumEtcCnt] = React.useState(0.0);
  const [bitCoinCnt, setBitCoinCnt] = React.useState(0.0);
  const [ethereumCnt, setEthereumCnt] = React.useState(0.0);
  const [rippleCnt, setRippleCnt] = React.useState(0.0);
  const [ADACnt, setADACnt] = React.useState(0.0);
  const [dogeCoinCnt, setDogeCoinCnt] = React.useState(0.0);
  const [etcCnt, setEtcCnt] = React.useState(0.0);
  const navigate = useNavigate();


  const getCoinCount = async () => {
    await Api.get("/api/v1/upbit/acount")
      .then(function (response) {
        console.log(response.data);
        (response.data).map(data=>settingCnt(data));
        setEtcCnt(sumEtcCnt);
      })
      .catch(function (err) {
        console.log(err);
        alert("계좌 조회 실패 : 업비트 키를 미등록하셨거나 잘못된 키입니다.");
        if (isLogin) {
          navigate('/upbit/infomation');
        }
      })
  };

  React.useEffect(() => {
    getCoinCount();
  }, []);

  function settingCnt(data) {
    if (data.currency === "XRP" && ( data.unit_currency === "KRW" || data.unit_currency === "BTC" || data.unit_currency === "USDT" )) {
      setRippleCnt(parseFloat(data.balance));
    }
    else if (data.currency === "BTC" && ( data.unit_currency === "KRW" || data.unit_currency === "BTC" || data.unit_currency === "USDT" )) {
      setBitCoinCnt(parseFloat(data.balance));
    }
    else if (data.currency === "ETH" && ( data.unit_currency === "KRW" || data.unit_currency === "BTC" || data.unit_currency === "USDT" )) {
      setEthereumCnt(parseFloat(data.balance));
    }
    else if (data.currency === "ADA" && ( data.unit_currency === "KRW" || data.unit_currency === "BTC" || data.unit_currency === "USDT" )) {
      setADACnt(parseFloat(data.balance));
    }
    else if (data.currency === "DOGE" && ( data.unit_currency === "KRW" || data.unit_currency === "BTC" || data.unit_currency === "USDT" )) {
      setDogeCoinCnt(parseFloat(data.balance));
    }
    else if (data.currency !== "KRW"){
      sumEtcCnt = sumEtcCnt + parseFloat(data.balance);
    }
  }


  const data = [
    {
      "id": "비트코인",
      "label": "비트코인",
      "value": bitCoinCnt.toFixed(2),
      "color": "hsl(309, 70%, 50%)"
    },
    {
      "id": "이더리움",
      "label": "이더리움",
      "value": ethereumCnt.toFixed(2),
      "color": "hsl(124, 70%, 50%)"
    },
    {
      "id": "리플",
      "label": "리플",
      "value": rippleCnt.toFixed(2),
      "color": "hsl(96, 70%, 50%)"
    },
    {
      "id": "에이다",
      "label": "에이다",
      "value": ADACnt.toFixed(2),
      "color": "hsl(204, 70%, 50%)"
    },
    {
      "id": "도지코인",
      "label": "도지코인",
      "value": dogeCoinCnt.toFixed(2),
      "color": "hsl(115, 70%, 50%)"
    },
    {
      "id": "기타",
      "label": "기타",
      "value": etcCnt.toFixed(2),
      "color": "hsl(246, 100%, 50%)"
    }
  ];

  return (
    <React.Fragment>
      <Title>보유 코인</Title>
      <ResponsivePie
        data={data}
        margin={{ top: 20, right: 80, bottom: 30, left: 60 }}
        innerRadius={0.5}
        padAngle={0.7}
        cornerRadius={3}
        activeOuterRadiusOffset={8}
        borderWidth={1}
        borderColor={{
          from: 'color',
          modifiers: [
            [
              'darker',
              0.2
            ]
          ]
        }}
        arcLinkLabelsSkipAngle={10}
        arcLinkLabelsTextColor="#333333"
        arcLinkLabelsThickness={2}
        arcLinkLabelsColor={{ from: 'color' }}
        arcLabelsSkipAngle={10}
        arcLabelsTextColor={{
          from: 'color',
          modifiers: [
            [
              'darker',
              2
            ]
          ]
        }}
        defs={[
          {
            id: 'dots',
            type: 'patternDots',
            background: 'inherit',
            color: 'rgba(255, 255, 255, 0.3)',
            size: 4,
            padding: 1,
            stagger: true
          },
          {
            id: 'lines',
            type: 'patternLines',
            background: 'inherit',
            color: 'rgba(255, 255, 255, 0.3)',
            rotation: -45,
            lineWidth: 6,
            spacing: 10
          }
        ]}

      />
    </React.Fragment>
  );
}