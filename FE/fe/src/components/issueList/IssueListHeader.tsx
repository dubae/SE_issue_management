import { useState, useEffect, useRef } from 'react'
import styled from 'styled-components'
import CheckBox from '../atom/CheckBox'
import DraftsOutlinedIcon from '@mui/icons-materials/DraftsOutlined';
import EmailOutlinedIcon from '@mui/icons-materials/EmailOutlined';
import ExpandMoreIcon from '@mui/icons-materials/ExpandMore';
import List from '../atom/List'
import useToggle from '../../hooks/useToggle'
import React from 'react';
function ListLeft(){
  return (
    <ListLeftBlock>
      <CheckBox/>
      <FilterTabBlock>
        <div><DraftsOutlinedIcon/> 열린 이슈 (0)</div>
        <div><EmailOutlinedIcon/> 닫힌 이슈 (0)</div>
      </FilterTabBlock>
    </ListLeftBlock>
  )
}
function ListRight(){
  const [type, setType] = useState<string>('')

  const assigneeToggle = useRef<HTMLDivElement>(null)
  const labelToggle = useRef<HTMLDivElement>(null)
  const milestoneToggle = useRef<HTMLDivElement>(null)
  const authorToggle = useRef<HTMLDivElement>(null)
  const listModal = useRef<HTMLDivElement>(null)

  const filterStandards = [
    {name:'담당자', ref: assigneeToggle},
    {name:'레이블', ref:labelToggle},
    {name:'마일스톤',ref: milestoneToggle},
    {name: '작성자', ref: authorToggle},
  ]
  const open = useToggle({toggle: [assigneeToggle, labelToggle, milestoneToggle, authorToggle ], modal: listModal, init:false})

  const handleClick = (clickTarget:string):void =>{
    setType(clickTarget)
  }

  const view = filterStandards.map((standard,idx)=><div ref={standard.ref} key={idx} onClick={()=>handleClick(standard.name)}>{standard.name}<ExpandMoreIcon/></div>)

  return (
    <>
      <FilterInListBlock> 
        {view}
        </FilterInListBlock>
      {open && <List type={type} modal={listModal}/>}
    </>
  )
}
function ListHead(){

  return (
    <>
      <ListHeadBlock>
        <ListLeft/>
        <ListRight/>
      </ListHeadBlock>
    </>
  )
}
const ListHeadBlock = styled.div`
display: flex;
justify-content: space-between;
background-color: ${({theme})=>theme.color.bgGrey};
border: ${({theme})=>theme.color.bgGrey};
border-bottom: 1px solid ${({theme})=>theme.color.lineGrey};
border-radius: 16px 16px 0 0;`
const ListLeftBlock =styled.div`
display: flex;`
const FilterTabBlock = styled.div`
display: flex;
div { 
  display: flex;
  align-items: center;
  margin-right: 10px;
}`

const FilterInListBlock=styled.div`
display: flex;
position: relative;
div { 
  display: flex;
  align-items: center;
  margin-right: 10px;
}`

export default ListHead