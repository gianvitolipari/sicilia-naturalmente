import React, { useState } from 'react'
import { Pressable, View, Text, StyleSheet } from 'react-native'
import { Fontisto } from '@expo/vector-icons'
import { AntDesign } from '@expo/vector-icons'
import { MaterialIcons } from '@expo/vector-icons'
import { useNavigation } from '@react-navigation/native'
//fi fi-credit-card
const userLoggedOption = ({
  title,
  screen,
  icon,
  customHandler,
  email,
  customColor,
  customStyle,
}) => {
  const navigation = useNavigation()

  const [selectedIcon] = useState(icon)

  return (
    <Pressable
      onPress={
        customHandler
          ? customHandler
          : () =>
              navigation.navigate(`${screen}`, {
                backButton: false,
                email: email,
              })
      }
      style={[
        {
          alignSelf: 'center',
          width: '90%',
          marginVertical: 20,
        },
        customStyle,
      ]}
    >
      <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
        {selectedIcon}
        <Text
          style={{
            fontFamily: 'Inter_600SemiBold',
            color: customColor ? customColor : '#094067',
          }}
        >
          {title}
        </Text>
        {/* to use MaterialIcons */}

        <AntDesign name="right" size={24} color="#094067" />
      </View>
    </Pressable>
  )
}

const style = StyleSheet.create({
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

export default userLoggedOption
