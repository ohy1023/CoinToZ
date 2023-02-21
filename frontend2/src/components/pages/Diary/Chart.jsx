import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import { LineChart, Line, XAxis, YAxis, Label, ResponsiveContainer } from 'recharts';
import Title from './Title';
import Api from '../../../functions/customApi';
import moment from 'moment';
import { Button } from 'react-bootstrap';


export default function Chart() {

  const theme = useTheme();

  const [rate, setRate] = React.useState([]);

  const [count, setCount] = React.useState(0);

  function useInterval(callback, delay) {
    const savedCallback = React.useRef();
    React.useEffect(() => {
      savedCallback.current = callback;
    }, [callback]);

    React.useEffect(() => {
      function tick() {
        savedCallback.current();
      }
      if (delay !== null) {
        let id = setInterval(tick, delay);
        return () => clearInterval(id);
      }
    }, [delay]);
  }

  function Test() {
    useInterval(() => {
      setCount(count => count + 1);
      console.log(count)
    }, 500);
  }

  // Generate Sales Data
  function createData(time, amount) {
    return { time, amount };
  }

  Test();

  React.useEffect(() => {
    Api.get('api/v1/upbit/revenue')
      .then(function (response) {
        setRate(rate.concat(createData(moment().format("HH:mm"), response.data.result)));
      })
  }, [count]);


  return (
    <React.Fragment>
      <Title>수익률</Title>
      <ResponsiveContainer>
        {/* <Button onClick={window.location.reload} /> */}
        <LineChart
          data={rate}
          margin={{
            top: 16,
            right: 16,
            bottom: 0,
            left: 24,
          }}
        >
          <XAxis
            dataKey="time"
            stroke={theme.palette.text.secondary}
            style={theme.typography.body2}
          />
          <YAxis
            stroke={theme.palette.text.secondary}
            style={theme.typography.body2}
          >
            <Label
              angle={270}
              position="left"
              style={{
                textAnchor: 'middle',
                fill: theme.palette.text.primary,
                ...theme.typography.body1,
              }}
            >
              수익률(%)
            </Label>
          </YAxis>
          <Line
            isAnimationActive={false}
            type="monotone"
            dataKey="amount"
            stroke={theme.palette.primary.main}
            dot={false}
          />
        </LineChart>
      </ResponsiveContainer>
    </React.Fragment>
  );
}