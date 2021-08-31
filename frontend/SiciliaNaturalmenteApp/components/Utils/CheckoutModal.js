import React, { useState } from 'react'
import { StyleSheet, Text, View } from 'react-native'
import { Divider, Button } from 'react-native-elements'
import Modal from 'react-native-modal'
import * as cartActions from '../../store/actions/cart'
import { useDispatch, useSelector } from 'react-redux'

const CheckoutModal = (props, { navigation }) => {
  const dispatch = useDispatch()

  const [loading, setLoading] = useState(false)

  let newProducts = []
  props.products.map((pr) => {
    newProducts.push({
      titolo: pr['productTitle'],
      quantita: pr['quantity'],
    })
  })

  const handleCheckout = async () => {
    setLoading(true)
    await dispatch(
      cartActions.checkout(
        newProducts,
        props.totalAmount,
        props.userRedux.paymentMethod.paymentMethodId,
      ),
    )
    setLoading(false)
    props.hideModal()
  }

  return (
    <Modal
      isVisible={props.modal}
      style={styles.bottomModal}
      swipeDirection={['up', 'left', 'right', 'down']}
      animationInTiming={400}
      backdropColor="white"
      backdropOpacity={0}
    >
      <View style={{ backgroundColor: '#0D1117' }}>
        <View style={{ width: '80%', alignSelf: 'center' }}>
          <Text
            style={{
              color: '#C9D1D9',
              fontFamily: 'Inter_600SemiBold',
              marginVertical: 20,
              fontSize: 18,
            }}
          >
            Riepilogo checkout
          </Text>
          <View
            style={{
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'space-between',
            }}
          >
            <Text
              style={{
                color: '#C9D1D9',
                fontFamily: 'Inter_400Regular',
                marginVertical: 20,
              }}
            >
              Carta scelta: 4242
            </Text>
            <Button
              title="cambia carta"
              onPress={() => navigation.navigate('BillingDetailsScreen')}
              containerStyle={{ borderRadius: 5 }}
            ></Button>
          </View>

          <View
            style={{
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'space-between',
            }}
          >
            <Text
              style={{
                color: '#C9D1D9',
                fontFamily: 'Inter_400Regular',
                marginVertical: 20,
              }}
            >
              Indirizzo: {props.userRedux?.user.indirizzo}
            </Text>
          </View>
          <Divider />
          <Button
            title="Procedi all'acquisto"
            containerStyle={{ marginTop: 30 }}
            onPress={handleCheckout}
            loading={loading}
          />
          <Button
            title="Annulla"
            containerStyle={{ marginVertical: 30 }}
            onPress={props.hideModal}
          />
        </View>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  bottomModal: {
    justifyContent: 'flex-end',
    margin: 0,
  },
})

export default CheckoutModal
