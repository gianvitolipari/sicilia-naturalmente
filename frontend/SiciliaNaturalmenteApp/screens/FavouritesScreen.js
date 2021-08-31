// * FAVOURITE SCREEN
// ? what should it do
// * View favourite product of the user, click them or delete them

import React from 'react'
import { View } from 'react-native'
import globalStyles from '../Style'
import { Header, Text, ListItem, Avatar } from 'react-native-elements'
import { AntDesign } from '@expo/vector-icons'
import { useDispatch, useSelector } from 'react-redux'

import * as UserActions from '../store/actions/user'

const FavouritesScreen = ({ navigation, route }) => {
  const dispatch = useDispatch()

  const favourites = useSelector((state) => state.user.user.favoriti)
  //I want to retrieve also the image

  const email = route.params.email.email

  const products = useSelector((state) => state.products.availableProducts)

  return (
    <View style={globalStyles.container}>
      <Header
        statusBarProps={{ barStyle: 'light-content' }}
        centerComponent={
          <Text style={globalStyles.header_text}>PRODOTTI FAVORITI</Text>
        }
        leftComponent={
          <AntDesign
            name="arrowleft"
            size={30}
            color="white"
            onPress={() => navigation.popToTop()}
          />
        }
        backgroundColor="#00214d"
        containerStyle={{
          height: 120,
          borderRadius: 30,
          alignItems: 'center',
          marginBottom: 10,
        }}
      />
      {products &&
        favourites.map((favourite, i) => (
          <ListItem
            style={{ width: '90%', alignSelf: 'center', marginBottom: 5 }}
            key={i}
            bottomDivider
            onPress={() => {
              navigation.navigate('Dettagli', {
                itemId: favourite.idProdotto,
              })
            }}
          >
            <Avatar
              source={{
                uri: favourite.immagine,
              }}
            />
            <ListItem.Content
              style={{ justifyContent: 'space-between', flexDirection: 'row' }}
            >
              <ListItem.Title style={globalStyles.text}>
                {favourite.titolo}
              </ListItem.Title>
              <ListItem.Title
                style={globalStyles.cancel_label}
                onPress={() =>
                  dispatch(UserActions.updateFavourite(email, favourite, 1))
                }
              >
                CANCELLA
              </ListItem.Title>
            </ListItem.Content>
          </ListItem>
        ))}
    </View>
  )
}

export default FavouritesScreen
