import React from 'react'
import { AppBar, Toolbar, Typography, IconButton, Menu, MenuItem } from '@mui/core';
import { styled } from '@mui/material/styles'
import AccountCircleIcon from '@mui/icons-materials/AccountCircle';
const useStyle = styled(()=>({
  typographyStyles: {
    flex: 1,
  }}))

function Header(){
  const classes = useStyle()
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);
  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <AppBar position="static" color="transparent">
      <Toolbar>
        <Typography className={classes.typographyStyles}>Issue Tracker</Typography>
        <div> 
        <IconButton
          aria-label="account of current user"
          aria-controls="menu-appbar"
          aria-haspopup="true"
          onClick={handleMenu}
          color="inherit"
        >
          <AccountCircleIcon />
        </IconButton>
        <Menu
          id="menu-appbar"
          anchorEl={anchorEl}
          open={open}
          onClose={handleClose}
        >
        <MenuItem onClick={handleClose}>My account</MenuItem>
        <MenuItem onClick={handleClose}>Logout</MenuItem>
      </Menu></div>

      </Toolbar>
    </AppBar>
  )
}

export default Header