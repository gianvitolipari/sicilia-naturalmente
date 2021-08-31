// * Pasta details screen
// ? what should it do
// * View the selected product, add it to the cart with arbitrary q.ty
// * Add it or delete it to favourite list if user is logged

import React, { useState } from 'react'
import {
  View,
  Text,
  StyleSheet,
  Image,
  TextInput,
  KeyboardAvoidingView,
  Keyboard,
  ScrollView,
  Alert,
  Pressable,
} from 'react-native'
import { AntDesign } from '@expo/vector-icons'
import Heart from '../assets/heart'
import { useDispatch, useSelector } from 'react-redux'
import * as cartActions from '../store/actions/cart'
import * as userActions from '../store/actions/user'
import { SimpleLineIcons } from '@expo/vector-icons'
import globalStyles from '../Style'
import CustomModal from '../components/CustomModal'
import PastaDetails from '../components/Pasta/PastaDetails'
import BoxDetails from '../components/Box/BoxDetails'
import { images } from '../data/dummy-data'
import { StatusBar } from 'expo-status-bar'
import ErrorModal from '../components/authComponents/ErrorModal'
import { Badge, Icon, withBadge, Header } from 'react-native-elements'

const ProductDetails2 = ({ route, navigation }) => {
  let selectedProduct = {}
  let isFavourite = false

  const products = useSelector((state) => state.products.availableProducts)

  const isAuth = useSelector((state) => state.user.isAuthenticated)

  const favourites = useSelector((state) => state.user?.user?.favoriti)

  const email = useSelector((state) => state.user.user)

  const productId = route.params
  const dispatch = useDispatch()

  const [quantity, setQuantity] = useState(1)

  const [customText, setCustomText] = useState()

  const [showModal, setShowModal] = useState(false)

  //count for the cart
  const badgeCount = useSelector((state) => state.cart.totalQuantity)

  //cerca il prodotto esatto
  if (products) {
    selectedProduct = products.find(
      (product) => product.idProdotto === productId.itemId,
    )
  }

  if (products && isAuth && favourites) {
    /* logic for the favourite */
    // search if this product is user's favourite
    isFavourite =
      favourites.filter((product) => product.titolo === selectedProduct.titolo)
        .length > 0
  }

  let boxLayout = null
  //if the category is a box change the layout (again)
  if (selectedProduct?.formato === 'SCATOLA') {
    //I have to find first the products based on the id
    boxLayout = selectedProduct.contenuto.map((prod, i) =>
      console.log(prod, ' in map'),
    )
  }

  const addTofavourite = async (email, title, isDelete) => {
    if (!isAuth) {
      return Alert.alert(
        'Funzione non accessibile',
        'Devi autenticarti per accedere a questa funzione!',
        [
          {
            text: 'OK',
            onPress: () => navigation.navigate('Account'),
            style: 'cancel',
          },
        ],
      )
    }
    await dispatch(userActions.updateFavourite(email, title, isDelete))
  }
  /* end of the favourite logic */

  let itsHealthy = false
  selectedProduct?.formato !== 'PRODOTTI' &&
    (itsHealthy = selectedProduct.grano === 'Perciasacchi' ? true : false)

  //redux dispatch
  const onAddTocart = () => {
    //validate again user input

    setShowModal(true)
    dispatch(cartActions.addToCart(selectedProduct, quantity))
    setTimeout(() => setShowModal(false), 2000)
  }

  //validate quantity chocen
  const validateQuantity = (type) => {
    if (quantity === 1 && type === 'meno') {
      setCustomText({
        status: 'custom',
        id: 'Non consentito',
        msg: 'just no',
      })
    } else if (
      (type === 'meno' && quantity >= 1) ||
      quantity == selectedProduct.quantita
    ) {
      setQuantity(quantity - 1)
    } else {
      setQuantity(quantity + 1)
    }
    if (quantity == selectedProduct.quantita) {
      setCustomText({
        status: 'custom',
        id: 'Stock Error',
        msg: 'Disponibilità massima',
      })
    }
    setTimeout(() => {
      setCustomText()
    }, 2000)
  }

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

  //render the cart icon
  const renderRightComponent = () => {
    return (
      <View style={styles.iconContainerShopping}>
        <SimpleLineIcons
          name="handbag"
          size={30}
          color="black"
          onPress={() => navigation.navigate('Cart')}
        />
        <Badge
          containerStyle={{ position: 'absolute', top: -4, right: -4 }}
          badgeStyle={{ backgroundColor: '#fa5246' }}
          value={badgeCount}
        />
      </View>
    )
  }

  return (
    <KeyboardAvoidingView
      style={styles.screen}
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
      onPress={() => {
        Keyboard.dismiss()
      }}
    >
      <Header
        backgroundImage={{
          uri: selectedProduct.immagine,
        }}
        statusBarProps={{ barStyle: 'light-content' }}
        leftComponent={renderLeftComponent}
        leftContainerStyle={{
          alignItems: 'center',

          alignSelf: 'flex-start',
        }}
        rightComponent={renderRightComponent}
        rightContainerStyle={{
          alignItems: 'center',

          alignSelf: 'flex-end',
        }}
        backgroundColor="#3D6DCC"
        containerStyle={{ flex: 0.7, alignItems: 'flex-start' }}
      />

      {customText && <ErrorModal payload={customText} />}
      <View style={styles.descriptionContainer}>
        <View style={{ padding: 20 }}>
          {/* Heading View */}
          <View
            style={{ justifyContent: 'space-between', flexDirection: 'row' }}
          >
            <Text style={styles.heading}>{selectedProduct.titolo}</Text>
            <View style={styles.quantityContainer}>
              <AntDesign
                name="minus"
                size={20}
                color="white"
                style={styles.input}
                onPress={() => validateQuantity('meno')}
              />
              <Text style={{ color: 'black' }}>{quantity}</Text>
              <AntDesign
                name="plus"
                size={20}
                color="white"
                style={styles.input}
                onPress={() => validateQuantity('piu')}
              />
            </View>
          </View>
          <Text
            style={{
              fontSize: 20,
              color: '#5f6c7b',
              fontFamily: 'Inter_600SemiBold',
              marginTop: 5,
            }}
          >
            {selectedProduct.prezzo * quantity} €
          </Text>
          <Text
            style={{
              color: '#251F45',
              fontFamily: 'Inter_600SemiBold',
              marginTop: 30,
            }}
          >
            DESCRIZIONE
          </Text>
          <Text style={styles.text}>{selectedProduct.descrizione}</Text>
          {selectedProduct.formato === 'SCATOLA' && (
            <BoxDetails
              selectedProduct={selectedProduct}
              boxLayout={boxLayout}
            />
          )}
        </View>
        {/*Start Button Container Component  */}
        <View style={styles.buttonContainer}>
          {isFavourite ? (
            <Pressable
              style={{
                padding: 7,
                alignItems: 'center',
              }}
              onPress={() => addTofavourite(email, selectedProduct, 1)}
            >
              <Heart color="#fa5246" secondColor="#fa5246" />
            </Pressable>
          ) : (
            <Pressable
              style={{
                alignSelf: 'flex-end',
                padding: 10,
                backgroundColor: '#f2f7f5',
                borderRadius: 20,
              }}
              onPress={() => addTofavourite(email, selectedProduct, 0)}
            >
              <Heart color="#00214d" secondColor="none" />
            </Pressable>
          )}
          <Pressable style={styles.button} onPress={() => onAddTocart()}>
            <Text
              style={{
                fontSize: 16,
                color: 'white',
                textAlign: 'center',
                fontFamily: 'Inter_400Regular',
              }}
            >
              Aggiungi al Carrello
            </Text>
          </Pressable>
        </View>
      </View>
      <CustomModal
        showModal={showModal}
        text="Prodotto aggiunto!"
        theme="rgba(44, 182, 125, 1)"
      />

      <StatusBar style="light" />
    </KeyboardAvoidingView>
  )
}

const styles = StyleSheet.create({
  screen: {
    flex: 1,
  },

  descriptionContainer: {
    overflow: 'hidden',
    paddingBottom: 0,
    flex: 1,
    justifyContent: 'space-between',
    backgroundColor: '#f2f7f5',
  },
  heading: {
    fontFamily: 'Inter_600SemiBold',
    fontSize: 23,
    color: '#00214d',
  },
  buttonContainer: {
    alignItems: 'center',
    alignSelf: 'flex-end',
    width: '100%',
    flexDirection: 'row',
    backgroundColor: '#00214d',
    paddingHorizontal: 20,
    paddingVertical: 20,
    borderTopLeftRadius: 15,
    borderTopRightRadius: 15,
    borderTopStartRadius: 30,
    borderTopEndRadius: 30,
  },

  input: {
    paddingTop: 3.5,
    backgroundColor: '#fa5246',
    borderRadius: 15,
    width: 30,
    height: 30,
    textAlign: 'center',
    marginHorizontal: 5,
  },
  text: {
    color: '#094067',
    fontFamily: 'Inter_400Regular',
    lineHeight: 30,
    marginTop: 5,
  },
  iconContainer: {
    padding: 5,
    backgroundColor: '#00214d',
    borderRadius: 20,
  },
  iconContainerShopping: {
    padding: 10,
    backgroundColor: 'white',
    borderRadius: 20,
  },
  button: {
    padding: 10,
    backgroundColor: '#fa5246',
    borderRadius: 20,
    width: '80%',
    marginLeft: 10,
  },
  quantityContainer: {
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
    width: '25%',
  },
})

export default ProductDetails2
