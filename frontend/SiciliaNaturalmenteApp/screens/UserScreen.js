// * USER SCREEN
// ? what should it do
// * If not Logged View the login screen
// * If logged View all the options

import React from 'react'
import { SafeAreaView, StyleSheet, KeyboardAvoidingView } from 'react-native'
import Login from '../components/authComponents/login'
import UserLogged from '../components/userLoggedComponent/userLogged'
import { Header } from 'react-native-elements'
import { useSelector } from 'react-redux'
import { images } from '../data/dummy-data'
import { StatusBar } from 'expo-status-bar'
import ErrorModal from '../components/authComponents/ErrorModal'

//import { customLeftHeaderComponent } from './ProductsScreen'

/*  User screen, if there is a user render the Account screen otherwise render the Login page    */

const UserScreen = () => {
  const user = useSelector((state) => state.user)
  const error = useSelector((state) => state.error)

  return (
    <SafeAreaView
      style={{
        flex: 1,
        alignItems: 'center',
        //render if is log in or not
        backgroundColor: '#F6F7FA',
      }}
    >
      {user.isAuthenticated ? (
        <UserLogged payload={user} />
      ) : (
        <>
          <Header
            backgroundImage={images.otherImages.royaltyBackground}
            backgroundImageStyle={{ borderRadius: 30 }}
            statusBarProps={{ barStyle: 'light-content' }}
            leftContainerStyle={{
              marginLeft: 10,
            }}
            backgroundColor="#F6F7FA"
            containerStyle={{
              height: 150,
            }}
          />
          {error.status && <ErrorModal payload={error} />}

          <Login />
        </>
      )}
      <StatusBar style="light" />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  input: {
    width: 100,
  },
  ImageBackground: {
    width: '100%',
    height: 150,
  },
})

export default UserScreen
