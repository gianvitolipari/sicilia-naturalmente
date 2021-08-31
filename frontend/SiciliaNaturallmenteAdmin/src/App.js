/*
 * Entry point of the admin project
 * Alfredo Lipari, Gianvito Grassi
 */

import React, { useEffect, useReducer, createContext } from 'react'
import './App.css'
import { BrowserRouter, Switch, Route } from 'react-router-dom'
import { ChakraProvider, ColorModeScript } from '@chakra-ui/react'
import customTheme from './styles/themes'
import Homepage from './views/Homepage'
import { reducer } from './Store/Reducers/UserReducer'
import AdminHomepage from './views/AdminHomepage'
import UserInfoView from './views/UserInfoView'
import ChatAdmin from './views/ChatView/ChatAdmin'

export const Context = createContext()

const App = () => {
  const initialState = {
    user: '',
    error: '',
    loading: false,
  }

  const [state, dispatch] = useReducer(reducer, initialState)

  const user = localStorage.getItem('token')

  return (
    <Context.Provider value={{ state, dispatch }}>
      <BrowserRouter>
        <ChakraProvider resetCSS theme={customTheme}>
          <div className="App">
            <Switch>
              <Route path="/" exact component={Homepage} />
            </Switch>

            {/* Protected rotues */}
            {user && (
              <>
                <Switch>
                  <Route
                    path="/adminHomePage"
                    exact
                    component={AdminHomepage}
                  />
                </Switch>
                <Switch>
                  <Route
                    path="/adminHomePage/:userEmail"
                    exact
                    render={(props) => (
                      <UserInfoView globalStore={props} {...props} />
                    )}
                  />
                  <Route
                    path="/adminHomePageChat/:userEmail"
                    exact
                    render={(props) => (
                      <ChatAdmin globalStore={props} {...props} />
                    )}
                  />
                </Switch>
              </>
            )}
          </div>
        </ChakraProvider>
      </BrowserRouter>
    </Context.Provider>
  )
}

export default App
