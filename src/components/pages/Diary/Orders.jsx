import * as React from 'react';
import Title from './Title';
import { Box } from '@mui/material';
import Api from '../../../functions/customApi';
import UpdateModal from './UpdateModal';
import { GridColDef, GridActionsCellItem, GridToolbarContainer, GridToolbarExport, DataGrid } from '@mui/x-data-grid';
import AutoFixHighOutlinedIcon from '@mui/icons-material/AutoFixHighOutlined';
import DatePicker from "react-datepicker";
import styled from 'styled-components';
import { ko } from 'date-fns/esm/locale';
import "react-datepicker/dist/react-datepicker.css";
import { Button, Table } from 'react-bootstrap';


const SDatePicker = styled(DatePicker)`
  width:150px;
  height:42px;
  padding: 8px 20px;
  border-radius:4px;
  font-size:12px;
  background-color:transparent;
  border: 1px solid;
`;


export default function Orders() {
  const columns: GridColDef[] = [
    { field: 'id', headerName: 'id', hide: true },
    { field: 'market', headerName: '코인', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'bid_created_at', headerName: '매수 시간', width: 125, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'bid_price', headerName: '매수 가격', width: 125, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'ask_price', headerName: '매도 가격', width: 125, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'volume', headerName: '수량', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'arbitrage', headerName: '차익', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'revenue', headerName: '수익률', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'comment', headerName: '메모(투자 계기)', width: 280, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    {
      field: 'actions',
      type: 'actions',
      headerName: '수정',
      width: 50,
      headerAlign: 'center', align: 'center',
      headerClassName: 'super-app-theme--header',
      getActions: (params) => [
        <GridActionsCellItem icon={<AutoFixHighOutlinedIcon />} onClick={(e) => {
          onButtonClick(e, params.row)
          updateHandler()
        }} label="Update" />
      ]
    }

  ];

  function updateHandler() {
    setUpdateModalShow(true)
  }

  const onButtonClick = (e, row) => {
    e.stopPropagation();
    setClickedRow(row)
  };

  const [diary, setDiary] = React.useState([]);
  const [updatemodalShow, setUpdateModalShow] = React.useState(false);
  const [clickedRow, setClickedRow] = React.useState(0);
  const [changeComment, setChangeComment] = React.useState(0);
  const [startDate, setStartDate] = React.useState(new Date());
  const [endDate, setEndDate] = React.useState(new Date());

  const find = async () => {
    console.log(startDate);
    console.log(endDate);
  }

  const getCoinInfo = async () => {
    await Api.get("/api/v1/diary/list2")
      .then(function (response) {
        setDiary(response.data)
      })
      .catch(function (err) {
        console.log(err);
        alert("매매일지 조회 실패");
      })
  };

  React.useEffect(() => {
    getCoinInfo();
  }, [changeComment]);


  return (
    <>
      <Box
        sx={{
          height: 600,
          width: '100%',
          '& .super-app-theme--header': {
            backgroundColor: 'rgba(54, 73, 81, 1)',
            color: 'white',
          },
        }}>
        <Title>매매 일지</Title>
        <Table>
          <tbody>
            <tr>
              <td style={{width: '75px'}}>
                <SDatePicker
                  locale={ko}
                  dateFormat="yyyy년 MM월 dd일"
                  selected={startDate}
                  onChange={(date: Date) => setStartDate(date)}
                  selectsStart
                  maxDate={new Date()}
                  startDate={startDate}
                  endDate={endDate}
                  form="external-form"
                />
              </td>
              <td style={{width: '30px', fontSize: '23px'}}>
                ~
              </td>
              <td style={{width: '75px'}}>
                <SDatePicker
                  locale={ko}
                  dateFormat="yyyy년 MM월 dd일"
                  selected={endDate}
                  onChange={(date: Date) => setEndDate(date)}
                  selectsEnd
                  minDate={startDate}
                  startDate={startDate}
                  endDate={endDate}
                  form="external-form"
                />
              </td>
              <td>
                <form id="external-form">
                  <Button type="submit" onClick={find} >
                    검색
                  </Button>
                </form>
              </td>
            </tr>
          </tbody>


        </Table>
        <DataGrid rows={diary} columns={columns} components={{
          Toolbar: CustomToolbar,
        }} />
      </Box>
      <UpdateModal
        diaryid={clickedRow.id === 'undefined' ? 0 : clickedRow.id}
        show={updatemodalShow}
        onHide={() => setUpdateModalShow(false)}
        onExit={() => setChangeComment(changeComment + 1)}
      />
    </>
  );
}

function CustomToolbar() {
  return (
    <GridToolbarContainer>
      <GridToolbarExport
        csvOptions={{
          fileName: 'TradingDiary',
          delimiter: ',',
          utf8WithBom: true,
        }} />
    </GridToolbarContainer>
  );
}