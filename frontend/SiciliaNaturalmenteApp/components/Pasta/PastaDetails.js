//* component -> pastaDetails in ProductDetails.js

import React from 'react'
import { View, Text, StyleSheet } from 'react-native'

const PastaDetails = ({ selectedProduct }) => {
  return (
    <View style={styles.listContainer}>
      <Text style={styles.description}>{selectedProduct.descrizione}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  listContainer: {
    height: 100,
    justifyContent: 'space-between',
    marginTop: 10,
    padding: 10,
  },
  description: {
    fontFamily: 'Inter_400Regular',
    color: '#222525',
  },
})

export default PastaDetails
