import React, { useState } from 'react'
import { StyleSheet, SafeAreaView, Alert } from 'react-native'
import { Header, Button } from 'react-native-elements'
import * as cartActions from '../store/actions/cart'
//import * as RootNavigation from '../Navigation'
import { useSelector, useDispatch } from 'react-redux'
import { StatusBar } from 'expo-status-bar'
import CustomModal from '../components/CustomModal'
import CartProduct from '../components/cartProduct'

const createAlert = (navigation) =>
  Alert.alert('Checkout forbidden', 'Devi autenticarti per continuare!', [
    {
      text: 'OK',
      onPress: () => navigation.navigate('Account'),
      style: 'cancel',
    },
  ])

const CartScreen = ({ navigation }) => {
  const cartItems = useSelector((state) => {
    const transformedCartItems = []
    for (const key in state.cart.items) {
      transformedCartItems.push({
        productId: key,
        productTitle: state.cart.items[key].productTitle,
        productPrice: state.cart.items[key].productPrice,
        productImage: state.cart.items[key].productImage,

        quantity: state.cart.items[key].quantity,
      })
    }
    return transformedCartItems
  })
  const cartTotalAmount = useSelector((state) => state.cart.totalAmount)
  const isAuth = useSelector((state) => state.user.isAuthenticated)
  const checkoutSuccess = useSelector((state) => state.cart.checkoutSuccess)
  const emailUser = useSelector((state) => state.user)
  const dispatch = useDispatch()

  const checkout = () => {
    if (!isAuth) {
      return createAlert(navigation)
    }

    dispatch(cartActions.checkout(cartItems, emailUser, cartTotalAmount))
  }

  return (
    <SafeAreaView style={styles.screen}>
      <Header
        statusBarProps={{ barStyle: 'light-content' }}
        backgroundColor="#F6F7FA"
        rightContainerStyle={{
          alignSelf: 'center',
        }}
        containerStyle={{
          height: 120,
        }}
      />

      <CartProduct
        image={cartItems[0].productImage}
        title={cartItems[0].productTitle}
        quantity={cartItems[0].quantity}
        product={cartItems[0]}
      />

      <CustomModal
        showModal={checkoutSuccess}
        text="Checkout Effettuato!"
        theme="rgba(66, 135, 245, 1)"
      />
      <StatusBar style="light" />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#F6F7FA',
  },
})

export default CartScreen
