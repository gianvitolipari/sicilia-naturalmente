import React, { useEffect, useCallback, useRef, useState } from 'react'
import ContainerFlex from '../components/Utils/FlexComponent/containerHorizzontal'
import DarkModeSwitch from '../components/homepage/DarkModeSwitch'
import { Heading } from '@chakra-ui/layout'
import { useColorModeValue } from '@chakra-ui/color-mode'
import axios from '../axios'
import {
  Divider,
  Text,
  Button,
  List,
  ListIcon,
  ListItem,
  Accordion,
  AccordionItem,
  AccordionButton,
  AccordionPanel,
  AccordionIcon,
} from '@chakra-ui/react'
import { useLocation, useHistory } from 'react-router'
import OrderTableComponent from '../components/orderComponent/OrderTableComponent'
import {
  CheckCircleIcon,
  ChevronRightIcon,
  ChevronDownIcon,
} from '@chakra-ui/icons'
import CustomAlert from '../components/Utils/AlertComponent/Alert'

const UserInfoView = (props) => {
  const [loading, setLoading] = useState(false)

  const location = useLocation()
  const history = useLocation()

  // list of all the user orders
  const stateUserOrder = location.state?.user.ordini
  const user = location.state?.user

  //state for the info
  const [info, setInfo] = useState()

  //use color mode value are Chakra ui toogle theme for dark mode
  const bg = useColorModeValue('gray.500', 'blue.500')
  const color = useColorModeValue('white', 'white.700')

  console.log(info, 'qui')

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
      {info && <CustomAlert status="info" title="Success" descr={info} />}
      {/* start user component */}

      <Heading as="h4" size="md" m="10">
        Informazioni contatto {user.nome}
      </Heading>

      <Divider colorScheme="twitter" w="80%" m="10" marginTop="0" />
      <List spacing={10}>
        <ListItem
          alignItems="center"
          flexDirection="row"
          d="flex"
          marginLeft="40"
        >
          <ListIcon as={CheckCircleIcon} color="green.500" />
          <Heading marginLeft="2" as="h5" size="md" color={bg}>
            Indirizzo consegna scelto: {user.indirizzo}
          </Heading>
        </ListItem>

        <Accordion allowToggle height="xl">
          <AccordionItem height="100%">
            {({ isExpanded }) => (
              <>
                <AccordionButton>
                  <ListItem
                    alignItems="center"
                    flexDirection="row"
                    d="flex"
                    marginLeft="36"
                    marginBottom="8"
                  >
                    <ListIcon
                      as={isExpanded ? ChevronDownIcon : ChevronRightIcon}
                      color="green.500"
                    />
                    <Heading marginLeft="2" as="h5" size="md" color={bg}>
                      Ordini effettuati utente:
                    </Heading>
                  </ListItem>
                </AccordionButton>

                {isExpanded &&
                  (stateUserOrder.length ? (
                    <OrderTableComponent
                      order={stateUserOrder}
                      setInfo={setInfo}
                    />
                  ) : (
                    <Text m="10">
                      L'utente non ha ancora effettuato nessun ordine
                    </Text>
                  ))}
              </>
            )}
          </AccordionItem>
        </Accordion>
      </List>
    </>
  )
}

export default UserInfoView
