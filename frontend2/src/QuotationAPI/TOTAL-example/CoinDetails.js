import styled from "styled-components";
import RealTimeChart from "./RealTimeChart";
import CoinInfo from "./CoinInfo";
import RealTimeOrderBook from "./RealTimeOrderBook";
import RealTimeTradeHistory from "./RealTimeTradeHistory";
import PaymentTab from '../../components/pages/PaymentPage/PaymentTab';

const DetailLayout = styled.div`
  height: 800px;
  background-color: whitesmoke;
  padding: 5px;
  display: grid;
  gap: 5px;
  grid-template-columns: 1fr 1fr 1.1fr;
  grid-template-rows: 105px 300px 1fr;
`;

function CoinDetails() {
  return (
    <DetailLayout>
      <CoinInfo />
      <RealTimeChart />
      {/* <RealTimeOrderBook /> */}
      <PaymentTab/>
      <RealTimeTradeHistory />
    </DetailLayout>
  );
}

export default CoinDetails;
