import React from 'react'
import { Stack } from '@chakra-ui/react'

const ContainerFlex = (props) => {
  return (
    <Stack
      {...props}
      bg={props.bg}
      spacing={'auto'}
      color={props.color}
      px={props.paddingX}
      py={props.paddingY}
      flexDirection={props.dir || 'row'}
    >
      {props.children}
    </Stack>
  )
}

export default ContainerFlex
