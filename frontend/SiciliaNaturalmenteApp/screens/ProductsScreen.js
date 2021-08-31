// * PRODUCTS SCREEN, the first screen to be visualized
// ? What should it do?
// * View all available products, filter them and select one

import React, { useState, useCallback, useEffect } from 'react'
import {
  StyleSheet,
  Text,
  View,
  KeyboardAvoidingView,
  Keyboard,
  Pressable,
  Image,
} from 'react-native'
import { SearchBar, Header, Button } from 'react-native-elements'
import { StatusBar } from 'expo-status-bar'
import { images } from '../data/dummy-data'
import { useDispatch, useSelector } from 'react-redux'
import { AntDesign } from '@expo/vector-icons'
import Pasta from '../components/Pasta/pasta'
import OtherProducts from '../components/OtherProducts/OtherProducts'
import Box from '../components/Box/Box'
import * as userActions from '../store/actions/user'
import * as errorActions from '../store/actions/error'
import Category from '../components/category'
import * as PastaProdcuctActions from '../store/actions/products'
import FilterOverlayPasta from '../components/filterComponent/pastaFilter'
import FilterIcon from '../assets/filter'
//this is the shop screen.

//const for the SiciliaLogo

const ProductsScreen = ({ navigation }) => {
  useEffect(() => {
    // dispatch(userActions.logout)
    console.log('grazie')
  })

  const dispatch = useDispatch()
  const [search, setSearch] = useState('')

  const [selectedCategoryPasta, setSelectedCategoryPasta] = useState(true) //the selected category
  const [selectedCategoryProducts, setSelectedCategoryProducts] = useState(
    false,
  ) //the selected category
  const [selectedCategoryBox, setSelectedCategoryBox] = useState(false) //the selected category
  const [renderedComponent, setRenderComponent] = useState(<Pasta />)

  const [overlayIsVisible, setIsVisible] = useState(false)

  //function to select the current category
  const selectedCategory = (category) => {
    switch (category) {
      case 'pasta':
        setSelectedCategoryPasta(true)
        setSelectedCategoryProducts(false)
        setSelectedCategoryBox(false)
        return setRenderComponent(<Pasta />)
      case 'prodotti':
        setSelectedCategoryPasta(false)
        setSelectedCategoryProducts(true)
        setSelectedCategoryBox(false)
        return setRenderComponent(<OtherProducts />)
      case 'box':
        setSelectedCategoryPasta(false)
        setSelectedCategoryProducts(false)
        setSelectedCategoryBox(true)
        return setRenderComponent(<Box />)
    }
  }
  //logic for the SearchBar3

  const searchFilterFunction = (text) => {
    setSearch(text)
    let newText = text
    if (text.length !== 0) {
      newText = text[0].toUpperCase()
    }
    //perform a filter function
    dispatch(PastaProdcuctActions.fetchOneProducts(newText, 'titolo'))
  }

  return (
    <KeyboardAvoidingView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      style={styles.screen}
      onClick={() => Keyboard.dismiss()}
    >
      <Header
        backgroundImage={images.otherImages.logo}
        backgroundImageStyle={{
          backgroundColor: '#F6F7FA',
        }}
        statusBarProps={{ barStyle: 'light-content' }}
        backgroundColor="#F6F7FA"
        containerStyle={{
          height: 150,
          alignItems: 'center',
          justifyContent: 'center',
          marginBottom: 10,
        }}
      />
      {selectedCategoryPasta && (
        <FilterOverlayPasta
          visible={overlayIsVisible}
          setIsVisible={() => setIsVisible(!overlayIsVisible)}
        />
      )}
      <View
        style={{
          flexDirection: 'row',
          justifyContent: 'space-around',
          alignItems: 'center',
          marginBottom: 10,
        }}
      >
        <Text
          onPress={() => selectedCategory('pasta')}
          style={{
            marginTop: 7,
            fontFamily: selectedCategoryPasta
              ? 'Inter_600SemiBold'
              : 'Inter_400Regular',
            color: selectedCategoryPasta ? '#00214d' : 'rgb(135, 137, 141)',
            borderBottomWidth: selectedCategoryPasta ? 2 : 0,
            borderBottomColor: '#00214d',
            paddingBottom: 2,
          }}
        >
          Pasta
        </Text>
        <Text
          onPress={() => selectedCategory('prodotti')}
          style={{
            marginTop: 7,
            fontFamily: selectedCategoryProducts
              ? 'Inter_600SemiBold'
              : 'Inter_400Regular',
            color: selectedCategoryProducts ? '#00214d' : 'rgb(135, 137, 141)',
            borderBottomWidth: selectedCategoryProducts ? 2 : 0,
            borderBottomColor: '#00214d',
            paddingBottom: 2,
          }}
        >
          Prodotti
        </Text>
        <Text
          onPress={() => selectedCategory('box')}
          style={{
            marginTop: 7,
            fontFamily: selectedCategoryBox
              ? 'Inter_600SemiBold'
              : 'Inter_400Regular',
            color: selectedCategoryBox ? '#00214d' : 'rgb(135, 137, 141)',
            borderBottomWidth: selectedCategoryBox ? 2 : 0,
            borderBottomColor: '#00214d',
            paddingBottom: 2,
          }}
        >
          Scatole
        </Text>
        <Pressable
          onPress={() => setIsVisible(true)}
          style={{
            backgroundColor: '#00214d',
            alignItems: 'center',
            flexDirection: 'row',
            borderRadius: 8,
            padding: 5,
          }}
        >
          <AntDesign name="search1" size={15} color="white" />
          <Text
            style={{
              fontFamily: 'Inter_600SemiBold',
              marginLeft: 5,
              color: 'white',
            }}
          >
            Cerca
          </Text>
        </Pressable>
      </View>

      {renderedComponent}

      <StatusBar style="light" />
    </KeyboardAvoidingView>
  )
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    backgroundColor: '#fffffe',
  },
  logoText: {
    backgroundColor: 'white',
    width: '60%',
    textAlign: 'center',
    padding: 10,
    borderRadius: 20,
    color: '#094067',
    fontFamily: 'Inter_600SemiBold',
    marginBottom: 30,
  },
  categories: {
    marginTop: 10,
    width: '85%',
    flexDirection: 'row',
    alignSelf: 'center',
    justifyContent: 'space-between',
    marginBottom: 10,
  },
  category: {
    padding: 10,
    alignSelf: 'center',
    borderRadius: 10,
    paddingHorizontal: 15,
    alignItems: 'center',
  },

  inputContainer: {
    backgroundColor: '#fffffe',
    justifyContent: 'center',
    width: '65%',
    borderBottomColor: 'transparent',
    borderTopColor: 'transparent',
    marginLeft: 5,
  },
  inputStyle: {
    borderRadius: 10,
    backgroundColor: 'white',
    borderWidth: 2,
    borderColor: 'rgb(0, 122, 255)',
    borderBottomWidth: 2,
    height: 39,
  },
  searchContainer: {
    flexDirection: 'row',
    marginBottom: 10,
    alignItems: 'center',
  },
  text: {
    fontSize: 15,
    marginHorizontal: 5,
    letterSpacing: 3,
    color: '#094067',
    fontFamily: 'Inter_600SemiBold',
  },
})

export default ProductsScreen
