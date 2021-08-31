//* component -> custom modal

import React from 'react'
import { View, Text, Modal, StyleSheet } from 'react-native'

const CustomModal = ({ showModal, text, theme, children, customStyle }) => {
  return (
    <Modal
      animationType="slide"
      transparent={true}
      visible={showModal}
      centered
    >
      <View style={customStyle ? customStyle : styles.centeredView}>
        <View style={[styles.modalView, { backgroundColor: theme }]}>
          <Text style={styles.modalText}>{text}</Text>
          {children}
        </View>
      </View>
    </Modal>
  )
}

const styles = StyleSheet.create({
  centeredView: {
    flex: 1,
    justifyContent: 'flex-end',
    alignItems: 'center',
    marginTop: 10,
  },
  modalView: {
    borderRadius: 10,
    padding: 20,
    margin: 20,
    paddingVertical: 15,
    width: '80%',
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  modalText: {
    textAlign: 'center',
    fontFamily: 'Inter_600SemiBold',
    fontSize: 18,
    color: 'white',
  },
})

export default CustomModal
