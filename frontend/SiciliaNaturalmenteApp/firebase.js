import * as firebase from 'firebase'
import 'firebase/firestore'

var firebaseConfig = {
  apiKey: 'AIzaSyBw30XXFh0PaQlmqfmmOguZII3nGaN5Z3k',
  authDomain: 'sicilianaturalmentegiftedchat.firebaseapp.com',
  projectId: 'sicilianaturalmentegiftedchat',
  storageBucket: 'sicilianaturalmentegiftedchat.appspot.com',
  messagingSenderId: '527428083170',
  appId: '1:527428083170:web:03f1e409d57bb0cf042dbb',
}

let app
if (firebase.apps.length === 0) {
  app = firebase.initializeApp(firebaseConfig)
} else {
  app = firebase.app()
}

const db = app.firestore()
export { db }
