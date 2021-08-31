import Prodotto from '../../models/prodotto'
import { SET_PASTA, IS_LOADING } from './types'
import axios from '../../axios'

export const fetchProducts = () => {
  return async (dispatch, getState) => {
    try {
      dispatch({ type: IS_LOADING })
      console.log('getting products from the server..')
      const data = await axios.get('/product/')

      const resData = await data.data
      const loadedPasta = []

      for (const key in resData) {
        loadedPasta.push(
          new Prodotto(
            resData[key].idProdotto,
            resData[key].categoria,
            resData[key].descrizione,

            resData[key].titolo,
            resData[key].prezzo,
            resData[key].immagineRetro,
            resData[key].grano,
            resData[key].minutiPreparazione,
            resData[key].grammi,
            resData[key].quantita,
            resData[key].contenuto,
            resData[key].formato,
            resData[key].immagine,
          ),
        )
      }

      dispatch({
        type: SET_PASTA,
        pasta: loadedPasta,
      })
    } catch (err) {
      //send to custom analytics server
      console.log(err)
      throw err
    }
  }
}
export const fetchOneProducts = (filterParam, filterType) => {
  return async (dispatch, getState) => {
    //perform the actions based on the filter
    console.log(filterType)
    dispatch({ type: IS_LOADING })
    switch (filterType) {
      case 'titolo':
        try {
          if (filterParam === '') {
            console.log('nothing')
            return dispatch(fetchProducts())
          }

          const data = await axios.get(`/product/research/${filterParam}`)

          const resData = await data.data

          return dispatch({
            type: SET_PASTA,
            pasta: resData,
          })
        } catch (err) {
          //send to custom analytics server
          console.log(err)
          throw err
        }

      case 'price':
        try {
          const data = await axios.get('/product/price')

          const resData = await data.data

          return dispatch({
            type: SET_PASTA,
            pasta: resData,
          })
        } catch (err) {
          //send to custom analytics server
          console.log(err)
          throw err
        }
      default:
        try {
          const data = await axios.get(`/product/format/${filterParam}`)

          const resData = await data.data

          console.log(resData)

          return dispatch({
            type: SET_PASTA,
            pasta: resData,
          })
        } catch (err) {
          //send to custom analytics server
          console.log(err)
          throw err
        }
    }
  }
}
