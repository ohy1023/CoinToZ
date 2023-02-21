import React , { useEffect, useState }from 'react';
import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import TableBody from '@mui/material/TableBody';


const Comment = ({comments, rowsPerPage, emptyRows, page}) => {

    return (
        <TableBody>
          {(rowsPerPage > 0
            ? comments.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            : comments
          ).map((comment) => (
            <TableRow key={comment.id}>
              <TableCell style={{ width: 180, fontWeight:'bold', textAlign: 'center', backgroundColor: '#e2e2e2'}} align="left">
                {comment.userName}
              </TableCell>
              <TableCell component="th" scope="row" >
                {comment.comment}
              </TableCell>
              <TableCell style={{ width: 200, fontFamily : 'monospace'}} align="right">
                {comment.createdAt}
              </TableCell>
            </TableRow>
          ))}

          {emptyRows > 0 && (
            <TableRow style={{ height: 53 * emptyRows }}>
              <TableCell colSpan={6} />
            </TableRow>
          )}
        </TableBody>
    );
};

export default Comment;