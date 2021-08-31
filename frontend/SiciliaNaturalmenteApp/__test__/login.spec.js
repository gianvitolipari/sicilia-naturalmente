import React from 'react'
import Login from '../testUtils/loginTest'
import { render, fireEvent, waitFor } from '@testing-library/react-native'
import configureStore from 'redux-mock-store'
import { Provider } from 'react-redux'

const mockStore = configureStore([])

const userStore = mockStore({
  user: {
    isLoading: false,
    isSignupSuccessful: false,
  },
  error: {
    msg: {},
    status: null,
    id: null,
  },
})

describe('the Login screen', () => {
  it('should render Login & register component correctly', () => {
    // await store.dispatch(LoginActions.register('ok'))

    render(
      <Provider store={userStore}>
        <Login />
      </Provider>,
    )
  })

  //testing isSignIn props
  it('should render Login first ', () => {
    const { getByTestId, debug } = render(
      <Provider store={userStore}>
        <Login />
      </Provider>,
    )

    expect(getByTestId('login'))
  })

  it('should render Register if "Registrati" is pressed ', () => {
    const { getByTestId, debug } = render(
      <Provider store={userStore}>
        <Login />
      </Provider>,
    )

    const button = getByTestId('RegistratiTest')

    //clicchiamo sul pulsante "registrati"
    fireEvent.press(button)

    //dovrebbe spuntare la logica per rigistrarsi
    expect(getByTestId('registrati'))
  })

  //registrati button it cant be pressed without validation
  it('register button should be not enabled with unchecked validation  ', async () => {
    const handleSubmit = jest.fn()
    const { getByTestId, debug } = render(
      <Provider store={userStore}>
        <Login />
      </Provider>,
    )

    const changeToRegisterButton = getByTestId('RegistratiTest')

    //clicchiamo sul pulsante "registrati"
    fireEvent.press(changeToRegisterButton)

    //find by instead of get is async queries
    const registerButton = getByTestId('RegisterButton')

    await waitFor(() => {
      fireEvent.press(registerButton)
    })

    //the button will not pressed cause the user is null
    expect(handleSubmit).toHaveBeenCalledTimes(0)
  })

  //registrati button it cant be pressed without validation
  it('register button should be enable with checked validation  ', async () => {
    const handleSubmit = jest.fn()
    const { getByTestId, debug } = render(
      <Provider store={userStore}>
        <Login onSubmit={handleSubmit} />
      </Provider>,
    )

    const changeToRegisterButton = getByTestId('RegistratiTest')

    //clicchiamo sul pulsante "registrati"
    fireEvent.press(changeToRegisterButton)

    const name = getByTestId('Nome')
    const email = getByTestId('email')
    const password = getByTestId('password')
    const confirmPassword = getByTestId('confirmPassword')

    /* change their value  */
    await waitFor(() => {
      fireEvent.changeText(name, 'alfred')
    })
    await waitFor(() => {
      fireEvent.changeText(email, 'alfri.lipari@gmail.com')
    })
    await waitFor(() => {
      fireEvent.changeText(password, 'wiiddialga1')
    })
    await waitFor(() => {
      fireEvent.changeText(confirmPassword, 'wiiddialga1')
    })

    const registerButton = getByTestId('RegisterButton')

    await waitFor(() => {
      fireEvent.press(registerButton)
    })
    //the user is valid so the handleSubmit will be called.
    await waitFor(() => {
      expect(handleSubmit).toHaveBeenCalled()
    })
  })
})
