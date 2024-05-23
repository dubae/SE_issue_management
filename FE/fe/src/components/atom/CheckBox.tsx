import React, { useState } from 'react'
import Checkbox from '@mui/material/Checkbox';

function CheckBox(){
  const [checked, setChecked] = useState(false);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setChecked(event.target.checked);
  };
  return (
    <Checkbox
      checked={checked}
      onChange={handleChange}
      color={"primary"}
      inputProps={{ 'aria-label':a ,'primary checkbox' }}
    />
  )
}

export default CheckBox