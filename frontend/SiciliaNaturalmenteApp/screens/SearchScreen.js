import React, { useState } from 'react'
import { View, ActivityIndicator } from 'react-native'
import {
  SearchBar,
  Header,
  Button,
  ListItem,
  Avatar,
  Text,
} from 'react-native-elements'
import { useSelector, useDispatch } from 'react-redux'
import * as PastaProdcuctActions from '../store/actions/products'
import globalStyles from '../Style'
import { AntDesign } from '@expo/vector-icons'

const SearchScreen = ({ navigation }) => {
  const [search, setSearch] = useState('')

  const dispatch = useDispatch()

  const products = useSelector((state) => state.products?.availableProducts)

  console.log(products, 'SEARCH')

  //logic for the SearchBar
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
    <View style={globalStyles.container}>
      <Header
        statusBarProps={{ barStyle: 'light-content' }}
        centerComponent={
          <Text style={globalStyles.header_text}>Cerca Prodotti</Text>
        }
        leftComponent={
          <AntDesign
            name="arrowleft"
            size={30}
            color="white"
            onPress={() => navigation.popToTop()}
          />
        }
        backgroundColor="#00214d"
        containerStyle={{
          height: 80,
          borderRadius: 10,
          borderBottomStartRadius: 10,
          borderBottomEndRadius: 10,
          alignItems: 'center',
        }}
      />
      <View style={{ width: '90%', alignSelf: 'center', marginTop: 30 }}>
        <SearchBar
          round
          placeholder="Cerca prodotto"
          searchIcon={{ size: 24 }}
          onChangeText={(text) => searchFilterFunction(text)}
          onClear={(text) => searchFilterFunction('')}
          value={search}
          inputContainerStyle={{ backgroundColor: 'white' }}
          containerStyle={{
            borderWidth: 0,
            backgroundColor: 'white',
            borderColor: 'grey',
            borderBottomColor: 'white',
            borderTopColor: 'white',
            borderRadius: 20,
            padding: 2,
            marginBottom: 20,
          }}
        />

        {products ? (
          products.map((prod) => (
            <ListItem
              key={prod.idProdotto}
              style={{ marginVertical: 5 }}
              onPress={() => {
                navigation.navigate('Dettagli', {
                  itemId: prod.idProdotto,
                })
              }}
            >
              <Avatar
                source={{
                  uri: prod.immagine,
                }}
              />
              <ListItem.Content
                style={{
                  flexDirection: 'row',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                }}
              >
                <ListItem.Title
                  style={{ fontFamily: 'Inter_400Regular', color: '#00214d' }}
                >
                  {prod.titolo}
                </ListItem.Title>
              </ListItem.Content>
              <ListItem.Chevron />
            </ListItem>
          ))
        ) : (
          <ActivityIndicator
            size="large"
            color="#00214d"
            style={{ marginTop: 20 }}
          />
        )}
      </View>
    </View>
  )
}

export default SearchScreen
