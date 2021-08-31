//* component -> modal to show the errors

import React from 'react'
import { View, Text, Pressable, StyleSheet, Image } from 'react-native'
import { Ionicons } from '@expo/vector-icons'

const ErrorModal = ({ payload }) => {
  console.log(payload, ' err')

  return (
    <View style={styles.modalContainer}>
      <View style={{ flexDirection: 'row', alignItems: 'center' }}>
        <Ionicons name="alert-circle-outline" size={24} color="#722D36" />
        <Text style={styles.headingText}>{payload.id}</Text>
      </View>

      <Text style={styles.errorMsg}>{payload.msg}</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  modalContainer: {
    width: '100%',
    alignSelf: 'center',
    borderRadius: 3,
    backgroundColor: '#F8D7DA',
    padding: 8,
    justifyContent: 'space-between',
    alignItems: 'center',
    flexDirection: 'row',
  },

  headingText: {
    fontFamily: 'Inter_600SemiBold',
    color: '#722D36',
    marginLeft: 3,
  },

  errorMsg: {
    alignSelf: 'center',
    fontFamily: 'Inter_600SemiBold',
    color: '#001858',
  },
})

export default ErrorModal
