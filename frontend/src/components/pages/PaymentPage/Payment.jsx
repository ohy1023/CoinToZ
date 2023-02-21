import * as React from 'react';
import {useState, useEffect} from "react";
import { useRecoilState } from "recoil";
import styles from "./Payment.css";
import axios from "axios";
import styled from "styled-components";
import { useFetchMarketCode } from "use-upbit-api";
import { marketCodesState } from "../../../QuotationAPI/TOTAL-example/atom";
import PaymentTab from './PaymentTab';
import CoinSelector from '../../../QuotationAPI/TOTAL-example/CoinSelector';
import RealTimeChart from '../../../QuotationAPI/TOTAL-example/RealTimeChart';
import CoinInfo from '../../../QuotationAPI/TOTAL-example/CoinInfo';
import RealTimeOrderBook from '../../../QuotationAPI/TOTAL-example/RealTimeOrderBook';
import RealTimeTradeHistory from '../../../QuotationAPI/TOTAL-example/RealTimeTradeHistory';

export default function Payment(){
    const DisplayBoard = styled.main`
        width: 1250px;
        margin: 0 auto;
        display: grid;
        grid-template-columns: 300px 950px;
        background-color: whitesmoke;

        font-family: Arial, Helvetica, sans-serif;

        *::-webkit-scrollbar,
        *::-webkit-scrollbar-thumb {
            width: 0px;
        }

        *::-webkit-scrollbar-thumb {
        }
        *:hover::-webkit-scrollbar,
        *:hover::-webkit-scrollbar-thumb {
            width: 26px;
            border-radius: 13px;
            background-clip: padding-box;
            border: 12px solid transparent;
            color: grey;
        }

        *:hover::-webkit-scrollbar-thumb {
            box-shadow: inset 0 0 0 10px;
        }

        box-shadow: rgba(0, 0, 0, 0.24) 0px 3px 8px;
        `;
    const DetailLayout = styled.div`
        height: 800px;
        background-color: whitesmoke;
        padding: 5px;
        display: grid;
        gap: 5px;
        grid-template-columns: 1fr 1fr 1.1fr;
        grid-template-rows: 105px 300px 1fr;
        `;

    const [side, setSide] = useState('');
    const [ordType, setOrdType] = useState('');
    const [market, setMarket] = useState('');
    const [volume, setVolume] = useState('');
    const [price, setPrice] = useState('');
    const { isLoading, marketCodes: fetchedMC } = useFetchMarketCode();
    const [marketCodes, setMarketCodes] = useRecoilState(marketCodesState);

    const order = async () => {
        axios
            .get('/api/v1/upbit/getMarket', {
                // side: side,
                // ordType: ordType,
                // market: market,
                // volume: volume,
                // price: price,
            })
            .then(response => {
                console.log('User profile', response.data);
            })
            .catch(error => {
                console.log('주문이 취소되었습니다.', error.response.data);
            });
    }


return (
    <>
    <DisplayBoard>
      <CoinSelector />
        <DetailLayout>
            <CoinInfo />
            <RealTimeChart />
            <PaymentTab/>
            <RealTimeTradeHistory />
        </DetailLayout>
    </DisplayBoard>
    </>
);
}








