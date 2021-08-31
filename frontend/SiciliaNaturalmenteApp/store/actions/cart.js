import {
  ADD_ONE_TO_CART,
  REMOVE_FROM_CART,
  ADD_TO_CART,
  CHECKOUT_FINISHED,
  IS_LOADING,
  CLEAR_CART_AFTER_CHECKOUT,
  UPDATE_PAST_ORDERS,
} from './types'
import { returnErrors, clearErrors } from './error'

import axios from '../../axios'
import AsyncStorage from '@react-native-async-storage/async-storage'

export const addToCart = (product, quantity) => async (dispatch) => {
  return dispatch({
    type: ADD_TO_CART,
    product: product,
    quantity: quantity,
  })
}

export const removeFromCart = (product) => {
  return { type: REMOVE_FROM_CART, product: product }
}
export const addOneToCart = (product) => {
  return { type: ADD_ONE_TO_CART, product: product }
}

// called when the checkout is called
export const checkout = (products, totalAmount, user) => async (dispatch) => {
  try {
    const token = await AsyncStorage.getItem('token')

    const body = {
      paymentMethod: user,
      price: totalAmount * 100,
      products: products,
    }

    let config = {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    }

    const response = await axios.post('payment/payment_intents', body, config)

    // show the success modal
    setTimeout(() => dispatch({ type: CHECKOUT_FINISHED }), 3000)
    dispatch({ type: CLEAR_CART_AFTER_CHECKOUT })

    setTimeout(() => {
      dispatch(clearErrors())
    }, 3000)

    // now update the past order of the user
    if (response.data) {
      const user = await axios.get('/users/me', config)
      console.log(user, ' QUI USER')
      return dispatch({ type: UPDATE_PAST_ORDERS, user: user.data })
    }
  } catch (err) {
    console.log(err.response.status, ' qui')
    dispatch(
      returnErrors(
        err.response.data.message,
        err.response.status,
        'CHECKOUT ERROR',
      ),
    )
    setTimeout(() => {
      dispatch(clearErrors())
    }, 3000)
  }
}
