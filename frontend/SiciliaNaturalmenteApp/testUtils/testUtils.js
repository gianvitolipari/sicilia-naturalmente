import React from 'react'
import { render } from '@testing-library/react-native'
import { Provider } from 'react-redux'
import { PersistGate } from 'redux-persist/integration/react'
import { NavigationContainer } from '@react-navigation/native'
import { store, persistor } from '../store'

const TestProvider = ({ store, children }) => (
  <Provider store={store}>
    <PersistGate persistor={persistor}>
      <NavigationContainer>{children}</NavigationContainer>
    </PersistGate>
  </Provider>
)

export function testRender(ui, { store, ...otherOpts }) {
  return render(<TestProvider store={store}>{ui}</TestProvider>, otherOpts)
}

export function makeTestStore() {
  const origDispatch = store.dispatch
  store.dispatch = jest.fn(origDispatch)
  return store
}

export const fixtureSet = {
  name: 'Bogus lego set',
  num_parts: 666,
  last_modified_dt: 'long ago',
  set_img_url: 'http://arent.i.pretty',
  set_num: '666_2',
  set_url: 'http://somewhere',
  theme_id: 666,
  year: 666,
}
