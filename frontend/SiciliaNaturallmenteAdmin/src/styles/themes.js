import { extendTheme } from '@chakra-ui/react'
import { theme as chakraTheme } from '@chakra-ui/react'
import { createBreakpoints } from '@chakra-ui/theme-tools'

const fonts = {
  ...chakraTheme.fonts,
  body: 'Gordita Regular',
  heading: 'Gordita Regular',
}

const breakpoints = createBreakpoints({
  sm: '40em',
  md: '52em',
  lg: '62em',
})

const overrides = {
  ...chakraTheme,
  fonts,
  breakpoints,
  fontWeights: {
    normal: 300,
    medium: 600,
    bold: 700,
  },
  fontSizes: {
    xs: '12px',
    sm: '14px',
    md: '16px',
    lg: '18px',
    xl: '20px',
    '2xl': '24px',
    '3xl': '28px',
    '4xl': '36px',
  },
}

const customTheme = extendTheme(overrides)

export default customTheme
