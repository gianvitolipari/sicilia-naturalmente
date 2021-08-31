import React from 'react'
import Svg, { Path } from 'react-native-svg'

const CircleIcon = () => {
  return (
    <Svg
      width="20"
      height="20"
      viewBox="0 0 94 94" //0 0 94 94
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <Path
        fill="#EFC75E"
        d="M47,94C21.084,94,0,72.916,0,47S21.084,0,47,0s47,21.084,47,47S72.916,94,47,94z M47,12.186
			c-19.196,0-34.814,15.618-34.814,34.814c0,19.195,15.618,34.814,34.814,34.814c19.195,0,34.814-15.619,34.814-34.814
			C81.814,27.804,66.195,12.186,47,12.186z"
      />
    </Svg>
  )
}

export default CircleIcon
