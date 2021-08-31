import { PASTA } from '../../data/dummy-data'
import { SET_PASTA, IS_LOADING } from '../actions/types'

const initialState = {
  availableProducts: [
    {
      categoria: 'pasta',
    },
  ],

  isLoading: false,
}

export default (state = initialState, action) => {
  switch (action.type) {
    case SET_PASTA:
      return {
        availableProducts: action.pasta,
        isLoading: false,
      }
    case IS_LOADING:
      return {
        oneProduct: action.pasta,
        isLoading: true,
      }
  }
  return state
}
