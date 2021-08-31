//* component -> box details in ProductsDetails

import React from 'react'
import { View, Text, StyleSheet, Pressable } from 'react-native'
import axios from '../../axios'
import { Avatar, Divider } from 'react-native-elements'
import { useNavigation } from '@react-navigation/native'
const BoxDetails = ({ selectedProduct, boxLayout }) => {
  const navigation = useNavigation()
  return (
    <View style={{ marginTop: 10 }}>
      {/* render the box Layout */}
      {boxLayout && (
        <>
          <Divider
            style={{
              backgroundColor: '#3da9fc',
              height: 0.5,
              marginVertical: 5,
            }}
          />
          <Text style={styles.text}>Cosa contiene: </Text>
          <View style={styles.boxLayout}>
            {boxLayout.map((product, i) => (
              <Pressable
                style={{ alignItems: 'center' }}
                key={product.idProdotto}
                onPress={() => {
                  navigation.navigate('Dettagli', {
                    itemId: product.idProdotto,
                  })
                }}
              >
                <Avatar
                  rounded
                  source={{
                    uri: product.image,
                  }}
                ></Avatar>
                <Text>x {selectedProduct.products[i].qty}</Text>
              </Pressable>
            ))}
          </View>
        </>
      )}
    </View>
  )
}

const styles = StyleSheet.create({
  listContainer: {
    height: 100,
    justifyContent: 'space-between',
    marginTop: 10,
  },
  boxLayout: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    marginTop: 4,
  },
  text: {
    color: '#094067',
    textAlign: 'center',
    fontFamily: 'Inter_400Regular',
  },
})

export default BoxDetails
