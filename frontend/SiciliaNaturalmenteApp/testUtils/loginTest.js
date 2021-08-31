import React, { useState, useEffect } from 'react'
import { View, Text, Keyboard, ActivityIndicator } from 'react-native'
import { Formik } from 'formik'
import { Input, Button } from 'react-native-elements'
import { useDispatch, useSelector } from 'react-redux'
import * as yup from 'yup'

const Login = ({ onSubmit }) => {
  const handleSubmit = async (values) => {
    onSubmit()
  }

  const error = useSelector((state) => state.error)
  const isLoading = useSelector((state) => state.user.isLoading)
  const isSignupSuccessful = useSelector(
    (state) => state.user.isSignupSuccessful,
  )
  const [isSignIn, setIsSignIn] = useState(true)
  const [showModal, setShowModal] = useState(false)

  const dispatch = useDispatch()

  /* se la registrazione ha successo fai apparire il Modal */
  useEffect(() => {
    if (isSignupSuccessful) {
      setIsSignIn(true)
      setShowModal(true)
      setTimeout(() => setShowModal(false), 3000)
    }
  }, [isSignupSuccessful])

  const loginValidationSchema = yup.object().shape({
    email: yup
      .string()
      .email('Please enter valid email')
      .required('Email Address is Required'),
    password: yup
      .string()
      .min(8, ({ min }) => `Password must be at least ${min} characters`)
      .matches(/[a-zA-Z]/, 'Password can only contain Latin letters.')
      .required('Password is required'),
  })

  return (
    <>
      {isSignIn ? (
        <Text testID="login">Log In</Text>
      ) : (
        <Text testID="registrati">Registrati</Text>
      )}

      <Formik
        initialValues={{
          name: '',
          email: '',
          password: '',
          confirmPassword: '',
        }}
        onSubmit={handleSubmit}
        validationSchema={loginValidationSchema}
        onPress={Keyboard.dismiss}
      >
        {({
          handleChange,
          handleBlur,
          handleSubmit,
          values,
          errors,
          isValid,
          touched,
        }) => (
          <View>
            {/* Se l'utente ha scelto di registrarsi spunta il nome    */}
            {!isSignIn ? (
              <Input
                placeholder="Nome"
                onChangeText={handleChange('name')}
                onBlur={handleBlur('name')}
                value={values.name}
                label="Il tuo nome"
                testID="Nome"
              />
            ) : null}
            {/* INPUT EMAIL */}
            <Input
              testID="email"
              placeholder="Email"
              keyboardType="email-address"
              onChangeText={handleChange('email')}
              onBlur={handleBlur('email')}
              value={values.email}
              label={
                errors.email && !isSignIn && touched.password ? (
                  <Text>{errors.email}</Text>
                ) : (
                  'La tua Email'
                )
              }
            />

            {/* END INPUT EMAIL */}

            {/* INPUT PASSWORD */}

            <Input
              testID="password"
              label={
                errors.password && !isSignIn && touched.password ? (
                  <Text>{errors.password}</Text>
                ) : (
                  'La tua Password'
                )
              }
              placeholder="Password"
              onChangeText={handleChange('password')}
              onBlur={handleBlur('password')}
              value={values.password}
              secureTextEntry
            />

            {/* END INPUT PASSWORD */}

            {/* Se l'utente ha scelto di registrarsi spunta il confirm password */}
            {!isSignIn ? (
              <Input
                testID="confirmPassword"
                label="Conferma Password"
                placeholder="Conferma Password"
                onChangeText={handleChange('confirmPassword')}
                onBlur={handleBlur('confirmPassword')}
                value={values.confirmPassword}
                secureTextEntry
              />
            ) : null}

            {isSignIn ? (
              <View>
                <View>
                  <Text
                    testID="RegistratiTest"
                    onPress={() => setIsSignIn(false)}
                  >
                    Registrati
                  </Text>
                </View>
                {isLoading ? (
                  <ActivityIndicator size="large" />
                ) : (
                  <Button onPress={handleSubmit} title="LOG IN" />
                )}
              </View>
            ) : (
              /*  REGISTER COMPONENT   */
              <View>
                <View>
                  <Text onPress={() => setIsSignIn(true)}>Log in</Text>
                </View>
                {isLoading ? (
                  <ActivityIndicator size="large" color="#999999" />
                ) : (
                  <Button
                    testID="RegisterButton"
                    onPress={handleSubmit}
                    disabled={!isValid}
                    title="SIGN IN"
                  />
                )}
              </View>
            )}
          </View>
        )}
      </Formik>
    </>
  )
}

export default Login
