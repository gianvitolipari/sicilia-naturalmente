import React, { useState } from 'react'
import CustomInput from '../Utils/InputComponent/CustomInput'
import {
  FormControl,
  FormLabel,
  FormErrorMessage,
  FormHelperText,
  Button,
  Flex,
  Text,
  Select,
  Divider,
} from '@chakra-ui/react'
import axios from '../../axios'
import { Formik, Field, Form } from 'formik'
import ContainerFlex from '../Utils/FlexComponent/containerHorizzontal'
import { IconButton } from '@chakra-ui/button'
import { MinusIcon, AddIcon } from '@chakra-ui/icons'

const NewProductForm = ({ setInfo, setError, fetch, products }) => {
  // custom formik image value
  const [image64, setImage64] = useState('')

  //loading props
  const [loading, setLoading] = useState(false)

  //contenuto props with initial status
  const [content, setContent] = useState(
    products.map((product) => ({
      titolo: product.titolo,
      quantita: 0,
    })),
  )

  //validate file
  const validateFiles = (value) => {
    let error
    if (value.length < 1) {
      error = 'File is required'
      return error
    }
    for (const file of Array.from(value)) {
      const fsMb = file.size / (1024 * 1024)
      const MAX_FILE_SIZE = 10
      if (fsMb > MAX_FILE_SIZE) error = 'Max file size 10mb'
      return error
    }
    return error
  }

  //handle for validation qty
  const validateQuantity = (value) => {
    let error
    if (value < 1 || value > 999) {
      error = 'Field required'
    }
    return error
  }

  //handle for validation
  const validatePrice = (value) => {
    let error
    if (value < 1 || value > 200) {
      error = 'just no'
    }
    return error
  }

  const uploadImage = async (e) => {
    const file = e.target.files[0]

    const base64 = await convertBase64(file)
    setImage64(base64)
  }

  //convert the file in base64
  const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader()
      fileReader.readAsDataURL(file)

      fileReader.onload = () => {
        resolve(fileReader.result)
      }

      fileReader.onerror = (error) => {
        reject(error)
      }
    })
  }

  //handle scatole form action
  const handleButtonScatole = (type, prodotto) => {
    let newProducts = [...content]

    type === 'meno'
      ? (newProducts[prodotto].quantita = content[prodotto].quantita - 1)
      : (newProducts[prodotto].quantita = content[prodotto].quantita + 1)

    setContent(newProducts)
  }

  //render scatole form
  const productsForm = products.map((product, pos) => (
    <Flex flexDir="row" key={product.titolo} alignItems="center" mb="5">
      <Text width="72">{product.titolo}</Text>
      <IconButton
        variant="outline"
        colorScheme="twitter"
        aria-label="delete product"
        icon={<MinusIcon />}
        disabled={content[pos]?.quantita < 1}
        onClick={() => handleButtonScatole('meno', pos)}
        mr="5"
      />
      <Text>{content[pos]?.quantita}</Text>
      <IconButton
        variant="outline"
        colorScheme="twitter"
        aria-label="add product"
        icon={<AddIcon />}
        ml="5"
        disabled={content[pos]?.quantita > product.quantita - 1}
        onClick={() => handleButtonScatole('più', pos)}
      />
      {content[pos]?.quantita > product.quantita - 1 && (
        <Text fontSize="small" ml="5">
          Limite Raggiunto
        </Text>
      )}
    </Flex>
  ))

  const submitForm = async (values, image, category) => {
    setLoading(true)
    const newContent = content.filter((product) => product.quantita != 0)
    console.log(JSON.stringify(newContent), ' ok')
    try {
      const token = localStorage.getItem('token')
      console.log(values)

      if (category === 'scatole') {
        values.formato = 'SCATOLA'
      } else if (category === 'prodotti') {
        values.formato = 'PRODOTTO'
      }

      const response = await axios.post(
        '/product/create',
        {
          ...values,
          immagine: image,
        },
        {
          headers: {
            Authorization: 'Bearer ' + token,
          },
        },
      )
      console.log('Lol')

      setInfo(response.data)
      setTimeout(() => setInfo(''), 3000)
      setLoading(false)
      fetch()
    } catch (e) {
      console.log(e, ' in submit form')
      setError(e?.response?.data.message)
      setTimeout(() => setError(''), 3000)
      setLoading(false)
    }
  }

  return (
    <Formik
      initialValues={{
        titolo: '',
        descrizione: '',
        prezzo: '',
        quantita: '',

        immagine: '',
        categoria: '',
        immagineRetro: '',
        //props for the pasta
        formato: 'CORTA',
        minutiPreparazione: '',
        grano: '',
        //props for the box
        contenuto: [],

        //includere sconto ?
      }}
      onSubmit={(values, actions) => {
        const { categoria, ...value } = values
        submitForm(value, image64, categoria)
      }}
    >
      {(props) => (
        <Form>
          <Divider mb="5" />

          <Field name="titolo" validate={validateQuantity}>
            {({ field, form }) => (
              <FormControl
                mb="10"
                isInvalid={form.errors.titolo && form.touched.titolo}
              >
                <FormLabel htmlFor="title">Titolo</FormLabel>
                <CustomInput {...field} type="text" m="1" />
                <FormErrorMessage>{form.errors.titolo}</FormErrorMessage>
              </FormControl>
            )}
          </Field>
          <Field name="descrizione" validate={validateQuantity}>
            {({ field, form }) => (
              <FormControl
                mb="10"
                isInvalid={form.errors.descrizione && form.touched.descrizione}
              >
                <FormLabel htmlFor="description">Descrizione</FormLabel>
                <CustomInput {...field} type="text" mb="1" />
                <FormErrorMessage>{form.errors.descrizione}</FormErrorMessage>
              </FormControl>
            )}
          </Field>
          <ContainerFlex justifyContent="space-between">
            <Field name="prezzo" validate={validatePrice}>
              {({ field, form }) => (
                <FormControl
                  isInvalid={form.errors.prezzo && form.touched.prezzo}
                  mb="10"
                  w="30%"
                >
                  <FormLabel htmlFor="name">Prezzo</FormLabel>
                  <Flex alignItems="center">
                    <CustomInput {...field} type="number" mb="1" />
                    <Text ml="3">€</Text>
                  </Flex>
                  <FormErrorMessage>{form.errors.prezzo}</FormErrorMessage>
                </FormControl>
              )}
            </Field>

            <Field name="quantita" validate={validateQuantity}>
              {({ field, form }) => (
                <FormControl
                  w="40%"
                  mb="10"
                  isInvalid={form.errors.quantita && form.touched.quantita}
                >
                  <FormLabel htmlFor="quantita">Quantità</FormLabel>
                  <CustomInput {...field} type="number" m="1" />
                  <FormErrorMessage>{form.errors.quantita}</FormErrorMessage>
                </FormControl>
              )}
            </Field>
          </ContainerFlex>
          {/* Field for the image */}
          <Field name="immagine" validate={validateFiles}>
            {({ field, form }) => (
              <FormControl
                onChange={uploadImage}
                mb="10"
                isInvalid={form.errors.immagine && form.touched.immagine}
              >
                <FormLabel htmlFor="image">Immagine</FormLabel>
                <CustomInput
                  {...field}
                  type="file"
                  marginY="3"
                  ml="-4"
                  border="none"
                />
                <FormErrorMessage>{form.errors.immagine}</FormErrorMessage>
              </FormControl>
            )}
          </Field>
          <Field name="categoria" validate={validateQuantity}>
            {({ field, form }) => (
              <FormControl
                mb="10"
                isInvalid={form.errors.categoria && form.touched.categoria}
              >
                <FormLabel htmlFor="categoria">Seleziona categories</FormLabel>
                <Select {...field} placeholder="Select option">
                  <option value="pasta">Pasta</option>
                  <option value="prodotti">Prodotti</option>
                  <option value="scatole">Scatole</option>
                </Select>
                <FormErrorMessage>{form.errors.categoria}</FormErrorMessage>
              </FormControl>
            )}
          </Field>

          {/* depends on the select input decide what to render  */}

          {props.values.categoria === 'pasta' && (
            <>
              <ContainerFlex justifyContent="space-between">
                <Field name="formato">
                  {({ field, form }) => (
                    <FormControl mb="10" w="50%">
                      <FormLabel htmlFor="formato">Seleziona formato</FormLabel>
                      <Select {...field} placeholder="Select">
                        <option value="0">Corta</option>
                        <option value="1">Media</option>
                        <option value="2">Lunga</option>
                      </Select>
                      <FormErrorMessage>{form.errors.formato}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
              </ContainerFlex>
              <ContainerFlex justifyContent="space-between">
                <Field name="grano">
                  {({ field, form }) => (
                    <FormControl mb="10" w="30%">
                      <FormLabel htmlFor="name">Grano</FormLabel>

                      <CustomInput {...field} type="text" mb="1" />

                      <FormErrorMessage>{form.errors.grano}</FormErrorMessage>
                    </FormControl>
                  )}
                </Field>

                <Field name="minutiPreparazione" validate={validateQuantity}>
                  {({ field, form }) => (
                    <FormControl w="40%" mb="10">
                      <FormLabel htmlFor="minutiPreparazione">
                        minuti Preparazione
                      </FormLabel>
                      <Flex alignItems="center">
                        <CustomInput {...field} type="text" mb="1" />
                        <Text ml="3">min</Text>
                      </Flex>
                      <FormErrorMessage>
                        {form.errors.minutiPreparazione}
                      </FormErrorMessage>
                    </FormControl>
                  )}
                </Field>
              </ContainerFlex>
            </>
          )}
          {props.values.categoria === 'scatole' && (
            <Flex flexDir="column" flex="1" width="100%">
              <Text textAlign="center" mb="5">
                Aggiungi i prodotti alla scatola
              </Text>
              {productsForm}
            </Flex>
          )}
          <Button
            onClick={props.handleSubmit}
            disabled={!props.isValid}
            mt={4}
            colorScheme="twitter"
            isLoading={loading}
            type="submit"
          >
            Add new product
          </Button>
        </Form>
      )}
    </Formik>
  )
}

export default NewProductForm
