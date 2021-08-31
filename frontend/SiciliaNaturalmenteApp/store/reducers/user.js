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
  UPDATE_FAVOURITE,
  UPDATE_PAST_ORDERS,
  SAVE_PAYMENT_METHOD,
} from '../actions/types'
import AsyncStorage from '@react-native-async-storage/async-storage'

//import { useNavigation } from '@react-navigation/native'

//const navigation = useNavigation()

const initialState = {
  token: AsyncStorage.getItem('token'),
  isAuthenticated: false,
  isLoading: false,
  user: {},
  isSignupSuccessful: false,
  paymentMethod: '',
}

export default (state = initialState, action) => {
  switch (action.type) {
    case USER_LOADING:
      return {
        ...state,
        isLoading: true,
      }
    case USER_LOADED:
      return {
        ...state,
        isLoading: false,
      }
    case REGISTER_SUCCESS:
      console.log('utente registrato')
      return {
        ...state,
        isSignupSuccessful: true,
      }
    case FINISH_REGISTER:
      return {
        ...state,
        isSignupSuccessful: false,
      }
    case LOGIN_SUCCESS:
      AsyncStorage.setItem('token', action.token)

      return {
        ...state,
        user: action.payload,
        isAuthenticated: true,
        isLoading: false,
        token: action.token,
        paymentMethod: action.pm,
      }
    case LOGOUT_SUCCESS:
    case AUTH_ERROR:
    case LOGIN_FAIL:
    case LOGOUT_SUCCESS:
    case REGISTER_FAIL:
      AsyncStorage.removeItem('token')

      return {
        ...state,
        token: null,
        user: {
          email: null,
          favourites: [],
          ordersHistory: [],
          creditCardInformation: [],
        },
        isAuthenticated: false,
        isLoading: false,
        isSignupSuccessful: false,
        paymentMethod: '',
      }
    case UPDATE_PAST_ORDERS:
      return {
        ...state,
        user: action.user,
      }
    case UPDATE_FAVOURITE:
      return {
        ...state,
        user: action.user,
      }
    case SAVE_PAYMENT_METHOD:
      return {
        ...state,
        paymentMethod: action.payload,
      }
  }

  return state
}
