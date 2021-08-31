//* component -> pasta filter component in ProductScreen.js

import React, { useState } from 'react'
import { View, StyleSheet } from 'react-native'
import {
  Overlay,
  ListItem,
  CheckBox,
  Text,
  Divider,
} from 'react-native-elements'
import { useDispatch } from 'react-redux'
import * as productsActions from '../../store/actions/products'
import { Ionicons } from '@expo/vector-icons'
import LongPastaIcon from '../../assets/longPastaIcon'
import ShortPastaIcon from '../../assets/shortPastaIcon'
import CoinIcon from '../../assets/coinIcon'
import CancelIcon from '../../assets/photo/cancel'
import CircleIcon from '../../assets/circleIcon'
import { useNavigation } from '@react-navigation/native'

const PastaFilter = (props) => {
  //first call dispatch object
  const dispatch = useDispatch()

  const navigation = useNavigation()

  //state for the active filter state
  const [activeFilter, setActiveFilter] = useState('annulla')

  //decide which action take based on the event
  const filterPasta = async (event) => {
    props.setIsVisible()

    switch (event) {
      case 'CERCA':
        setActiveFilter('CERCA')
        navigation.navigate('SearchScreen')
      case 'prezzo':
        setActiveFilter('prezzo')
        return dispatch(productsActions.fetchOneProducts(event, 'price'))
      case 'CORTA':
        setActiveFilter('CORTA')
        return dispatch(productsActions.fetchOneProducts(event, 'format/CORTA'))
      case 'MEDIA':
        setActiveFilter('MEDIA')
        return dispatch(productsActions.fetchOneProducts(event, 'format/MEDIA'))
      case 'LUNGA':
        setActiveFilter('LUNGA')
        return dispatch(productsActions.fetchOneProducts(event, 'format/LUNGA'))

      case 'annulla':
        setActiveFilter('annulla')
        return dispatch(productsActions.fetchProducts())
    }
  }

  const pastaFilter = [
    {
      title: 'Cerca per titolo',
      event: 'CERCA',
    },
    {
      title: 'Ricerca per prezzo minore',
      event: 'prezzo',
    },
    {
      title: 'Pasta piccola',
      event: 'CORTA',
    },
    {
      title: 'Pasta corta',
      event: 'MEDIA',
    },
    {
      title: 'Pasta lunga',
      event: 'LUNGA',
    },

    {
      title: 'togli filtro',
      event: 'annulla',
    },
  ]

  console.log(activeFilter, ' ok')

  return (
    <Overlay
      isVisible={props.visible}
      onBackdropPress={() => props.setIsVisible()}
      overlayStyle={styles.container}
    >
      <View>
        <Text h2 style={styles.h2}>
          Filtra
        </Text>
        <Divider />
        {pastaFilter.map((filter, i) => (
          <ListItem
            key={i}
            bottomDivider
            onPress={() => filterPasta(filter.event)}
            containerStyle={
              filter.event === activeFilter ? styles.bg : styles.bg1
            }
          >
            <ListItem.Content
              style={{
                flexDirection: 'row',
                justifyContent: 'flex-start',
                alignItems: 'center',
              }}
            >
              {(() => {
                switch (i) {
                  case 0:
                    return <Ionicons name="search" size={24} color="#EFC75E" />
                  case 1:
                    return <CoinIcon />
                  case 2:
                    return <CircleIcon />
                  case 3:
                    return <ShortPastaIcon />
                  case 4:
                    return <LongPastaIcon />
                  case 5:
                    return <CancelIcon />
                }
              })()}

              <ListItem.Title style={styles.text}>
                {filter.title}
              </ListItem.Title>
            </ListItem.Content>
            <ListItem.Chevron />
          </ListItem>
        ))}
      </View>
    </Overlay>
  )
}

const styles = StyleSheet.create({
  container: {
    height: '60%',
    width: '80%',
    position: 'absolute',
    top: 30,
    borderRadius: 25,
  },
  h2: {
    marginLeft: 10,
    color: '#094067',
    marginBottom: 5,
    fontFamily: 'Inter_400Regular',
  },
  text: {
    marginLeft: 10,
    color: '#5f6c7b',
    fontFamily: 'Inter_400Regular',
  },
  bg: {
    backgroundColor: '#d8eefe',
  },
  bg1: {
    backgroundColor: 'white',
  },
})

export default PastaFilter
