import React from 'react';
import SideMenu from '../../common/SideMenu/SideMenu';
import { Col, Row } from 'antd';
import TotalExample from '../../../QuotationAPI/TOTAL-example/TotalExample';

function UpbitMainPage() {
    return (
        <Row>
            <h3 style={{marginLeft:'180px', marginTop:'20px'}}>EXCHANGE</h3>
            <Col style={{marginTop:'17px', marginBottom: '20px'}} span={24}><TotalExample /></Col>
        </Row>
    );

}
export default UpbitMainPage;
