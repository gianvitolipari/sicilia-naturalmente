//* component -> how a single cart Item renders in CartScreen.js

import React from 'react'
import { View, Text, StyleSheet, Image } from 'react-native'
import { AntDesign } from '@expo/vector-icons'

import axios from '../axios'
import { useDispatch } from 'react-redux'
import * as cartActions from '../store/actions/cart'

/* Style for the single cart Product   */

const CartProduct = (props) => {
  const dispatch = useDispatch()
  return (
    <View style={styles.screen}>
      <View style={styles.imageContainer}>
        <Image
          source={{ uri: props.image }}
          style={{
            width: '100%',
            height: '100%',
          }}
        />
      </View>
      <Text
        style={{
          fontSize: 16,
          fontFamily: 'Inter_600SemiBold',
          color: '#094067',
        }}
      >
        {props.title}
      </Text>
      <View style={styles.quantityContainer}>
        <AntDesign
          name="minus"
          size={20}
          color="white"
          style={styles.input}
          onPress={() => dispatch(cartActions.removeFromCart(props.product))}
        />
        <Text style={{ color: 'black' }}>{props.quantity}</Text>
        <AntDesign
          name="plus"
          size={20}
          color="white"
          style={styles.input}
          onPress={() => dispatch(cartActions.addOneToCart(props.product))}
        />
      </View>
    </View>
  )
}
const styles = StyleSheet.create({
  screen: {
    flex: 1,
    width: '90%',
    alignSelf: 'center',
    justifyContent: 'space-between',
    flexDirection: 'row',
    alignItems: 'center',
    padding: 10,
    marginVertical: 10,
    borderBottomColor: 'rgba(95, 108, 123, 0.4)',
    borderBottomWidth: 1,
  },
  imageContainer: {
    overflow: 'hidden',
    width: 50,
    height: 50,
    borderRadius: 25,
  },
  quantityContainer: {
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    width: '25%',
  },
  input: {
    paddingTop: 3.5,
    backgroundColor: 'rgba(239, 69, 101, 0.8)',
    borderRadius: 15,
    width: 30,
    height: 30,
    textAlign: 'center',
    marginHorizontal: 5,
  },
})

export default CartProduct
