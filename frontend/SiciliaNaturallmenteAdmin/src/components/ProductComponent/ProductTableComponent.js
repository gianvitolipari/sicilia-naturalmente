import React, { useEffect, useRef, useState, useCallback } from 'react'
import CustomTable from '../Utils/TableComponent/CustomTable'
import axios from '../../axios'
import {
  Td,
  Tr,
  Box,
  Tooltip,
  IconButton,
  Image,
  Text,
  Button,
  Flex,
} from '@chakra-ui/react'

import NewProductForm from './NewProductForm'
import CustomModal from '../Utils/CustomModal/CustomModal'
import { useDisclosure } from '@chakra-ui/hooks'
import { DeleteIcon, SettingsIcon, AddIcon } from '@chakra-ui/icons'
import ChangeProductForm from './ChangeProductForm'

const ProductTableComponent = ({ setError, setInfo }) => {
  // useRef for rendering optimization with useCallback
  const renderCall = useRef(true)

  const [productsList, setProductsList] = useState(null)
  const [isLoading, setLoading] = useState(false)
  const [products, setProducts] = useState([])

  const [selectedCategory, setSelectedCategory] = useState(null)

  // declare state for the modals
  const { isOpen, onOpen, onClose } = useDisclosure()

  const fetchProd = async () => {
    try {
      const data = await axios.get('/product/')

      populateProduct(data)
      setLoading('true')
    } catch (e) {
      console.log(e)
      setLoading('true')
    }
  }

  //handle for delete product
  const deleteProduct = async (titolo) => {
    try {
      const token = localStorage.getItem('token')
      console.log(titolo)
      const response = await axios.delete(`product/delete/${titolo}`, {
        headers: {
          Authorization: 'Bearer ' + token,
          'Content-Type': 'application/json',
        },
      })
      setInfo(response.data)
      setTimeout(() => setInfo(), 3000)
      fetchProd()
    } catch (e) {
      setError(e.response.data.message)
      setTimeout(() => setError(), 3000)
    }
  }

  //hanlde change product
  const handleChangeProduct = async (values, titolo) => {
    try {
      const token = localStorage.getItem('token')
      console.log(values, titolo, ' ok')
      const data = await axios.post('/product/editProduct', null, {
        headers: {
          Authorization: 'Bearer ' + token,
        },
        params: {
          titolo: titolo,
          prezzo: values.prezzo,
          quantita: values.quantity,
        },
      })
      setInfo(data.data)
      setTimeout(() => setInfo(), 3000)
      fetchProd()
    } catch (e) {
      setError(e.response.data.message)
      setTimeout(() => setError(), 3000)
    }
  }

  //fetch all the products
  useEffect(() => {
    renderCall.current && fetchProd()

    return () => {
      renderCall.current = false
    }
  }, [renderCall])

  // pass to the modal the Setting product selected
  const modalSelected = (data) => {
    setSelectedCategory(data)
    onOpen()
  }

  //populate the list according to the table
  const populateProduct = useCallback((data) => {
    console.log(data?.data)

    const list = data?.data?.map((product) => (
      <Tr key={product.idProdotto}>
        <Td>
          <Image
            borderRadius="full"
            boxSize="50px"
            src={product.immagine}
            alt={product.titolo}
          />
        </Td>
        <Td>{product.titolo}</Td>
        <Td textAlign={['center', 'initial']}>
          {product.quantita ? (
            <Box p="2" bg="blue.500" textAlign="center" w="10">
              {product.quantita}
            </Box>
          ) : (
            <Box p="2" backgroundColor="red.500">
              Esaurito
            </Box>
          )}
        </Td>
        <Td>
          <Tooltip
            colorScheme="twitter"
            aria-label="settings tooltip"
            hasArrow
            label="change product"
          >
            <IconButton
              variant="outline"
              colorScheme="twitter"
              aria-label="see info"
              onClick={() => modalSelected(product)}
              icon={<SettingsIcon />}
              mr="6"
            />
          </Tooltip>

          <CustomModal
            isCentered
            header={'Delete product'}
            icon={<DeleteIcon />}
            labeltool="delete product"
            blockScrollOnMount={false}
            size="xl"
          >
            <Flex alignItems="center" justifyContent="space-between">
              <Text mr="3">
                Are you sure you want to delete {product.titolo} ?
              </Text>
              <Button
                colorScheme="twitter"
                onClick={() => deleteProduct(product.titolo)}
              >
                Delete
              </Button>
            </Flex>
          </CustomModal>
        </Td>
      </Tr>
    ))

    setProductsList(list)

    //populate the product form

    const productsObj = []

    data?.data.map((product) => {
      productsObj.push({
        titolo: product.titolo,
        quantita: product.quantita,
      })
    })

    setProducts(productsObj)
  }, [])

  //product/editproduct post

  return (
    <>
      <CustomTable
        w="80%"
        marginX="10"
        keys={['#', 'Title', 'Quantity', 'modify']}
        values={productsList}
        title="List all products"
        loading={isLoading ? 'false' : undefined}
        selcat={selectedCategory}
        open={isOpen}
        onClose={onClose}
        ismodal="true"
        modalbody={
          <ChangeProductForm
            selcat={selectedCategory}
            handleAction={(values, titolo) =>
              handleChangeProduct(values, titolo)
            }
          />
        }
        foot={
          <Tr>
            <Td>Aggiungi</Td>
            <Td></Td>
            <Td></Td>
            <Td>
              <CustomModal
                blockScrollOnMount={false}
                header={'Add new product'}
                icon={<AddIcon />}
                labeltool="Add product"
                size="xl"
              >
                <NewProductForm
                  setError={setError}
                  setInfo={setInfo}
                  fetch={fetchProd}
                  products={products}
                />
              </CustomModal>
            </Td>
          </Tr>
        }
      >
        {productsList}
      </CustomTable>
    </>
  )
}

export default ProductTableComponent
