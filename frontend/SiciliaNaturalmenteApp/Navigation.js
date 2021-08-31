import React from 'react'
import { createStackNavigator } from '@react-navigation/stack'
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs'

//to add badge to the icon I have to access the global store
import { useSelector } from 'react-redux'

import ProductScreen from './screens/ProductsScreen'
import CartScreen from './screens/CartScreen'
import UserScreen from './screens/UserScreen'
import DoveSiamoScreen from './screens/WhereWeAreScreen'
import ProductDetails2 from './screens/DetailsScreen2'
import OrderHistoryScreen from './screens/OrdersHistoryScreen'
import OrderHistoryDetails from './components/pastOrderComponet/pastOrderDetail'
import FavouritesScreen from './screens/FavouritesScreen'
import CreditCardInformation from './screens/CreditInformationScreen'
import BillingDetailsScreen from './screens/BillingDetailsScreen'
import Ionicons from 'react-native-vector-icons/Ionicons'
import ChatScreen from './screens/ChatScreen'
import SearchScreen from './screens/SearchScreen'

const Stack = createStackNavigator()

const Tab = createBottomTabNavigator()

export const AppTab = () => {
  const badgeCount = useSelector((state) => state.cart.totalQuantity)

  return (
    <Tab.Navigator
      screenOptions={({ route }) => ({
        tabBarIcon: ({ focused, color, size, iconName }) => {
          switch (route.name) {
            case 'Home':
              iconName = 'restaurant-outline'
              color = focused ? 'rgb(0, 122, 255)' : '#8E8E8F'
              break
            case 'Dove Siamo':
              iconName = 'map-outline'
              color = focused ? 'rgb(0, 122, 255)' : '#8E8E8F'
              break
            case 'Cart':
              iconName = 'cart-outline'
              color = focused ? 'rgb(0, 122, 255)' : '#8E8E8F'
              break
            case 'Account':
              iconName = 'person-outline'
              color = focused ? 'rgb(0, 122, 255)' : '#8E8E8F'
          }

          // You can return any component that you like here!
          return <Ionicons name={iconName} size={size} color={color} />
        },
      })}
    >
      <Tab.Screen name="Home" component={AppStack} />
      <Tab.Screen name="Dove Siamo" component={DoveSiamoScreen} />
      <Tab.Screen
        name="Cart"
        component={CartScreen}
        options={{
          tabBarBadge: badgeCount > 0 ? badgeCount : null,
          tabBarBadgeStyle: { backgroundColor: '#ef4565' },
        }}
      />
      <Tab.Screen name="Account" component={UserStack} />
    </Tab.Navigator>
  )
}

const UserStack = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="AccountHome"
        component={UserScreen}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="HistoryOrders"
        component={OrderHistoryScreen}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="HistoryOrdersDetails"
        component={OrderHistoryDetails}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="Favourites"
        component={FavouritesScreen}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="CreditCardInformation"
        component={CreditCardInformation}
        options={{
          headerShown: false,
        }}
      />
    </Stack.Navigator>
  )
}

const AppStack = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="Prodotti"
        component={ProductScreen}
        options={{
          headerShown: false,
        }}
      />
      <Stack.Screen
        name="SearchScreen"
        component={SearchScreen}
        options={{
          headerShown: false,
        }}
      />
    </Stack.Navigator>
  )
}

//container with screens without the stack
export const AppContainer = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen
        name="TabApp"
        component={AppTab}
        options={{
          headerShown: false,
        }}
      />

      <Stack.Screen
        name="Dettagli"
        component={ProductDetails2}
        options={{
          headerShown: false,
        }}
      />

      <Stack.Screen
        name="ChatScreen"
        component={ChatScreen}
        options={{
          headerShown: false,
        }}
      />

      <Stack.Screen
        name="BillingDetailsScreen"
        component={BillingDetailsScreen}
        options={{
          headerShown: false,
        }}
      />
    </Stack.Navigator>
  )
}
