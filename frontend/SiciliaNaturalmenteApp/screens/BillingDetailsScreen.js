import React, { useState, useEffect } from 'react'
import {
  Text,
  Platform,
  Keyboard,
  SafeAreaView,
  StyleSheet,
  Alert,
  StatusBar,
  View,
} from 'react-native'
import { GooglePlacesAutocomplete } from 'react-native-google-places-autocomplete'
import { useNavigation } from '@react-navigation/core'
import { images } from '../data/dummy-data'
import { Header, Button } from 'react-native-elements'
import axios from '../axios'
import AsyncStorage from '@react-native-async-storage/async-storage'
import ErrorModal from '../components/authComponents/ErrorModal'
import { useSelector, useDispatch } from 'react-redux'
import * as errorActions from '../store/actions/error'
import { AntDesign } from '@expo/vector-icons'

const BillingDetailsScreen = ({ route }) => {
  const [address, setAddress] = useState()
  const navigation = useNavigation()

  const dispatch = useDispatch()

  // retrieve from the store address and errors
  const userAddress = useSelector((state) => state.user.user?.indirizzo)
  const error = useSelector((state) => state.error)

  //render the back button conditionally
  const backButton = route.params

  //render the arrow key
  const renderLeftComponent = () => {
    return (
      <View style={styles.iconContainer}>
        <AntDesign
          name="arrowleft"
          size={28}
          color="#fffffe"
          onPress={() => navigation.popToTop()}
        />
      </View>
    )
  }

  console.log(backButton, ' QUI')

  console.log(userAddress, 'In billing')

  const [loading, setLoading] = useState(false)

  //prevent the user to left the screen until he insert all the data
  useEffect(() => {
    backButton.firstTime &&
      navigation.addListener('beforeRemove', (e) => {
        e.preventDefault()
        Alert.alert(
          'Dati necessari',
          'perfavore inserisci indirizzo di spedizione simile all esempio fornito',
        )
      })
    return () => navigation.removeListener()
  }, [navigation])

  const handleSubmit = async () => {
    setLoading(true)
    try {
      const token = await AsyncStorage.getItem('token')

      console.log(address.description, ' ok')

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
        params: {
          indirizzo: address.description,
        },
      }

      /*  controllo parametro  */
      if (address.terms.length !== 5) {
        //  controllo lunghezza

        dispatch(
          errorActions.returnErrors('Indirizzo non corrretto', 100, 'Errore'),
        )
        setTimeout(() => dispatch(errorActions.clearErrors()), 3000)
        setLoading(false)

        return
      }

      const data = await axios.post('/users/address', null, config)
      if (data.data) {
        setLoading(false)
        navigation.navigate('AccountHome')
      }
    } catch (err) {
      setLoading(false)
      console.log(err, 'qui')
      console.log(err.response.data)
    }
  }

  return (
    <SafeAreaView
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      onPress={() => {
        Keyboard.dismiss()
      }}
      style={{ flex: 1 }}
    >
      <Header
        backgroundImage={images.otherImages.royaltyBackground}
        backgroundImageStyle={{ borderRadius: 30 }}
        statusBarProps={{ barStyle: 'light-content' }}
        leftContainerStyle={{
          marginLeft: 10,
        }}
        leftComponent={!backButton.firstTime ? renderLeftComponent : null}
        backgroundColor="#F6F7FA"
        containerStyle={{
          height: 150,
        }}
      />
      {error.status && <ErrorModal payload={error} />}
      <View style={{ padding: 15, flex: 1 }}>
        <Text style={styles.heading}> Indirizzo di spedizione </Text>
        <Text style={styles.text}>
          inserisci il tuo indirizzo per intero, se è possibile con il civico
        </Text>
        {userAddress ? (
          <Text style={styles.text}>Indirizzo scelto: {userAddress}</Text>
        ) : !address ? (
          <Text style={styles.text}>
            Esempio: Via Cagliari, 11, Catania, CT, Italia
          </Text>
        ) : (
          <Text style={styles.text}>
            E' simile all'esempio mostrato? Potrai comunque cambiarlo per
            confermare l'indirizzo di spedizione
          </Text>
        )}

        <GooglePlacesAutocomplete
          placeholder="Search"
          minLength={2} // minimum length of text to search
          autoFocus={false}
          returnKeyType={'search'} // Can be left out for default return key https://facebook.github.io/react-native/docs/textinput.html#returnkeytype
          listViewDisplayed="auto" // true/false/undefined
          fetchDetails={true}
          renderDescription={(row) => row.description} // custom description render
          onPress={(data, details = null) => {
            console.log(data, 'qui')
            setAddress(data)
          }}
          query={{
            // available options: https://developers.google.com/places/web-service/autocomplete
            key: 'AIzaSyDCetXIWkMO_4b_NH-s_lzepRCym53wp1Q',
            language: 'it', // language of the results
            types: 'address', // default: 'geocode'
          }}
          styles={{
            description: {
              fontWeight: 'bold',
            },
            predefinedPlacesDescription: {
              color: '#1faadb',
            },
          }}
          nearbyPlacesAPI="GooglePlacesSearch" // Which API to use: GoogleReverseGeocoding or GooglePlacesSearch
          GooglePlacesDetailsQuery={{
            fields: 'formatted_address',
          }}
          filterReverseGeocodingByTypes={[
            'locality',
            'administrative_area_level_3',
          ]} // filter the reverse geocoding results by types - ['locality', 'administrative_area_level_3'] if you want to display only cities
          debounce={200}
        />

        <Button
          title={userAddress ? 'Aggiorna' : 'Conferma'}
          disabled={!address}
          onPress={() => handleSubmit()}
          color="#094067"
          loading={loading}
          raised
        />
      </View>
      <StatusBar style="light" />
    </SafeAreaView>
  )
}

const styles = StyleSheet.create({
  heading: {
    fontFamily: 'Inter_600SemiBold',
    fontSize: 30,
    color: '#094067',
    marginBottom: 20,

    marginTop: 20,
  },
  iconContainer: {
    padding: 5,
    backgroundColor: '#00214d',
    borderRadius: 20,
  },
  text: {
    fontFamily: 'Inter_600SemiBold',
    fontSize: 15,
    color: '#094067',
    marginBottom: 20,
    marginLeft: 8,
    marginTop: 10,
  },
})

export default BillingDetailsScreen
