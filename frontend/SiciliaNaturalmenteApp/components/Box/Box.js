//* component -> FlatList of boxes

import React from 'react'
import { StyleSheet, FlatList, ImageBackground } from 'react-native'
import { Text } from 'react-native-elements'
import { useNavigation } from '@react-navigation/native'
import { useSelector } from 'react-redux'
import OneBox from './oneBox'

const Box = () => {
  const products = useSelector((state) =>
    state.products.availableProducts.filter((box) => box.formato === 'SCATOLA'),
  )

  const renderBox = ({ item }) => {
    return (
      <OneBox
        title={item.titolo}
        image={item.immagine}
        price={item.prezzo}
        products={item.contenuto}
        descrizione={item.descrizione}
        onSelect={() => {
          navigation.navigate('Dettagli', {
            itemId: item.idProdotto,
          })
        }}
      />
    )
  }

  const navigation = useNavigation()

  return (
    <ImageBackground style={styles.container}>
      <Text style={styles.text}>La scelta migliore per un regalo ! </Text>
      <FlatList
        data={products}
        keyExtractor={(item) => item.idProdotto}
        renderItem={renderBox}
        key={2}
      />
    </ImageBackground>
  )
}

const styles = StyleSheet.create({
  container: {
    marginTop: 10,
    width: '100%',
    height: 600,
  },
  text: {
    fontFamily: 'Inter_400Regular',
    textAlign: 'center',
    fontSize: 16,
    marginBottom: 10,
    color: '#094067',
  },
})

export default Box
