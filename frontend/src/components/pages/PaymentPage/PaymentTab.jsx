import React , { useEffect, useState }from 'react';
import axios from 'axios';
import Box from '@mui/material/Box';
import Tab from '@mui/material/Tab';
import Tabs from '@mui/material/Tabs';
import PropTypes from 'prop-types';
import { useTheme } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import { Button } from '@mui/material';
import styled from 'styled-components';
import Api from '../../../functions/customApi';
import styles from './PaymentTab.module.css';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`full-width-tabpanel-${index}`}
      aria-labelledby={`full-width-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 2 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `full-width-tab-${index}`,
    'aria-controls': `full-width-tabpanel-${index}`,
  };
}


export default function PaymentTab() {
  const [value, setValue] = React.useState(0);
  const [market, setMarket] = useState([]);
  const theme = useTheme();
  const [account, setAccount] = useState([]);


  useEffect(() => {
    getInfo();
    axios.get('api/v1/upbit/getMarket') 
        .then((response) => {
          console.log(response)
          setMarket(response.data);
          // console.log(market);
        })
        .catch(function(error){
            console.log(error);
        })
  }, [sessionStorage.getItem("temp")]);

  const getInfo = async () => {
    await Api.get("/api/v1/users")
      .then(function (response) {
        console.log(response.data.result);
        setAccount(response.data.result);
      })
      .catch(function (err) {
        console.log(err);
      })
  };

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  // ?????? ??????
  const handleSubmit = (e) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const orderData = {
      market: data.get('market'),
      ord_type: data.get('orderType'),
      price: data.get('price'),
      volume: data.get('volume'),
    };
    orderData.side = 'bid';

    {
      onhandlePost(orderData);
    }
    console.log(orderData);
  }

  // ?????? ?????? 
  const handleSubmit_2 = (e) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const orderData = {
      market: data.get('market'),
      ord_type: data.get('orderType'),
      price: data.get('price'),
      volume: data.get('volume'),
    };
    orderData.side = 'ask';

    {
      onhandlePost_2(orderData);
    }
    console.log(orderData);
  }

  // ?????? ?????? 
  const onhandlePost = async (data) => {
    const { market, ord_type, price, side, volume } = data;
    const postData = { market, ord_type, price, side, volume };

    Api.post('/api/v1/upbit/order', postData)
        .then(response => {
            alert('????????? ?????????????????????.');
            console.log("????????? ?????????????????????" + postData);
        })
        .catch(error => {
          console.log(error);
          const message = JSON.parse(error.response.data.split(': ')[1])[0].error['message']
          console.log(message);
          alert(message);
        });
  };

  // ?????? ??????
  const onhandlePost_2 = async (data) => {
    const { market, ord_type, price, side, volume } = data;
    const postData = { market, ord_type, price, side, volume };

    Api.post('/api/v1/upbit/order', postData)
        .then(response => {
            alert('????????? ?????????????????????.');
            console.log("????????? ?????????????????????" + postData);
        })
        .catch(error => {
            console.log(error);
            const message = JSON.parse(error.response.data.split(': ')[1])[0].error['message']
            console.log(message);
            alert(message);
        });
  };

  // ?????? ??????
  const ordType = [
    {
      value: 'limit',
      label: '????????? ??????',
    }
    ,{
      value: 'price',
      label: '????????? ??????'
    }
  ];

  // ?????? ??????
  const ordType_2 = [
    {
      value: 'limit',
      label: '????????? ??????',
    }
    ,{
      value: 'market',
      label: '????????? ??????'
    }
  ];


  return (
    <Box sx={{width: '100%', height: '100%', backgroundColor:'#eee'}}>
      <Tabs
        value={value}
        onChange={handleChange}
        textColor="inherit"
        variant="fullWidth"
        aria-label="full width tabs example"
      >
        <Tab label="??????" {...a11yProps(0)} />
        <Tab label="??????" {...a11yProps(1)} />
      </Tabs>
        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 0.5 }}>
          <FormControl>
            <TabPanel className={styles.tab} value={value} index={0} dir={theme.direction}>
              <div>
                  <TextField
                    fullWidth
                    id="filled-select-currency"
                    select
                    label="?????? ?????? *"
                    name="orderType"
                    defaultValue=""
                    helperText="?????? ????????? ??????????????????."
                    variant="filled"

                  >
                    {ordType.map((option) => (
                      <MenuItem key={option.value} value={option.value}>
                        {option.label}
                      </MenuItem>
                    ))}
                  </TextField>

                  <TextField
                    fullWidth
                    id="filled-select-currency"
                    select
                    label="?????? ID * "
                    name="market"
                    defaultValue=""
                    helperText="??????(??????)??? ??????????????????."
                    variant="filled"
                  >
                    {market.map((option) => (
                      <MenuItem key={option.market} value={option.market}>
                        {option.korean_name} ({option.market})
                      </MenuItem>
                    ))}
                  </TextField>

                  <TextField
                    fullWidth
                    id="filled-required"
                    label="?????????"
                    name="volume"
                    defaultValue=""
                    helperText="???????????? ??????????????????."
                    variant="filled"
                  />

                  <TextField
                    fullWidth
                    id="filled-required"
                    label="?????? ?????? * "
                    name="price"
                    defaultValue=""
                    helperText="?????? ????????? ??????????????????. "
                    variant="filled"
                  />

              </div>
              <div>
              {account.needUpbitKey === false ? (
                <Button 
                  variant="contained"
                  type="submit"
                  size="large"
                  className={"bottomButton"}
                  sx={{ mt: 2.0 }}>
                  ????????????
                </Button>
              ) : (
                <>
                <Button 
                  disabled='true'
                  variant="contained"
                  type="submit"
                  size="large"
                  className={"bottomButton"}
                  sx={{ mt: 2.0 }}>
                  ????????????
                </Button>
                <div className={styles.caption}>????????? ??? ????????? ?????? ?????? ??? ????????? ??? ????????????.</div>
                </>
              )}
              </div>
            </TabPanel>
          </FormControl>
        </Box>
        <Box component="form" noValidate onSubmit={handleSubmit_2} sx={{ mt: -2.8 }}>
          <FormControl>
            <TabPanel className={styles.tab} value={value} index={1} dir={theme.direction}>
              <div>
                <TextField
                  fullWidth
                  id="filled-select-currency"
                  select
                  label="?????? ?????? * "
                  name="orderType"
                  defaultValue=""
                  helperText="?????? ????????? ??????????????????."
                  variant="filled"
                >
                  {ordType_2.map((option) => (
                    <MenuItem key={option.value} value={option.value}>
                      {option.label}
                    </MenuItem>
                  ))}
                </TextField>

                <TextField
                  fullWidth
                  id="filled-select-currency"
                  select
                  label="?????? ID * "
                  name="market"
                  defaultValue=""
                  helperText="??????(??????)??? ??????????????????."
                  variant="filled"
                >
                  {market.map((option) => (
                    <MenuItem key={option.market} value={option.market}>
                      {option.korean_name} ({option.market})
                    </MenuItem>
                  ))}
                </TextField>

                <TextField
                  fullWidth
                  id="filled-required"
                  label="????????? * "
                  name="volume"
                  defaultValue=""
                  helperText="???????????? ??????????????????."
                  variant="filled"
                />

                <TextField
                  fullWidth
                  id="filled-required"
                  label="?????? ??????"
                  name="price"
                  defaultValue=""
                  helperText="?????? ????????? ??????????????????."
                  variant="filled"
                />

              </div>
              <div>
                {account.needUpbitKey === false ? (
                  <Button 
                    variant="contained"
                    type="submit"
                    size="large"
                    className={"bottomButton"}
                    sx={{ mt: 2.0 }}>
                    ????????????
                  </Button>
                ) : (
                  <>
                  <Button 
                    disabled='true'
                    variant="contained"
                    type="submit"
                    size="large"
                    className={"bottomButton"}
                    sx={{ mt: 2.0 }}>
                    ????????????
                  </Button>
                  <div className={styles.caption}>????????? ??? ????????? ?????? ?????? ??? ????????? ??? ????????????.</div>
                  </>
                )}
              </div>
            </TabPanel>
          </FormControl>
        </Box>    
    </Box>
  );
}