import React from 'react'
import { Input, InputGroup, InputLeftElement } from '@chakra-ui/react'
import { Search2Icon, ChevronDownIcon } from '@chakra-ui/icons'
import ContainerFlex from '../FlexComponent/containerHorizzontal'

const SearchComponent = () => {
  return (
    <ContainerFlex w="50%" margin="10">
      <InputGroup>
        <InputLeftElement
          pointerEvents="none"
          children={<Search2Icon color="gray.300" />}
        />
        <Input type="text" placeholder="Search for a user by email" />
      </InputGroup>
    </ContainerFlex>
  )
}

export default SearchComponent
