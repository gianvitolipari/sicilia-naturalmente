import { createStore, combineReducers, applyMiddleware, compose } from 'redux'
import productsReducer from './store/reducers/products'
import cartReducer from './store/reducers/cart'
import userReducer from './store/reducers/user'
import errorReducer from './store/reducers/error'
import ReduxThunk from 'redux-thunk'
import AsyncStorage from '@react-native-async-storage/async-storage'
import { persistReducer, persistStore } from 'redux-persist'

const persistConfig = {
  key: 'root',
  storage: AsyncStorage,
}

const rootReducer = combineReducers({
  products: productsReducer,
  cart: cartReducer,
  user: userReducer,
  error: errorReducer,
})

//I DONT USE THIS ALTHOUGH
let composeEnhancers = compose

if (__DEV__) {
  composeEnhancers = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose
}

//create persist reducer

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = createStore(persistedReducer, applyMiddleware(ReduxThunk))

export const persistor = persistStore(store)
