//* component -> category component in ProductScreen.js

import React from 'react'
import { Text, View, StyleSheet, Image, Pressable } from 'react-native'
import PastaIcon from '../assets/photo/PastaIcon'
import PastaIconOutline from '../assets/photo/PastaIconOutline'
import BoxOutline from '../assets/photo/boxOutline'
import OtherProducts from '../assets/photo/OtherProducts'

const Category = (props) => {
  return (
    <Pressable onPress={() => props.onSelect(props.title)}>
      <View
        style={{
          ...styles.imageContainer,
          borderColor: props.color ? '#5f6c7b' : 'black',
        }}
      >
        {(() => {
          switch (props.image) {
            case 4:
              return <PastaIcon />
            case 1:
              return <PastaIconOutline />
            case 5:
              return <OtherProducts />
            case 6:
              return <BoxOutline />
            default:
              return (
                <Image source={props.image} style={{ width: 40, height: 40 }} />
              )
          }
        })()}
      </View>
    </Pressable>
  )
}

const styles = StyleSheet.create({
  imageContainer: {
    borderWidth: 1,
    width: '30%',

    borderRadius: 15,
    padding: 12,
    overflow: 'hidden',
  },
})

export default Category
