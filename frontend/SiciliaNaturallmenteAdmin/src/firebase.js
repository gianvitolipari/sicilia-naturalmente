import firebase from 'firebase/app'
import 'firebase/firestore'
var firebaseConfig = {
  apiKey: 'AIzaSyBw30XXFh0PaQlmqfmmOguZII3nGaN5Z3k',
  authDomain: 'sicilianaturalmentegiftedchat.firebaseapp.com',
  projectId: 'sicilianaturalmentegiftedchat',
  storageBucket: 'sicilianaturalmentegiftedchat.appspot.com',
  messagingSenderId: '527428083170',
  appId: '1:527428083170:web:03f1e409d57bb0cf042dbb',
}

firebase.initializeApp(firebaseConfig)

const db = firebase.firestore()

export { db }
