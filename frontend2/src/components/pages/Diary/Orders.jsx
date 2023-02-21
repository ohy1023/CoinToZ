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
import DeleteModal from './DeleteModal';
import DeleteIcon from '@mui/icons-material/Delete';


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
    { field: 'created_at', headerName: '거래 시간', width: 125, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'market', headerName: '코인', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'ord_type', headerName: '거래 방식', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'side', headerName: '거래 종류', width: 100, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'price', headerName: '거래 가격', width: 150, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'volume', headerName: '수량', width: 150, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    { field: 'comment', headerName: '메모(투자 계기)', width: 268, headerAlign: 'center', align: 'center', headerClassName: 'super-app-theme--header' },
    {
      field: 'actions',
      type: 'actions',
      headerName: '수정 삭제',
      width: 125,
      headerAlign: 'center', align: 'center',
      headerClassName: 'super-app-theme--header',
      getActions: (params) => [
        <GridActionsCellItem icon={<AutoFixHighOutlinedIcon />} onClick={(e) => {
          onButtonClick(e, params.row)
          updateHandler()
        }} label="Update" />,
        <GridActionsCellItem icon={<DeleteIcon/>} onClick={(e)=>{
          onButtonClick(e,params.row)
          deleteHandler()
        }} label="Delete" />
      ]
    }

  ];

  function updateHandler() {
    setUpdateModalShow(true)
  }

  function deleteHandler() {
    setDeleteModalShow(true)
  }

  function startDateFormat(date) {
    let dateFormat = date.getFullYear() +
      '-' + ((date.getMonth() + 1) < 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
      '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate())) + 'T00:00:00'
    return dateFormat;
  }

  function endDateFormat(date) {
    let dateFormat = date.getFullYear() +
      '-' + ((date.getMonth() + 1) < 9 ? "0" + (date.getMonth() + 1) : (date.getMonth() + 1)) +
      '-' + ((date.getDate()) <= 9 ? "0" + (date.getDate()) : (date.getDate())) + 'T23:59:59'
    return dateFormat;
  }

  const onButtonClick = (e, row) => {
    e.stopPropagation();
    setClickedRow(row)
  };

  const [diary, setDiary] = React.useState([]);
  const [updatemodalShow, setUpdateModalShow] = React.useState(false);
  const [deletemodalShow, setDeleteModalShow] = React.useState(false);
  const [clickedRow, setClickedRow] = React.useState(0);
  const [changeComment, setChangeComment] = React.useState(0);
  const [startDate, setStartDate] = React.useState(new Date());
  const [endDate, setEndDate] = React.useState(new Date());

  const getCoinInfo = async () => {
    await Api.get("/api/v1/diary/list")
      .then(function (response) {
        setDiary(response.data)
      })
      .catch(function (err) {
        console.log(err);
      })
  };

  React.useEffect(() => {
    getCoinInfo();
  }, [changeComment]);

  const searchByDate = async () => {
    await Api.get(`api/v1/diary/search`,{
      params:{
        startDate:startDateFormat(startDate),
        endDate:endDateFormat(endDate)
      }
    }).then(function(response) {
      setDiary(response.data.result)
      alert("검색 완료");
    })
  .catch(function(error){
    console.log(error);
    alert('검색 실패');
  })}


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
              <td style={{ width: '75px' }}>
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
              <td style={{ width: '30px', fontSize: '23px' }}>
                ~
              </td>
              <td style={{ width: '75px' }}>
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
                  <Button type="button" onClick={searchByDate} >
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
      <DeleteModal
        diaryid={clickedRow.id === 'undefined' ? 0 : clickedRow.id}
        show={deletemodalShow}
        onHide={() => setDeleteModalShow(false)}
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