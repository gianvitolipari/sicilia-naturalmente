import { useColorMode, IconButton } from '@chakra-ui/react'
import { SunIcon, MoonIcon } from '@chakra-ui/icons'

import React from 'react'

function DarkModeSwitch() {
  const { colorMode, toggleColorMode } = useColorMode()
  const iconColor = {
    light: 'gray.500',
    dark: 'blue.600',
  }
  const iconBgColor = {
    light: 'white',
    dark: 'gray.700',
  }

  return (
    <IconButton
      aria-label="Toogle dark mode"
      icon={colorMode === 'dark' ? <SunIcon /> : <MoonIcon />}
      onClick={toggleColorMode}
      color={iconColor[colorMode]}
      bg={iconBgColor[colorMode]}
    />
  )
}

export default DarkModeSwitch
