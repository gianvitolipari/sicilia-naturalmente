import {
  ADD_TO_CART,
  REMOVE_FROM_CART,
  ADD_ONE_TO_CART,
  CLEAR_CART_AFTER_CHECKOUT,
  CLEAR_CART,
  CHECKOUT_FINISHED,
} from '../actions/types'

import CartItem from '../../models/cart-item'

const initialState = {
  items: {},
  totalAmount: 0,
  totalQuantity: 0,

  checkoutSuccess: false,
}

export default (state = initialState, action) => {
  switch (action.type) {
    /* THE USER WANTS TO ADD ITEMS   */

    case ADD_TO_CART:
      //if there were an "RIORDINA" ACTION before, reset the cart

      if (Array.isArray(state.items)) {
        state.items = {}
        state.totalAmount = 0
        state.totalQuantity = 0
      }

      const addedProduct = action.product
      const prodPrice = parseInt(addedProduct.prezzo)

      const prodTitle = addedProduct.titolo
      const prodImage = addedProduct.immagine
      const prodQuantity = parseInt(action.quantity)

      let updatedOrNewCartItem

      if (state.items[addedProduct.idProdotto]) {
        // already have the item in the cart

        updatedOrNewCartItem = new CartItem(
          state.items[addedProduct.idProdotto].quantity + prodQuantity,
          prodPrice,
          prodTitle,
          prodImage,
        )
      } else {
        updatedOrNewCartItem = new CartItem(
          prodQuantity,
          prodPrice,
          prodTitle,
          prodImage,
        )
      }

      return {
        ...state,
        items: {
          ...state.items,
          [addedProduct.idProdotto]: updatedOrNewCartItem,
        },
        totalAmount: state.totalAmount + prodPrice * prodQuantity,
        totalQuantity: state.totalQuantity + prodQuantity,
      }

    case CHECKOUT_FINISHED:
      return {
        ...state,
        checkoutSuccess: false,
        totalQuantity: 0,
      }

    case REMOVE_FROM_CART:
      console.log(state.items, 'MH')

      let selectedCartItem = state.items[action.product.productId]

      let currentQty = selectedCartItem.quantity

      let updatedCartItems
      if (currentQty > 1) {
        //delete only one item
        const updatedQuantity = parseInt(selectedCartItem.quantity) - 1

        selectedCartItem.quantity = updatedQuantity //add in the element the new quantity

        updatedCartItems = {
          ...state.items,
          [action.product.productId]: selectedCartItem,
        }
      } else {
        //delete the object
        updatedCartItems = { ...state.items }
        delete updatedCartItems[action.product.productId]
      }
      return {
        ...state,
        items: updatedCartItems,
        totalAmount: state.totalAmount - selectedCartItem.productPrice,
        totalQuantity: state.totalQuantity - 1,
      }
    case ADD_ONE_TO_CART:
      let updatedCart = state.items[action.product.productId] //retrieve the element in the state
      let updatedQuantity =
        parseInt(state.items[action.product.productId].quantity) + 1

      updatedCart.quantity = updatedQuantity //add in the element the new quantity

      return {
        ...state,
        items: {
          ...state.items,
          [action.product.productId]: updatedCart,
        },
        totalAmount: state.totalAmount + action.product.productPrice,
        totalQuantity: state.totalQuantity + 1,
      }

    case CLEAR_CART_AFTER_CHECKOUT:
      return {
        ...state,
        items: {},
        totalAmount: 0,

        checkoutSuccess: true,
      }

    case CLEAR_CART:
      return {
        ...state,
        items: {},
        totalAmount: 0,
        totalQuantity: 0,
      }
  }

  //check if there are no object in the cart
  if (Object.keys(state.items).length === 0) {
    return {
      ...state,
      totalAmount: 0,
    }
  }

  return state
}
