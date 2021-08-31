import React from 'react'
import { testRender, fixtureSet, makeTestStore } from '../testUtils/testUtils'
import CartScreen from '../testUtils/cartTest'
import { render, fireEvent, waitFor } from '@testing-library/react-native'
import configureStore from 'redux-mock-store'
import { Provider } from 'react-redux'

const mockStore = configureStore([])

/* MOCK redux is for testing ACTIONS not REDUCERS and so does not return no update state */

//mock the cart store
const cartStore = mockStore({
  cart: {
    items: {
      1: {
        productTitle: 'formaggio',
        productPrice: 2,
        productImage: 'no',
        quantity: 5,
      },
    },
    totalAmount: 0,
    totalQuantity: 0,

    checkoutSuccess: false,
  },
  user: {
    checkoutSuccess: false,
  },
})

describe('the Cart screen', () => {
  it('should render Cart Screen correctly', async () => {
    // await store.dispatch(LoginActions.register('ok'))

    const { debug, getByTestId } = render(
      <Provider store={cartStore}>
        <CartScreen />
      </Provider>,
    )

    await waitFor(() => debug())
  })
})
