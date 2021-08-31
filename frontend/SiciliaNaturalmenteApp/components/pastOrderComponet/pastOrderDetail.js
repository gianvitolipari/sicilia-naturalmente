//* past order List in pastOrderDetails.js

import React, { useEffect, useState } from 'react'
import { View, StyleSheet, StatusBar, Pressable } from 'react-native'
import { ListItem, Text, Header, Avatar } from 'react-native-elements'
import { useSelector, useDispatch } from 'react-redux'
import { AntDesign } from '@expo/vector-icons'
import axios from '../../axios'
import * as CartActions from '../../store/actions/cart'
import AsyncStorage from '@react-native-async-storage/async-storage'
import * as userActions from '../../store/actions/user'

const PastOrderDetail = ({ route, navigation }) => {
  const dispatch = useDispatch()

  const [content, setContent] = useState([])

  //fetch the id from the nav props
  const orderId = route.params.itemId
  const stato = route.params.status

  const setContentHandle = async () => {
    try {
      const token = await AsyncStorage.getItem('token')

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }

      const order = await axios.get(`users/order/content/${orderId}`, config)
      console.log(order.data, 'kAA')
      setContent(order.data)
    } catch (e) {
      if (e.request.status === 403) {
        dispatch(userActions.logout)
        navigation.navigate('AccountHome')
      }
    }
  }

  // retrieve content
  useEffect(() => {
    setContentHandle()
    return () => console.log('clenaup')
  }, [])

  const renderLeftComponent = () => {
    return (
      <AntDesign
        name="arrowleft"
        size={26}
        color="white"
        onPress={() => navigation.popToTop()}
      />
    )
  }

  const reorderHandle = () => {
    navigation.navigate('Cart')
  }

  return (
    <View>
      <Header
        statusBarProps={{ barStyle: 'light-content' }}
        leftComponent={renderLeftComponent}
        centerComponent={
          <Text
            style={{
              color: 'white',
              fontFamily: 'Inter_600SemiBold',
              fontSize: 18,
            }}
          >
            Ordine n. {orderId}
          </Text>
        }
        backgroundColor="#00214d"
        containerStyle={{
          paddingBottom: 20,
          alignItems: 'center',
        }}
      />
      {stato === 'IN_PREPARAZIONE' ? (
        <Pressable style={styles.button}>
          <Text style={styles.text}>Il tuo ordine è in preparazione</Text>
        </Pressable>
      ) : stato === 'SPEDITO' ? (
        <Pressable style={styles.button}>
          <Text style={styles.text}>Il tuo ordine è stato spedito</Text>
        </Pressable>
      ) : (
        <Pressable style={styles.button}>
          <Text style={styles.text}>Il tuo ordine è stato consegnato</Text>
        </Pressable>
      )}
      <View style={{ width: '90%', alignSelf: 'center' }}>
        <Text
          style={{
            color: 'white',
            fontFamily: 'Inter_600SemiBold',
            fontSize: 18,
            marginLeft: 5,
            color: '#00214d',
            marginBottom: 10,
          }}
        >
          Contenuto:
        </Text>
        {content.map((product, i) => (
          <ListItem key={product.titolo} bottomDivider>
            <Avatar />
            <ListItem.Content
              style={{ flexDirection: 'row', justifyContent: 'space-between' }}
            >
              <ListItem.Title>{product.titolo}</ListItem.Title>
              <ListItem.Title>Qty. {product.quantita}</ListItem.Title>
            </ListItem.Content>
          </ListItem>
        ))}
      </View>
      <StatusBar barStyle="light-content" />
    </View>
  )
}

const styles = StyleSheet.create({
  button: {
    alignSelf: 'center',
    backgroundColor: '#ef4565',
    paddingVertical: 5,
    alignItems: 'center',
    width: '100%',
    marginBottom: 20,
    marginTop: 5,
  },
  text: {
    fontFamily: 'Inter_600SemiBold',
    fontSize: 16,
    color: 'white',
  },
})

export default PastOrderDetail
