/*
 * Login Component of the Admin initial page
 *
 */

import { VStack } from '@chakra-ui/layout'
import { Button } from '@chakra-ui/button'
import React, { useReducer, useContext, useEffect } from 'react'
import { Context } from '../../App'
import CustomInput from '../Utils/InputComponent/CustomInput'
import { useColorModeValue } from '@chakra-ui/color-mode'
import axios from '../../axios'
import CustomAlert from '../Utils/AlertComponent/Alert'
import { useHistory } from 'react-router'

const LoginHomepage = () => {
  // import the dispatch object from App.js
  const { state, dispatch } = useContext(Context)

  const history = useHistory()

  //use color mode value are Chakra ui toogle theme for dark mode
  const bg = useColorModeValue('gray.500', 'blue.600')
  const color = useColorModeValue('white', 'white.700')

  // CREATE STATE FOR THE INPUTS
  const initialState = {
    email: '',
    password: '',
  }

  useEffect(() => {
    const listener = (event) => {
      if (event.code === 'Enter' || event.code === 'NumpadEnter') {
        console.log('Enter key was pressed. Run your function.')
        event.preventDefault()
        login()
      }
    }
    document.addEventListener('keydown', listener)
    return () => {
      document.removeEventListener('keydown', listener)
    }
  }, [])

  // create reducer
  function reducer(state, action) {
    switch (action.type) {
      case 'SET_INPUT':
        return { ...state, [action.inputName]: action.inputValue }

      default:
        return state
    }
  }

  // event input handler
  // event handler
  function onChange(event) {
    reducerDispatch({
      type: 'SET_INPUT',
      inputName: event.target.name,
      inputValue: event.target.value,
    })
  }

  // login handler
  async function login() {
    console.log(reducerState)
    dispatch({
      type: 'LOADING',
    })
    try {
      const data = await axios.post('/login/signin', null, {
        params: reducerState,
      })

      const token = await data.data

      //provare se ad acccedere è un admin
      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }

      try {
        const data = await axios.get('users/accounts', config)
        console.log('òlol')
      } catch (err) {
        // se è un cliente che fa la richiesta elimina cliente
        if (err?.response?.data.status === 403) {
          console.log('lol')
          dispatch({
            type: 'END_LOADING',
          })
          console.log(err?.response?.data?.message)
          dispatch({
            type: 'ERROR_AUTH',
            error: 'I clienti non possono avere accesso',
          })
          setTimeout(() => {
            dispatch({
              type: 'CLEAR_ERRORS',
            })
          }, 4000)
          return
        }
      }
      if (data.data) {
        dispatch({
          type: 'END_LOADING',
          user: token,
        })

        localStorage.setItem('token', token)
        dispatch({
          type: 'SET_USER',
          user: token,
        })

        history.push('/adminHomePage')
      }
    } catch (err) {
      console.log(err.response.data)
      dispatch({
        type: 'END_LOADING',
      })
      console.log(err?.response?.data?.message)
      dispatch({
        type: 'ERROR_AUTH',
        error: err.response.data.message,
      })
      setTimeout(() => {
        dispatch({
          type: 'CLEAR_ERRORS',
        })
      }, 5000)
    }
  }

  // useReducer hook creates local state
  const [reducerState, reducerDispatch] = useReducer(reducer, initialState)

  return (
    <>
      {/* render conditionally the customAlert */}
      {state.error && (
        <CustomAlert status="error" title="Auth error" descr={state.error} />
      )}

      <VStack
        w={{
          base: '44',
          md: '56',
          lg: '72',
          xl: '96',
        }}
        margin="10em auto"
      >
        <CustomInput name="email" label="email" onChange={onChange} mb="10" />
        <CustomInput
          name="password"
          label="password"
          type="password"
          onChange={onChange}
          mb="10"
        />
        <Button
          w={{
            base: '44',
            md: '56',
            lg: '72',
            xl: '96',
          }}
          onClick={login}
          bg={bg}
          color={color}
          type="submit"
          isLoading={state.loading}
        >
          Login
        </Button>
      </VStack>
    </>
  )
}

export default LoginHomepage
