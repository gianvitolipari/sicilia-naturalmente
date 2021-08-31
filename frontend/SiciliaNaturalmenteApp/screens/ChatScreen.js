import React, {
  useState,
  useCallback,
  useEffect,
  useLayoutEffect,
  useRef,
} from 'react'
import { GiftedChat } from 'react-native-gifted-chat'
import { View, Keyboard } from 'react-native'
import { db } from '../firebase'
import { AntDesign } from '@expo/vector-icons'
import { Header, Badge, Text } from 'react-native-elements'
import { images } from '../data/dummy-data'
import { StatusBar } from 'expo-status-bar'
import { MaterialCommunityIcons } from '@expo/vector-icons'
import { Pressable } from 'react-native'
import OverlayEmail from '../components/Utils/OverlayEmail'
import { useSelector } from 'react-redux'

const ChatScreen = ({ route, navigation }) => {
  const email = route.params?.email

  const [messages, setMessages] = useState([])
  const [length, setLength] = useState(0)

  const [modalVisible, setModalVisible] = useState(false)

  const textRef = useRef(true)

  //render the cart icon
  const renderRightComponent = () => {
    return (
      <Pressable
        onPress={() => setModalVisible(!modalVisible)}
        style={{
          padding: 10,
          backgroundColor: 'white',
          borderRadius: 20,
        }}
      >
        <MaterialCommunityIcons
          name="email-check-outline"
          size={28}
          color="black"
        />
        <Badge
          containerStyle={{ position: 'absolute', top: -4, right: -4 }}
          badgeStyle={{ backgroundColor: '#fa5246' }}
          value={'email'}
        />
      </Pressable>
    )
  }

  //render the arrow key
  const renderLeftComponent = () => {
    return (
      <View style={{ padding: 10, backgroundColor: 'white', borderRadius: 20 }}>
        <AntDesign
          name="arrowleft"
          size={28}
          color="black"
          onPress={() => navigation.popToTop()}
        />
      </View>
    )
  }

  useEffect(() => {
    // for real update
    const subscribe = db
      .collection('chats')
      .where('webutils._id', '==', email)
      .onSnapshot(async (snapshot) => {
        snapshot.docChanges().forEach((change) => {
          if (change.type == 'added') {
            let data = change.doc.data()
            data.createdAt = data.createdAt

            setMessages((prevMessages) => GiftedChat.append(prevMessages, data))
          }
        })
        // send the notification to the user
      })

    // set the welcome message
    textRef.current &&
      setMessages((previousMessages) =>
        GiftedChat.append(previousMessages, {
          _id: 1,

          text:
            'Puoi chiedermi di tutti gli stati degli ordini, delle novità e informazioni!',
          createdAt: new Date(),
          user: {
            _id: 2,
            name: 'SiciliaNaturalmente',
            avatar:
              'https://www.accademiasicilianadellapasta.it/wp-content/uploads/2020/06/Logo-Sicilia.jpg',
          },
        }),
      )

    return () => {
      subscribe()

      textRef.current = 'false'
    }
  }, [])

  useLayoutEffect(() => {
    // query the message

    fetchMessage()

    return console.log('Cleanup')
  }, [])

  const fetchMessage = async () => {
    console.log(email, ' qui')

    //now retrieve the message between the users

    db.collection('chats')
      .where('webutils._id', '==', email)
      .get()
      .then((query) =>
        setMessages(
          query.docs.map((doc) => ({
            _id: doc.data()._id,
            createdAt: doc.data().createdAt,
            text: doc.data().text,
            user: doc.data().user,
          })),
        ),
      )
      .catch(function (error) {
        console.log('Error getting documents: ', error)
      })
  }

  const onSend = useCallback(async (newMessages = []) => {
    const { _id, text, user } = newMessages[0]

    // new object
    let webutils = { _id: email, order: messages.length + 1 }

    const createdAt = Math.floor(new Date().getTime() / 1000)
    db.collection('chats').add({
      _id,

      createdAt,
      text,
      user,
      webutils,
    })
  }, [])

  return (
    <>
      <Header
        backgroundImage={images.otherImages.royaltyBackground}
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
      />
      <OverlayEmail
        modal={modalVisible}
        hideModal={() => setModalVisible(!modalVisible)}
      />

      <GiftedChat
        messages={messages}
        onSend={(messages) => onSend(messages)}
        user={{
          _id: 'gianvito99.gg@gmail.com', //put the email of the user maybe
        }}
      />
      <StatusBar style="light" />
    </>
  )
}

export default ChatScreen
