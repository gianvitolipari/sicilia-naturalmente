//* component -> one pasta of the FlatList in ProductScreen.js

import React from 'react'
import { Text, View, StyleSheet, Image, TouchableOpacity } from 'react-native'
import AppLoading from 'expo-app-loading'
import {
  useFonts,
  Inter_900Black,
  Inter_400Regular,
} from '@expo-google-fonts/dev'

const Product = (props) => {
  let [fontsLoaded] = useFonts({
    Inter_400Regular,
    Inter_900Black,
  })

  if (!fontsLoaded) {
    return <AppLoading />
  } else {
    //x 0.5 e 1
    return (
      <View style={styles.screen}>
        <TouchableOpacity onPress={props.onSelect} style={{ flex: 1 }}>
          <View style={styles.imageContainer}>
            <Image
              source={{ uri: props.image }}
              style={{
                width: 180,
                height: 200,
              }}
            />
          </View>
          <View style={styles.textContainer}>
            <Text
              style={{
                fontFamily: 'Inter_600SemiBold',
                fontSize: 12,
                color: '#094067',
              }}
            >
              {props.title}
            </Text>
            <View style={{ width: 50 }}>
              <Text
                style={{
                  fontFamily: 'Inter_600SemiBold',
                  color: '#094067',
                  fontSize: 18,
                }}
              >
                {props.price}€
              </Text>
            </View>
          </View>
        </TouchableOpacity>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    maxWidth: '45%',
    marginHorizontal: 10,
    marginVertical: 10,
    height: 300,
    borderRadius: 20,
    overflow: 'hidden',
  },
  imageContainer: {
    borderRadius: 20,
    overflow: 'hidden',
    marginTop: 20,
    alignSelf: 'center',
    marginHorizontal: 20,
  },
  textContainer: {
    flex: 1,
    justifyContent: 'space-around',
    paddingHorizontal: 20,
    paddingVertical: 10,
  },
})

export default Product
