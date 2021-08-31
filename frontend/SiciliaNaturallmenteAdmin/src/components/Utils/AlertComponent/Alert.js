import React from 'react'
import {
  Alert,
  AlertIcon,
  AlertTitle,
  AlertDescription,
  CloseButton,
} from '@chakra-ui/react'

const CustomAlert = (props) => {
  return (
    <Alert status={props.status}>
      <AlertIcon />
      <AlertTitle mr={2}>{props.title}</AlertTitle>
      <AlertDescription>{props.descr}</AlertDescription>
    </Alert>
  )
}

export default CustomAlert
