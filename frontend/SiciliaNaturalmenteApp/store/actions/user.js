import { returnErrors, clearErrors } from './error'
import {
  USER_LOADED,
  USER_LOADING,
  AUTH_ERROR,
  LOGIN_SUCCESS,
  LOGIN_FAIL,
  LOGOUT_SUCCESS,
  REGISTER_SUCCESS,
  REGISTER_FAIL,
  FINISH_REGISTER,
  UPDATE_PAST_ORDERS,
  UPDATE_FAVOURITE,
  SAVE_PAYMENT_METHOD,
} from './types'

import axios from '../../axios'
import AsyncStorage from '@react-native-async-storage/async-storage'

export const register = (values, isLogin) => {
  return async (dispatch, payload) => {
    dispatch({ type: USER_LOADING })
    const { name, confirmPassword, ...dataLogin } = values

    /* l'utente vuole loggarsi */
    if (isLogin) {
      dispatch(clearErrors())
      try {
        const data = await axios.post('/login/signin', null, {
          params: dataLogin,
        })

        const resData = await data.data

        // save the token
        await AsyncStorage.setItem('token', resData)

        // signin returns the token now retieve all the user info

        let config = {
          headers: {
            Authorization: 'Bearer ' + resData,
          },
        }
        const user = await axios.get('/users/me', config)

        //retrive the paymentMethod selected
        const pm = await AsyncStorage.getItem('payment-method')

        const newPm = await JSON.parse(pm)

        console.log(pm, ' qququ')

        dispatch({ type: USER_LOADED })
        return dispatch({
          //dispatch di authenticate
          type: LOGIN_SUCCESS,
          payload: user.data,
          token: resData,
          pm: newPm,
        })
      } catch (err) {
        if (err.response) {
          // There is an error response from the server
          // You can anticipate error.response.data here

          dispatch(
            returnErrors(
              err.response.data.message,
              err.response.status,
              'LOGIN FAILED',
            ),
          )
        } else if (err.request) {
          // The request was made but no response was received
          // Error details are stored in error.reqeust
          console.log(err.request)
          dispatch({ type: USER_LOADED })
        } else {
          // Some other errors
          console.log('Error', err.message)
        }

        dispatch({ type: USER_LOADED })
        dispatch({
          type: LOGIN_FAIL,
        })
        setTimeout(() => {
          dispatch(clearErrors())
        }, 3000)
        return
      }
    }
    //l'utente vuole registrarsi

    try {
      data = await axios.post('/login/signup', values)

      const token = await data.data
      await AsyncStorage.setItem('token', token)

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }

      //create customer id for the new user and attach it
      const customer = await axios.get('/payment/customer_id', config)
      console.log(customer.data, ' customer')

      dispatch({ type: USER_LOADED })

      setTimeout(
        () =>
          dispatch({
            type: FINISH_REGISTER,
          }),
        2000,
      )

      return dispatch({
        //dispatch di authenticate
        type: REGISTER_SUCCESS,
      })
      /* ERROR CASE  */
    } catch (err) {
      dispatch({ type: USER_LOADED })

      console.log('errore', err.response.data.message)
      dispatch(
        returnErrors(
          err.response.data.message,
          err.response.status,
          'REGISTER FAILED',
        ),
      )
      dispatch({
        type: REGISTER_FAIL,
      })
      setTimeout(() => {
        dispatch(clearErrors())
      }, 3000)
      return
    }
  }
}

export const logout = (dispatch) => {
  AsyncStorage.removeItem('userData')
  //clearLogoutTimer()
  dispatch({ type: LOGOUT_SUCCESS })
}

/* action for load the orders based on the email   */
export const loadPastOrders = (email) => async (dispatch) => {
  dispatch({ type: USER_LOADING })
  try {
    const result = await axios.post('orders/getOrders', email)
    const data = await result.data

    dispatch({ type: USER_LOADED })
    dispatch({ type: LOAD_PAST_ORDERS, payload: data })
  } catch (e) {
    console.log(e.response.data, 'in loadPastOrders')
    dispatch({ type: USER_LOADED })
    dispatch(logout)
  }
}

/* action for add to the favourite based on the email */
export const updateFavourite = (email, prodotto, isDelete) => async (
  dispatch,
) => {
  try {
    const token = await AsyncStorage.getItem('token')

    console.log(prodotto.titolo, ' ok')

    let config = {
      headers: {
        Authorization: 'Bearer ' + token,
      },
      params: {
        titolo: prodotto.titolo,
      },
    }

    // delete the favorite product from the list
    if (isDelete) {
      const result = await axios.delete(
        `users/favorite/delete?titolo=${prodotto.titolo}`,
        {
          headers: {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json, text/plain, */*',
          },
          ì,
        },
      )
      const data = await result.data
      if (data) {
        //update the user
        const user = await axios.get('/users/me', config)
        return dispatch({ type: UPDATE_FAVOURITE, user: user.data })
      }
    }
    console.log('Lol')
    const result = await axios.post('users/favorite', null, config)
    const data = await result.data

    console.log(data, ' qui')

    if (data) {
      //update the user
      const user = await axios.get('/users/me', config)
      dispatch({ type: UPDATE_FAVOURITE, user: user.data })
    }
  } catch (e) {
    console.log(e.request, ' qui')
  }
}

//Stripe api, create a paymentMethod and attach to a user
export const savePaymentMethod = (info) => {
  console.log(info, ' qquuq')
  AsyncStorage.setItem('payment-method', JSON.stringify(info))
  return { type: SAVE_PAYMENT_METHOD, payload: info }
}
