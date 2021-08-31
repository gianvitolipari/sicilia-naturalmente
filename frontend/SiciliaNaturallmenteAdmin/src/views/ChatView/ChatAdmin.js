import React, { useRef, useState } from 'react'
import { useCollectionData } from 'react-firebase-hooks/firestore'
import './ChatAdmin.module.css'
import { Flex, Text } from '@chakra-ui/react'

import _, { orderBy } from 'lodash'
import ContainerFlex from '../../components/Utils/FlexComponent/containerHorizzontal'
import firebase from 'firebase/app'
import 'firebase/firestore'
import { useLocation } from 'react-router'

var firebaseConfig = {
  apiKey: 'AIzaSyBw30XXFh0PaQlmqfmmOguZII3nGaN5Z3k',
  authDomain: 'sicilianaturalmentegiftedchat.firebaseapp.com',
  projectId: 'sicilianaturalmentegiftedchat',
  storageBucket: 'sicilianaturalmentegiftedchat.appspot.com',
  messagingSenderId: '527428083170',
  appId: '1:527428083170:web:03f1e409d57bb0cf042dbb',
}
if (!firebase.apps.length) {
  firebase.initializeApp(firebaseConfig)
}

const db = firebase.firestore()

const ChatAdmin = () => {
  //object to retrieve all the state from the path of react router
  const location = useLocation()

  // list of all the user info
  const stateUser = location.state?.user

  const dummy = useRef()
  const messagesRef = db.collection('chats')
  const query = messagesRef.where('webutils._id', '==', stateUser.email)

  const [messages] = useCollectionData(query, { idField: '_id' })

  const [formValue, setFormValue] = useState('')

  useCollectionData(query, { idField: '_id' })

  let messagesOrdered = []

  // order the messages
  if (messages?.length > 1) {
    messagesOrdered = _.orderBy(messages, ['createdAt'], ['asc'])
    console.log(messagesOrdered, ' quiAAA')
  }

  function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (
      c,
    ) {
      var r = (Math.random() * 16) | 0,
        v = c == 'x' ? r : (r & 0x3) | 0x8
      return v.toString(16)
    })
  }

  const sendMessage = async (e) => {
    e.preventDefault()

    await messagesRef.add({
      text: formValue,
      createdAt: Math.round(new Date().getTime() / 1000),
      _id: uuidv4(), // unique id,
      user: {
        _id: stateUser.email, // email of th user we want to send
        avatar:
          'https://www.accademiasicilianadellapasta.it/wp-content/uploads/2020/06/Logo-Sicilia.jpg',
      },
      webutils: {
        _id: stateUser.email,
      },
    })
    setFormValue('')
    dummy.current.scrollIntoView({ behavior: 'smooth' })
  }

  function ChatMessage(props) {
    const { text } = props.message

    console.log(props.message.user?._id, ' ok')

    const direction =
      props.message.user?._id === 'gianvito99.gg@gmail.com'
        ? 'row-reverse'
        : 'row'

    const color =
      props.message.user?._id === 'gianvito99.gg@gmail.com'
        ? '#0b93f6'
        : '#white'
    return (
      <>
        <Flex className={`message ${direction}`} flexDir={direction}>
          <Text marginBottom="3" alignSelf="flex-end" color={color}>
            {text}
          </Text>
        </Flex>
      </>
    )
  }

  return (
    <ContainerFlex alignSelf="center" justifyContent="center">
      <main>
        {messagesOrdered &&
          messagesOrdered.map((msg) => (
            <ChatMessage key={msg._id} message={msg} />
          ))}

        <span ref={dummy}></span>
      </main>

      <form
        onSubmit={sendMessage}
        style={{
          height: '10vh',
          position: 'fixed',
          bottom: 0,
          backgroundColor: 'rgb(24, 23, 23)',
          width: '80%',
          display: 'flex',
          fontSize: '1.5rem',
        }}
      >
        <input
          value={formValue}
          onChange={(e) => setFormValue(e.target.value)}
          placeholder="say something nice"
        />

        <button
          type="submit"
          disabled={!formValue}
          style={{ width: '20%', backgroundColor: 'rgb(56, 56, 143)' }}
        >
          🕊️
        </button>
      </form>
    </ContainerFlex>
  )
}

export default ChatAdmin
