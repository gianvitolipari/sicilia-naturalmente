import React, { useState, useEffect } from 'react'
import {
  View,
  Text,
  ImageBackground,
  StyleSheet,
  Dimensions,
  SafeAreaView,
  Pressable,
  Image,
} from 'react-native'
import { Divider } from 'react-native-elements'
import { useNavigation } from '@react-navigation/native'
import { useDispatch } from 'react-redux'
import {
  Ionicons,
  AntDesign,
  Fontisto,
  Feather,
  MaterialIcons,
} from '@expo/vector-icons'
import * as userActions from '../../store/actions/user'
import Heart from '../../assets/heart'
import { SvgUri } from 'react-native-svg'

const win = Dimensions.get('window')
const ratio = win.width / 541 //541 is actual image width

import UserLoggedOption from './userLoggedOption'

const userLogged = ({ payload }) => {
  const navigation = useNavigation()

  const dispatch = useDispatch()
  const email = payload.user.email

  const user = `https://avatars.dicebear.com/api/micah/${payload.user.nome}.svg?radius=20`

  console.log(user, 'aqui')

  return (
    <SafeAreaView style={{ flex: 1 }}>
      <ImageBackground
        source={require('../../assets/photo/userBackground.jpeg')}
        style={styles.image}
      >
        <View style={styles.userImage}>
          <SvgUri width="100%" height="100%" uri={user} />
        </View>
      </ImageBackground>

      <View style={styles.ViewContainer}>
        <Text style={[styles.heading, styles.viewHeading]}>
          Account {payload.user.nome}
        </Text>

        <UserLoggedOption
          title="ORDINI EFFETTUATI"
          screen="HistoryOrders"
          icon={<Fontisto name="shopping-bag-1" size={24} color="#094067" />}
          logout="true"
          email={email}
        />
        <UserLoggedOption
          title="FAVORITI"
          screen="Favourites"
          icon={<Heart color="#094067" secondColor="#094067" />}
          logout="true"
          email={email}
        />

        <UserLoggedOption
          title="CARTE UTENTE"
          screen="CreditCardInformation"
          icon={<Fontisto name="credit-card" size={18} color="#094067" />}
          logout="true"
          email={email}
        />

        <UserLoggedOption
          title="INDIRIZZO UTENTE"
          screen="BillingDetailsScreen"
          icon={<Feather name="map-pin" size={24} color="#094067" />}
          logout="true"
          email={email}
        />

        <UserLoggedOption
          title="CHAT CON ADMIN"
          screen="ChatScreen"
          icon={
            <Ionicons
              name="chatbubble-ellipses-outline"
              size={24}
              color="#094067"
            />
          }
          logout="true"
          email={email}
        />

        <Divider
          style={{
            backgroundColor: '#094067',
            width: '85%',
            alignSelf: 'center',
            marginBottom: 10,
          }}
        />

        <Pressable
          onPress={() => dispatch(userActions.logout)}
          style={{
            alignSelf: 'center',
            width: '90%',
            marginVertical: 20,
          }}
        >
          <View
            style={{ justifyContent: 'space-between', flexDirection: 'row' }}
          >
            <MaterialIcons name="logout" size={24} color="#094067" />

            <Text style={{ fontFamily: 'Inter_400Regular', color: '#094067' }}>
              LOGOUT
            </Text>
            <AntDesign name="right" size={24} color="#094067" />
          </View>
        </Pressable>
      </View>
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  image: {
    overflow: 'hidden',
    borderRadius: 50,
    width: win.width,
    height: ratio * 400,
    alignItems: 'center',
    justifyContent: 'center',
  },
  userImage: {
    borderRadius: 50,
    width: 100,
    height: 100,
    backgroundColor: 'white',
    zIndex: 10,
    marginTop: 10,
    alignItems: 'center',
    justifyContent: 'center',
  },
  logo: {
    width: 66,
    height: 58,
  },
  heading: {
    alignSelf: 'center',
    color: '#094067',
    fontFamily: 'Inter_600SemiBold',
    textTransform: 'uppercase',
  },
  ViewContainer: {
    flex: 1,
    marginTop: 20,
  },

  viewHeading: {
    alignSelf: 'flex-start',
    fontSize: 23,
    marginBottom: 30,
    textTransform: 'none',
    marginHorizontal: 20,
  },
})

export default userLogged
