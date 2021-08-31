import React from 'react'
import CustomInput from '../Utils/InputComponent/CustomInput'
import {
  FormControl,
  FormLabel,
  FormErrorMessage,
  Heading,
  Button,
  Flex,
  Text,
} from '@chakra-ui/react'
import { Formik, Field, Form } from 'formik'

import ContainerFlex from '../Utils/FlexComponent/containerHorizzontal'

const ChangeProductForm = (props) => {
  //handle for validation qty
  const validateQuantity = (value) => {
    let error
    console.log(value)
    if (value < 1 || value > 999) {
      error = 'So now it will be out of stock'
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

  return (
    <>
      <Heading mb="3" as="h4" size="md">
        {props.selcat.titolo}
      </Heading>

      <Formik
        initialValues={{
          quantity: props.selcat.quantita,
          prezzo: props.selcat.prezzo,
          //includere sconto ?
        }}
        onSubmit={(values, actions) => {
          props.handleAction(values, props.selcat.titolo)
        }}
      >
        {(props) => (
          <Form>
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

              <Field name="quantity" validate={validateQuantity}>
                {({ field, form }) => (
                  <FormControl
                    w="40%"
                    mb="10"
                    isInvalid={form.errors.quantity && form.touched.quantity}
                  >
                    <FormLabel htmlFor="quantita">Quantita</FormLabel>
                    <CustomInput {...field} type="number" m="1" />
                    <FormErrorMessage>{form.errors.quantity}</FormErrorMessage>
                  </FormControl>
                )}
              </Field>
            </ContainerFlex>
            <Button
              disabled={!props.isValid}
              mt={4}
              colorScheme="twitter"
              type="submit"
            >
              Update
            </Button>
          </Form>
        )}
      </Formik>
    </>
  )
}

export default ChangeProductForm
