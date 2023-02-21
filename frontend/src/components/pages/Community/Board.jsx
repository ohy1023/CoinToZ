import React , { useEffect, useState }from 'react';
import axios from 'axios';
import PropTypes from 'prop-types';
import { useTheme, styled } from '@mui/material/styles';
import { Link } from "react-router-dom";
import CreateIcon from '@mui/icons-material/Create';
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableHead from '@mui/material/TableHead';
import TableBody from '@mui/material/TableBody';
import TableCell,{ tableCellClasses } from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableFooter from '@mui/material/TableFooter';
import TablePagination from '@mui/material/TablePagination';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import FirstPageIcon from '@mui/icons-material/FirstPage';
import KeyboardArrowLeft from '@mui/icons-material/KeyboardArrowLeft';
import KeyboardArrowRight from '@mui/icons-material/KeyboardArrowRight';
import LastPageIcon from '@mui/icons-material/LastPage';
import Post from './Post';
import SideMenu from '../../common/SideMenu/SideMenu';
import { Col, Row } from 'antd';

function TablePaginationActions(props) {
  const theme = useTheme();
  const { count, page, rowsPerPage, onPageChange } = props;

  const handleFirstPageButtonClick = (event) => {
    onPageChange(event, 0);
  };

  const handleBackButtonClick = (event) => {
    onPageChange(event, page - 1);
  };

  const handleNextButtonClick = (event) => {
    onPageChange(event, page + 1);
  };

  const handleLastPageButtonClick = (event) => {
    onPageChange(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };

  return (
    <Box sx={{ flexShrink: 0, ml: 2.5 }}>
      <IconButton
        onClick={handleFirstPageButtonClick}
        disabled={page === 0}
        aria-label="first page"
      >
        {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
      </IconButton>
      <IconButton
        onClick={handleBackButtonClick}
        disabled={page === 0}
        aria-label="previous page"
      >
        {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
      </IconButton>
      <IconButton
        onClick={handleNextButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="next page"
      >
        {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
      </IconButton>
      <IconButton
        onClick={handleLastPageButtonClick}
        disabled={page >= Math.ceil(count / rowsPerPage) - 1}
        aria-label="last page"
      >
        {theme.direction === 'rtl' ? <FirstPageIcon /> : <LastPageIcon />}
      </IconButton>
    </Box>
  );
}

TablePaginationActions.propTypes = {
  count: PropTypes.number.isRequired,
  onPageChange: PropTypes.func.isRequired,
  page: PropTypes.number.isRequired,
  rowsPerPage: PropTypes.number.isRequired,
};

const StyledTableCell = styled(TableCell)(({ theme }) => ({
  [`&.${tableCellClasses.head}`]: {
    backgroundColor: '#8C9BC1',
    color: theme.palette.common.white,
  },
  [`&.${tableCellClasses.body}`]: {
    fontSize: 20,
  },
}));

export default function Board() {
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const [posts, setPosts] = useState([]);

  useEffect(() => {
      axios.get('api/v1/posts') 
        .then((response) => {
            setPosts(response.data.result);
            console.log(posts);
        })
        .catch(function(error){
            console.log(error);
        })
  }, []);

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - posts.length) : 0;

  const handleChangePage = (event, newPage) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <Row>
      <Col span={19}><h3 style={{marginLeft:'180px', marginTop:'20px'}}>COMMUNITY</h3></Col>
      <Col span={5}><Link style={{marginTop:'20px'}} className="btn btn-outline-primary" to="/post"><CreateIcon></CreateIcon> 글 작성</Link></Col>
        <Col span={24}>
          <Box sx={{pl : '12%', pr: '12%', pt: '15px', height : '80vh'}}>
          <Paper sx={{ width: '100%', height:'90%' , overflow: 'hidden'}}>
          <TableContainer sx={{maxHeight: '100%'}}>
            <Table sx={{ minWidth: '80%' }} stickyHeader aria-label="sticky table">
              <TableHead>
                  <TableRow>
                      <StyledTableCell style={{fontSize:18}} align='center'>No</StyledTableCell>
                      <StyledTableCell style={{fontSize:18}} align='center'>제목</StyledTableCell>
                      <StyledTableCell style={{fontSize:18}} align='center'>글쓴이</StyledTableCell>
                      <StyledTableCell style={{fontSize:18}} align='center'>작성일</StyledTableCell>
                      <StyledTableCell style={{fontSize:18}} align='center'>좋아요</StyledTableCell>
                  </TableRow>
              </TableHead>
              <Post posts={posts} rowsPerPage={rowsPerPage} emptyRows={emptyRows} page={page}></Post>
            </Table>
            <TablePagination
              rowsPerPageOptions={[5, 10, 25, { value: -1, label: 'All' }]}
              component="div"
              count={posts.length}
              rowsPerPage={rowsPerPage}
              page={page}
              onPageChange={handleChangePage}
              onRowsPerPageChange={handleChangeRowsPerPage}
              ActionsComponent={TablePaginationActions}
            />
          </TableContainer>
          </Paper>
          </Box>
        </Col>
    </Row>
  );
}