import React , { useEffect, useState }from 'react';
import TableCell from '@mui/material/TableCell';
import TableRow from '@mui/material/TableRow';
import TableBody from '@mui/material/TableBody';
import { NavLink } from 'react-router-dom';

const Post = ({posts, rowsPerPage, emptyRows, page}) => {

    return (
        <TableBody>
          {(rowsPerPage > 0
            ? posts.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
            : posts
          ).map((post) => (
            <TableRow key={post.id}>
              <TableCell align='center' style={{ width: 80 }}>
                {post.id}
              </TableCell>
              <TableCell component="th" scope="row">
                <NavLink style={{fontSize:18}} to={`/postDetail/${post.id}`}>{post.title}</NavLink>
              </TableCell>
              <TableCell align='center' style={{ width: 160 }} >
                {post.userName}
              </TableCell>
              <TableCell align='center' style={{ width: 160 }} >
                {post.createdAt}
              </TableCell>
              <TableCell align='center' style={{ width: 120 }}>
                {post.likeCount}
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

export default Post;