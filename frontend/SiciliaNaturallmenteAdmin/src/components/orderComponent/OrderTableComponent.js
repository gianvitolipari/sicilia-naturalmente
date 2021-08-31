import React, { useEffect, useRef, useState, useCallback } from 'react'
import CustomTable from '../Utils/TableComponent/CustomTable'
import axios from '../../axios'
import {
  Td,
  Tr,
  Box,
  Divider,
  Text,
  Select,
  Flex,
  Tooltip,
  IconButton,
  List,
  ListItem,
  ListIcon,
} from '@chakra-ui/react'
import {
  ViewIcon,
  CheckCircleIcon,
  EditIcon,
  ChevronRightIcon,
} from '@chakra-ui/icons'
import CustomModal from '../Utils/CustomModal/CustomModal'
import { useColorModeValue } from '@chakra-ui/color-mode'
import ContainerFlex from '../Utils/FlexComponent/containerHorizzontal'

const OrderTableComponent = (props, { history }) => {
  // useRef for rendering optimization with useCallback
  const renderCall = useRef(true)

  const [contenuto, setContenuto] = useState([])

  //use color mode value are Chakra ui toogle theme for dark mode
  const bgOnCourse = useColorModeValue('gray.500', 'yellow.700')
  const bgDone = useColorModeValue('green.300', 'blue.800')
  const colorDone = useColorModeValue('white.300', 'blue.800')

  const [orderList, setOrderList] = useState([])
  const [isLoading, setLoading] = useState(false)

  useEffect(() => {
    populateOrder()

    return () => {
      renderCall.current = false
    }
  }, [])

  // set the content state
  const handleClick = async (idOrder) => {
    try {
      const token = await localStorage.getItem('token')

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }

      const order = await axios.get(`users/order/content/${idOrder}`, config)
      const data = order.data
      setContenuto(data)
    } catch (e) {
      console.log(e, ' in fetch contenuto')
      console.log(e.response.data)
    }
  }

  // handle change order state and payment state
  const handleChangeStatus = async (idOrder, status) => {
    try {
      const token = await localStorage.getItem('token')

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
        params: {
          idOrdine: idOrder,
          statoPagamento: status, //CONSEGNATO
        },
      }

      const order = await axios.post(
        'users/order/status',
        null,

        config,
      )
      props.setInfo(order.data)
      setTimeout(() => props.setInfo(), 3000)
    } catch (e) {
      console.log(e, 'in order')
    }
  }

  //populate the list according to the table
  const populateOrder = useCallback(() => {
    const list = props.order.map((order) => (
      <Tr key={order.idOrdine}>
        <Td>{order.idOrdine}</Td>
        <Td>{order.data}</Td>
        <Td textAlign={['center', 'initial']}>
          {order.stato === 'IN_PREPARAZIONE' ? ( //in preparazione, spedito, consegnato
            <Select
              placeholder="In corso"
              bg={bgOnCourse}
              onClick={(e) =>
                handleChangeStatus(order.idOrdine, e.target.value)
              }
            >
              <option value="IN_PREPARAZIONE">In preparazione</option>
              <option value="SPEDITO">Spedito</option>
              <option value="CONSEGNATO">Consengato</option>
            </Select>
          ) : order.stato === 'SPEDITO' ? (
            <Select
              placeholder="Spedito"
              bg={bgOnCourse}
              onClick={(e) =>
                handleChangeStatus(order.idOrdine, e.target.value)
              }
            >
              <option value="IN_PREPARAZIONE">In preparazione</option>
              <option value="SPEDITO">Spedito</option>
              <option value="CONSEGNATO">Consengato</option>
            </Select>
          ) : (
            <Box p="2" backgroundColor={bgDone}>
              Consegnato
            </Box>
          )}
        </Td>

        <Td
          onClick={() => handleClick(order.idOrdine)}
          textAlign={['center', 'initial']}
        >
          <Tooltip
            colorScheme="twitter"
            aria-label="add tooltip"
            hasArrow
            label="Visualizza contenuto"
          >
            <IconButton
              variant="outline"
              colorScheme="twitter"
              aria-label="delelte product"
              icon={<ViewIcon w={5} h={5} color={bgDone} />}
            />
          </Tooltip>
        </Td>
      </Tr>
    ))
    setLoading('true')
    setOrderList(list)
  }, [])

  return (
    <>
      <Flex width="80%" alignSelf="center" marginLeft="30" flexDir="column">
        {contenuto.length > 0 && <Text>Contenuto Ordine: </Text>}
        {contenuto.map((cont) => (
          <List
            key={cont.titolo}
            flexDir="row"
            borderRadius={10}
            alignItems="center"
            justifyContent="center"
            marginY="2"
          >
            <ListItem>
              <ListIcon as={ChevronRightIcon} color="green.500" />
              {cont.titolo}
            </ListItem>
            <ListItem>{cont.quantita}</ListItem>
            <Divider />
          </List>
        ))}
      </Flex>

      <CustomTable
        w="90%"
        marginX="10"
        keys={['idOrdine', 'Data', 'Stato', 'Info']}
        values={orderList}
        title="List all Orders"
        loading={isLoading ? 'false' : undefined}
      >
        {orderList}
      </CustomTable>
      <Divider />
    </>
  )
}

export default OrderTableComponent
