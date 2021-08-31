import React from 'react'
import ContainerFlex from '../components/Utils/FlexComponent/containerHorizzontal'
import { useColorModeValue } from '@chakra-ui/color-mode'
import { Heading } from '@chakra-ui/layout'
import DarkModeSwitch from '../components/homepage/DarkModeSwitch'

import LoginHomepage from '../components/homepage/LoginHomepage'

const Homepage = () => {
  const bg = useColorModeValue('gray.500', 'blue.600')
  const color = useColorModeValue('white', 'white.700')

  return (
    <>
      <ContainerFlex
        bg={bg}
        color={color}
        dir="row"
        justifyContent="space-between"
        paddingX="10"
        paddingY="5"
      >
        {/* Heading Component */}
        <Heading>Sicilia Naturalmente Admin</Heading>
        <DarkModeSwitch />
        {/* End Heading Component */}
      </ContainerFlex>
      <LoginHomepage />
    </>
  )
}

export default Homepage
