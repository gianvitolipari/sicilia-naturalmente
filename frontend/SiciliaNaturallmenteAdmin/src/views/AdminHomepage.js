/*
 *   Admin page when the signin is successfull
 */

import React, { useState } from 'react'
import ContainerFlex from '../components/Utils/FlexComponent/containerHorizzontal'
import { Heading, Text, Divider } from '@chakra-ui/layout'
import DarkModeSwitch from '../components/homepage/DarkModeSwitch'
import { useColorModeValue } from '@chakra-ui/color-mode'
import { Button } from '@chakra-ui/button'
import ProductTableComponent from '../components/ProductComponent/ProductTableComponent'
import SearchComponent from '../components/Utils/InputComponent/SearchComponent'
import UsersTableComponent from '../components/UserComponents/UsersTableComponent'
import CustomAlert from '../components/Utils/AlertComponent/Alert'

const AdminHomepage = ({ history }) => {
  //use color mode value are Chakra ui toogle theme for dark mode
  const bg = useColorModeValue('gray.500', 'blue.600')
  const color = useColorModeValue('white', 'white.700')

  const [selectedCategory, setSelectedCategory] = useState(true)

  //state for the error
  const [error, setError] = useState()

  //state for the info
  const [info, setInfo] = useState()

  return (
    <>
      <ContainerFlex
        bg={bg}
        color={color}
        justifyContent="space-between"
        paddingX="10"
        paddingY="5"
      >
        {/* Heading Component */}
        <Heading as="h5" size="lg" isTruncated>
          Sicilia Naturalmente Admin
        </Heading>
        <ContainerFlex justifyContent="space-between" alignItems="center">
          <Button
            colorScheme="twitter"
            mr="5"
            onClick={() => history.push('/')}
          >
            Logout
          </Button>
          <DarkModeSwitch />
        </ContainerFlex>
        {/* End Heading Component */}
      </ContainerFlex>
      {/* render conditionally the customAlert */}
      {error && (
        <CustomAlert
          status="error"
          title="Something went wrong"
          descr={error}
        />
      )}
      {info && <CustomAlert status="info" title="Success" descr={info} />}
      <Heading as="h1" size="2xl" isTruncated color="blue.400" margin="10">
        Benvenuto Admin
      </Heading>
      <Divider />
      <ContainerFlex
        borderWidth="30"
        marginY="10"
        justifyContent="flex-start"
        spacing="30px"
        paddingX="10"
        jf="flex-start"
      >
        <Text
          borderRadius="0"
          padding="2"
          onClick={() => setSelectedCategory(true)}
          backgroundColor={selectedCategory ? 'blue.500' : 'gray.400'}
          color="white"
        >
          Gestisci utenti
        </Text>
        <Text
          color="white"
          borderRadius="0"
          padding="2"
          onClick={() => setSelectedCategory(false)}
          background={!selectedCategory ? 'blue.500' : 'gray.400'}
        >
          Gestisci prodotti
        </Text>
      </ContainerFlex>
      {/* render conditionally the category selected */}

      {selectedCategory ? (
        <UsersTableComponent setError={setError} setInfo={setInfo} />
      ) : (
        <ProductTableComponent setError={setError} setInfo={setInfo} />
      )}
    </>
  )
}

export default AdminHomepage
