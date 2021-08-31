import React, { useState } from 'react'
import { Text, Keyboard, StyleSheet, TextInput } from 'react-native'
import { Overlay, Button } from 'react-native-elements'
import axios from '../../axios'
import * as userActions from '../../store/actions/user'
import * as errorActions from '../../store/actions/error'
import AsyncStorage from '@react-native-async-storage/async-storage'
import { useDispatch, useSelector } from 'react-redux'
import { MaterialIcons } from '@expo/vector-icons'
import { useNavigation } from '@react-navigation/core'
import ErrorModal from '../../components/authComponents/ErrorModal'

const OverlayEmail = (props) => {
  const [email, setEmail] = useState('')

  const dispatch = useDispatch()
  const navigation = useNavigation()

  // state button
  const [state, setState] = useState('idle')

  const error = useSelector((state) => state.error)

  const sendEmail = async () => {
    const token = await AsyncStorage.getItem('token')
    let config = {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    }
    try {
      const response = await axios.post(
        '/sendEmail',
        {
          message: 'ak',
        },
        config,
      )
      console.log(response.data)
    } catch (err) {
      if (err.response) {
        // There is an error response from the server
        // You can anticipate error.response.data here
        console.log(err.response, ' mq')

        dispatch(
          errorActions.returnErrors(
            err.response.data.message,
            err.response.status,
            'EMAIL ERROR',
          ),
        )
        setTimeout(() => {
          dispatch(errorActions.clearErrors())
        }, 3000)
      } else if (err.request) {
        if (e.request.status === 403) {
          dispatch(userActions.logout)
          navigation.navigate('AccountHome')
        }
        dispatch({ type: USER_LOADED })
      } else {
        // Some other errors
        console.log('Error', err.message)
      }
    }
  }

  return (
    <Overlay
      isVisible={props.modal}
      onBackdropPress={() => props.hideModal()}
      overlayStyle={{ width: '80%', position: 'absolute', top: 50 }}
    >
      <Text
        style={{
          fontFamily: 'Inter_600SemiBold',
          color: '#00214d',
          fontSize: 18,
          alignSelf: 'center',
        }}
      >
        Invia una email
      </Text>

      <TextInput
        raised
        style={styles.postInput}
        onChangeText={(text) => setEmail(text)}
        multiline={true}
        icon={<MaterialIcons name="send" size={24} color="white" />}
        numberOfLines={6}
        placeholder="Scrivi una email"
        underlineColorAndroid="transparent"
        require={true}
        value={email}
        textAlignVertical="top"
      />
      <Button
        onPress={() => sendEmail()}
        title="Invia"
        buttonStyle={{
          backgroundColor: '#00214d',

          justifyContent: 'space-around',

          padding: 5,
        }}
        containerStyle={{
          marginVertical: 20,
          width: '95%',
          alignSelf: 'center',
          textAlign: 'center',
        }}
      />
      {error.status && <ErrorModal payload={error} />}
    </Overlay>
  )
}

const styles = StyleSheet.create({
  postInput: {
    borderColor: '#42435b',
    borderWidth: 1,
    margin: 10,
    padding: 5,
  },
})

export default OverlayEmail
