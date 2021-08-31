// * CART SCREEN
// ? what can you do
// * checkout method when user is authenticated
// * add and delete product from the cart

import React, { useEffect, useState } from 'react'
import { View, StyleSheet, SafeAreaView, Image, Alert } from 'react-native'
import { Header, Button, Overlay, Text } from 'react-native-elements'
import { FlatList } from 'react-native-gesture-handler'
import { AntDesign } from '@expo/vector-icons'
import * as cartActions from '../store/actions/cart'
import { useSelector, useDispatch } from 'react-redux'
import { StatusBar } from 'expo-status-bar'
import CustomModal from '../components/CustomModal'
import CartProduct from '../components/cartProduct'
import { images } from '../data/dummy-data'
import UserLoggedOption from '../components/userLoggedComponent/userLoggedOption'
import { MaterialCommunityIcons } from '@expo/vector-icons'
import { Divider } from 'react-native-elements/dist/divider/Divider'
import CheckoutModal from '../components/Utils/CheckoutModal'
import ErrorModal from '../components/authComponents/ErrorModal'

const createAlert = (navigation) =>
  Alert.alert('Checkout forbidden', 'Devi autenticarti per continuare!', [
    {
      text: 'OK',
      onPress: () => navigation.navigate('Account'),
      style: 'cancel',
    },
  ])

const CartScreen = ({ navigation }) => {
  /* start Stripe logic */

  const [paymentChoice, setPaymentChoice] = useState(false)
  const [checkoutModal, setCheckoutModal] = useState(false)

  //error state
  const error = useSelector((state) => state.error)

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
  const userRedux = useSelector((state) => state.user)
  const dispatch = useDispatch()

  console.log(userRedux, 'cart')

  const checkout = (method) => {
    if (!isAuth) {
      return createAlert(navigation)
    }

    if (method === 'carta') {
    }

    if (method === 'contanti') {
      // il cliente vuole pagare con i contanti
      dispatch(cartActions.checkout(cartItems, emailUser, cartTotalAmount))
    }
    setPaymentChoice(false)
  }

  return (
    <SafeAreaView style={styles.screen}>
      <Header
        backgroundImage={images.otherImages.cartBackgroundHeader}
        backgroundImageStyle={{ borderRadius: 30 }}
        statusBarProps={{ barStyle: 'light-content' }}
        backgroundColor="#F6F7FA"
        rightContainerStyle={{
          alignSelf: 'center',
        }}
        containerStyle={{
          height: 120,
        }}
      />
      {error.status && <ErrorModal payload={error} />}
      <FlatList
        style={styles.container}
        ListHeaderComponent={
          <Text
            style={{
              fontSize: 23,
              fontFamily: 'Inter_600SemiBold',
              color: '#094067',
              marginBottom: 20,
            }}
          >
            I tuoi ordini
          </Text>
        }
        data={cartItems}
        keyExtractor={(item) => item.productId}
        renderItem={(itemData) => (
          <CartProduct
            image={itemData.item.productImage}
            title={itemData.item.productTitle}
            quantity={itemData.item.quantity}
            product={itemData.item}
          />
        )}
        ListFooterComponent={
          <>
            <View style={styles.priceContainer}>
              {cartTotalAmount === 0 ? (
                <Text
                  style={{
                    fontFamily: 'Inter_400Regular',
                    textAlign: 'center',
                    color: '#5f6c7b',
                  }}
                >
                  Il tuo carrello è vuoto torna ad aggiungere dei prodotti!
                </Text>
              ) : (
                <Text style={styles.summaryText}>
                  Total: <Text style={styles.amount}>€{cartTotalAmount}</Text>
                </Text>
              )}
            </View>

            {/* button checkout   */}
            <View
              style={{
                marginTop: 30,
                width: '100%',
                overflow: 'hidden',
              }}
            >
              <Button
                onPress={() => {
                  if (!isAuth) {
                    return createAlert(navigation)
                  }
                  setPaymentChoice(true)
                }}
                title="acquista!"
                disabled={cartTotalAmount === 0}
                raised
                icon={<AntDesign name="checkcircleo" size={24} color="white" />}
                iconRight
                titleStyle={{
                  fontFamily: 'Inter_400Regular',
                }}
                containerStyle={{
                  width: '95%',
                  alignSelf: 'center',
                }}
                buttonStyle={{
                  backgroundColor: '#00214d',

                  justifyContent: 'space-between',

                  padding: 15,
                }}
              />
            </View>
          </>
        }
      />
      {/* Checkout Custom Modal */}
      <CheckoutModal
        modal={checkoutModal}
        hideModal={() => setCheckoutModal(false)}
        userRedux={userRedux}
        products={cartItems}
        totalAmount={cartTotalAmount}
      />

      <Overlay
        isVisible={paymentChoice}
        overlayStyle={{
          width: '80%',
          backgroundColor: '#fffffe',
          borderRadius: 5,
        }}
      >
        <Text
          h5
          style={{
            textAlign: 'center',
            marginBottom: 10,
            fontFamily: 'Inter_600SemiBold',
            color: '#00214d',
            fontSize: 15,
          }}
        >
          Scelgi il metodo di pagamento
        </Text>
        <Divider />
        <UserLoggedOption
          customStyle={{
            backgroundColor: '#d3d3d3',
            padding: 5,
            marginTop: 30,
            color: '#020826',
          }}
          customHandler={() => checkout('contanti')}
          customColor="#00214d"
          title="Paga in contanti"
          icon={
            <MaterialCommunityIcons
              name="cash-marker"
              size={24}
              color="#00214d"
            />
          }
        />
        <UserLoggedOption
          customStyle={{ backgroundColor: '#d3d3d3', padding: 5 }}
          customHandler={() => {
            setPaymentChoice(false)
            setCheckoutModal(true)
          }}
          customColor="#00214d"
          title="Paga con carta"
          icon={
            <MaterialCommunityIcons
              name="credit-card-check-outline"
              size={24}
              color="#00214d"
            />
          }
        />
        <UserLoggedOption
          customStyle={{ backgroundColor: '#d3d3d3', padding: 5 }}
          customHandler={() => {
            setPaymentChoice(false)
          }}
          customColor="#094067"
          title="Annulla"
          icon={
            <MaterialCommunityIcons
              name="window-close"
              size={24}
              color="#ef4565"
            />
          }
        />
      </Overlay>

      <CustomModal
        showModal={checkoutSuccess}
        text="Checkout Effettuato"
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
  container: {
    marginVertical: 20,
    width: '85%',
    alignSelf: 'center',
  },
  product: {
    flexDirection: 'row',
    width: '100%',

    marginTop: 15,
  },

  priceContainer: {
    alignItems: 'flex-end',
    marginBottom: 30,
  },
  bottomModal: {
    justifyContent: 'flex-end',
    margin: 0,
  },
  amount: {
    fontSize: 20,
    fontFamily: 'Inter_600SemiBold',
    color: '#094067',
  },
  summaryText: {
    marginTop: 10,
    marginRight: 10,
  },
  text: {
    textAlign: 'center',
    fontFamily: 'Inter_400Regular',
    marginVertical: 3,
  },
})

export default CartScreen
