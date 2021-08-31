import React from 'react'
import {
  FormControl,
  FormLabel,
  FormErrorMessage,
  FormHelperText,
  Input,
} from '@chakra-ui/react'

const CustomInput = (props) => {
  return (
    <FormControl id={props.id}>
      <FormLabel>{props.label}</FormLabel>
      <Input
        {...props}
        type={props.type}
        value={props.value}
        name={props.name}
        placeholder={props.placeholder}
      />
      {props.children}
    </FormControl>
  )
}

export default CustomInput
