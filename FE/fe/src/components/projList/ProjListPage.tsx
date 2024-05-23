import Header from '../header/Header'
import styled from 'styled-components'
import ProjList from './ProjList'
import React from 'react'

function ProjListPage(){

  return (
    <>
      <Header/>
      <Body>
        <ProjList/>
      </Body>
    </>
  )
}

const Body = styled.div`
padding: 80px;`

export default ProjListPage