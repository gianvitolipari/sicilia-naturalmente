import React from 'react'
//* component -> one box of Box flatList in ProductScreen.js

import { Text, View, StyleSheet, Image, TouchableOpacity } from 'react-native'
import { images } from '../../data/dummy-data'

const OneBox = (props) => {
  return (
    <View style={styles.screen}>
      <TouchableOpacity onPress={props.onSelect}>
        <View style={styles.textContainer}>
          <Text style={{ color: '#094067', fontFamily: 'Inter_600SemiBold' }}>
            {props.title}
          </Text>
        </View>
        <View style={styles.imageContainer}>
          <Image
            source={{ uri: props.image }}
            style={{
              width: '70%',
              height: 150,
              borderRadius: 20,
            }}
          />
          <View style={styles.sideContainer}>
            <Text style={styles.priceText}>{props.price}€</Text>
            <Image
              source={images.otherImages.surpriseImage}
              style={{ width: 35, height: 35 }}
            ></Image>
          </View>
        </View>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    marginHorizontal: 20,
    marginVertical: 20,
  },
  imageContainer: {
    borderRadius: 10,
    overflow: 'hidden',
    flexDirection: 'row',
  },
  textContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 10,
    paddingLeft: 10,
  },
  sideContainer: {
    backgroundColor: '#fbdd74',
    width: '100%',
    padding: 30,
    marginLeft: 5,
    borderRadius: 10,
    justifyContent: 'center',
  },
  priceText: {
    color: '#094067',

    fontFamily: 'Inter_600SemiBold',
    fontSize: 20,
  },
})

export default OneBox
