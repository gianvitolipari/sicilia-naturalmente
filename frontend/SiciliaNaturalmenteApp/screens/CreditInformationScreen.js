// * CREDIT INFORMATION SCREEN
// ? what should it do
// * save billing and spedition information

import React, { useEffect, useState } from 'react'
import { Alert, StyleSheet, View } from 'react-native'
import { Input } from 'react-native-elements'
import { useStripe, CardField } from '@stripe/stripe-react-native'
import axios from '../axios'
import { Header, Text, Button } from 'react-native-elements'
import { AntDesign } from '@expo/vector-icons'
import * as userActions from '../store/actions/user'
import { useDispatch, useSelector } from 'react-redux'
import AsyncStorage from '@react-native-async-storage/async-storage'
import globalStyles from '../Style'
import CardComponent from '../components/Utils/CardComponent'

export default function PaymentsUICompleteScreen({ navigation, route }) {
  //retrieve address information with createPaymentMethod
  const { createPaymentMethod } = useStripe()

  // state of the payment methods
  const [paymentMethods, setPaymentMethods] = useState([])

  const customer = useSelector((state) => state.user.user)
  const paymentMethodSelected = useSelector((state) => state.user.paymentMethod)

  const dispatch = useDispatch()

  useEffect(() => {
    // first call the function to retrieve payment methods
    customer && retrievePaymentInfo()
    return () => {
      retrievePaymentInfo()
    }
  }, [paymentMethodSelect])

  // retrieve all the payment method of the customer
  const retrievePaymentInfo = async () => {
    try {
      const token = await AsyncStorage.getItem('token')
      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }

      const response = await axios.get(
        '/payment/customer_payment_methods',
        config,
      )
      const data = await response.data

      setPaymentMethods(data)
    } catch (e) {
      if (e.request.status) {
        dispatch(userActions.logout)
        navigation.navigate('AccountHome')
      }
    }
  }

  // save the paymentMethod selected
  const paymentMethodSelect = (pm) => {
    dispatch(userActions.savePaymentMethod(pm))
  }

  // to show all the available paymentMethod
  const paymentMethodsView = paymentMethods.map((pm) => (
    <CardComponent
      key={pm.paymentMethodId}
      last4={pm.last4}
      onSelect={() => paymentMethodSelect(pm)}
      isSelected={
        paymentMethodSelected.paymentMethodId === pm.paymentMethodId
          ? true
          : false
      }
    />
  ))

  const [card, setCard] = useState({
    complete: false,
  })

  //here I will take all customer's utilities

  const createPaymentMeth = async () => {
    //first create payment-method attach this to a customer
    try {
      // Create payment method
      const { paymentMethod, error } = await createPaymentMethod({
        type: 'Card',

        billingDetails: {
          email: customer.email,
          addressLine1: customer.indirizzo,
        },
      })

      if (error) {
        console.log('Payment Method error', error)
      }

      const token = await AsyncStorage.getItem('token')
      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
          'Content-Type': 'application/json',
        },
      }

      console.log(token, ' qui')

      //attach the paymentMethod to the customer
      const result = await axios.post(
        'payment/customer',
        paymentMethod.id,
        config,
      )
      if (result) {
        retrievePaymentInfo()
        dispatch(userActions.savePaymentMethod(paymentMethod.id))
      }
    } catch (e) {
      console.log(e, 'In credit')
    }
  }

  return (
    <View style={globalStyles.container}>
      <Header
        statusBarProps={{ barStyle: 'light-content' }}
        centerComponent={
          <Text style={globalStyles.header_text}>
            INFORMAZIONI {customer?.nome.toUpperCase()}
          </Text>
        }
        leftComponent={
          <AntDesign
            name="arrowleft"
            size={30}
            color="white"
            onPress={() => navigation.popToTop()}
          />
        }
        backgroundColor="#00214d"
        containerStyle={{
          height: 120,
          borderRadius: 30,
          alignItems: 'center',
          marginBottom: 10,
        }}
      />
      {/* if shipping & billing properties exist show the form  */}

      <View style={styles.container}>
        <Text
          style={{
            fontFamily: 'Inter_600SemiBold',
            color: '#00214d',
            marginBottom: 5,
          }}
        >
          INFORMAZIONI SU PAGAMENTO
        </Text>
        {paymentMethodsView}
        <Text
          style={{
            fontFamily: 'Inter_400Regular',
            color: '#00214d',
            marginBottom: 5,
            marginTop: 20,
          }}
        >
          Aggiungi nuovo metodo (max. 5 )
        </Text>
        <CardField
          postalCodeEnabled={false}
          placeholder={{
            number: '4242 4242 4242 4242',
          }}
          cardStyle={{
            backgroundColor: '#FFFFFF',
            textColor: '#000000',
          }}
          style={{
            width: '100%',
            height: 50,
            marginVertical: 10,
          }}
          onCardChange={(cardDetails) => {
            setCard(cardDetails)
          }}
          onFocus={(focusedField) => {
            console.log('focusField', focusedField)
          }}
        />
        <Button
          title="Aggiungi"
          onPress={createPaymentMeth}
          disabled={!card.complete}
          buttonStyle={{ backgroundColor: '#00214d' }}
          raised={true}
        />
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
  },
  container: {
    alignSelf: 'center',
    width: '90%',
    marginTop: 20,
  },
  formContainer: {
    marginTop: 10,
  },
})
