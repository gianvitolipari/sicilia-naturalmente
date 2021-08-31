// * ORDER HISTORY SCREEN
// ? what should it do
// * View all past orders, click on them to see their information

import React from 'react'
import { View, Text, StyleSheet, Pressable, ScrollView } from 'react-native'
import { Header } from 'react-native-elements'
import { AntDesign } from '@expo/vector-icons'
import { useSelector } from 'react-redux'
import { ListItem } from 'react-native-elements'
import globalStyles from '../Style'

const OrdersHistoryScreen = ({ navigation, route }) => {
  /* create dispatch object */

  /* retrieve pastOrders (if there are any) */
  const pastOrders = useSelector((state) => state.user.user?.ordini)

  const lengthOrders = pastOrders.length

  return (
    <View>
      <Header
        backgroundImageStyle={{ resizeMode: 'cover', borderRadius: 30 }}
        statusBarProps={{ barStyle: 'light-content' }}
        centerComponent={
          <Text
            style={{
              color: 'white',
              fontFamily: 'Inter_600SemiBold',
              fontSize: 16,
            }}
          >
            ORDINI PASSATI
          </Text>
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
        containerStyle={{ height: 120, borderRadius: 30, alignItems: 'center' }}
      />

      {lengthOrders > 0 ? (
        <ScrollView>
          {/* when dispatch is fineshed render the list*/}
          {pastOrders.map((order, i) => (
            <Pressable
              onPress={() => {
                navigation.navigate('HistoryOrdersDetails', {
                  itemId: order.idOrdine,
                  status: order.stato,
                })
              }}
              key={order.idOrdine}
            >
              <ListItem containerStyle={styles.listContainer}>
                <ListItem.Content>
                  <ListItem.Title style={{ color: '#094067' }}>
                    {order.data}
                  </ListItem.Title>
                </ListItem.Content>
                <ListItem.Content>
                  <ListItem.Title
                    style={{
                      backgroundColor: 'rgba(216, 238, 254, 0.4)',
                      padding: 10,
                      borderRadius: 10,
                      fontFamily: 'Inter_400Regular',
                      color: '#5f6c7b',
                    }}
                  >
                    totale: {order.prezzoTot / 100} €
                  </ListItem.Title>
                </ListItem.Content>
                <ListItem.Chevron />
              </ListItem>
            </Pressable>
          ))}
        </ScrollView>
      ) : (
        <Text style={[globalStyles.text, styles.text]}>
          Non ci sono ordini passati!
        </Text>
      )}
    </View>
  )
}

const styles = StyleSheet.create({
  Container: {
    flex: 1,
  },
  Heading: {
    color: '#094067',
    fontFamily: 'Inter_600SemiBold',
    fontSize: 20,
    width: '90%',
    alignSelf: 'center',
    marginTop: 10,
    marginBottom: 30,
    elevation: 2,
  },

  image: {
    width: '100%',
    height: 100,
  },
  tableContainer: {
    marginTop: 20,

    alignSelf: 'center',
    height: '80%',
    width: '90%',
  },
  text: {
    alignSelf: 'center',
    marginTop: 10,
  },
  listContainer: {
    marginBottom: 15,
    borderRadius: 20,
    marginTop: 20,
    width: '90%',
    alignSelf: 'center',
    justifyContent: 'space-between',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.5,
    shadowRadius: 2,
    elevation: 2,
  },
})

export default OrdersHistoryScreen
