import React, { useState } from 'react'
import ReactiveButton from 'reactive-button'
import { Text } from 'react-native'

const CustomReactiveButton = (props) => {
  return (
    <ReactiveButton buttonState={props.state} onClick={props.onClickHandler} />
  )
}

export default CustomReactiveButton
