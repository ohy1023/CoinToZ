import React from 'react';
import { useNavigate } from 'react-router-dom';
import { AiFillGithub } from 'react-icons/ai';
import logo from '../../../assets/appBar/logo.png';
import styled from 'styled-components';
import { css } from 'styled-components';
import { Col, Row } from 'antd';

const container = css`
	max-width: 1400px;
	width: 100%;
	display: flex;
	margin: 30px auto;
`;

const FooterComponent = styled.footer`
	width: 100%;
	height: 180px;
	display: flex;
	justify-content: center;
	background: #e2e2e2;
	.content {
		${container}
		display: flex;
		// align-items: center;
		// justify-content: space-between;
		.rep {
			display: flex;
			// align-items: center;
			> div {
				:nth-child(1) {
					padding: 0 0.5rem;
				}
			}
		}
		a {
			display: flex;
			// justify-content: center;
			padding: 0.125rem 0.125rem;
			align-items: left;
			font-style: italic;
			margin: 0.125rem 0.25rem;
			color: #333;
			:active,
			:link,
			:visited {
				color: #333;
			}
		}
		img {
			width: 110px;
			height: 80px;
		}
		> div {
			:nth-child(1) {
				display: flex;
				align-items: center;
				justify-content: center;
			}
			> div {
				margin: 0.5rem 0;
			}
		}
	}
`;

function Footer() {

  const FOOTER_LIST = {
    repository1: 'https://gitlab.com/people-of-development1/financial-final-project-be',
    repository2: 'https://gitlab.com/people-of-development1/financial-final-project-fe',
    copyright: 'CoinToZ © 2023 All rights reserved.',
    be1: [
      { name: 'Kang DongYeon', repository: 'https://github.com/dongyeon-0822' },
      { name: 'Kang Subin', repository: 'https://github.com/kang-subin' },
      { name: 'Kim Sangho', repository: 'https://github.com/sangho527' },
    ],
    be2: [
      { name: 'Son seyeol', repository: 'https://github.com/ccodding' },
      { name: 'OHyungSang', repository: 'https://github.com/ohy1023' },
      { name: 'Yim Hakjun', repository: 'https://github.com/hakjun1' },
    ],
  };
  const navigate = useNavigate();

  return (
    <FooterComponent>
      <div className="content">
      <Row style={{width : '100%'}}>
        <Col span={6}>
          <div>
            <Row><h5>ABOUT</h5></Row>
            <ul>
              <li>
                <a href={FOOTER_LIST.repository1} target="_blank" rel="noreferrer">
                  [BackEnd] GitLab Repository
                </a>
              </li>
              <li>
                <a href={FOOTER_LIST.repository2} target="_blank" rel="noreferrer">
                  [FrontEnd] GitLab Repository
                </a>
              </li>
            </ul>
          </div>
        </Col>
        
        <Col span={12}>
          <div>
            <Row><h5>DEVELOPER</h5></Row>
            <div className="rep">
              {FOOTER_LIST.be1.map((v, i) => (
                <a href={v.repository} key={i} target="_blank" rel="noreferrer">
                  <div>
                    <AiFillGithub />
                  </div>
                  <div>{v.name}</div>
                </a>
              ))}
            </div>
            <div className="rep">
              {FOOTER_LIST.be2.map((v, i) => (
                <a href={v.repository} key={i} target="_blank" rel="noreferrer">
                  <div>
                    <AiFillGithub />
                  </div>
                  <div>{v.name}</div>
                </a>
              ))}
            </div>
          </div>
        </Col>

        <Col span={4}>
          <div style={{color : '#757575', marginTop: '90px'}}>{FOOTER_LIST.copyright}</div>
        </Col>
      </Row>
      </div>
    </FooterComponent>
  );
}

export default Footer;