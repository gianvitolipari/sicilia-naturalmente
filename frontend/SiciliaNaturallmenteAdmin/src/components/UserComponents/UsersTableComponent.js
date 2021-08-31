import React, {
  useState,
  useEffect,
  useRef,
  useCallback,
  useContext,
} from 'react'
import CustomTable from '../Utils/TableComponent/CustomTable'
import axios from '../../axios'
import {
  Td,
  Tr,
  Tooltip,
  IconButton,
  Flex,
  Text,
  Button,
} from '@chakra-ui/react'
import { Context } from '../../App'
import { useHistory } from 'react-router'

import CustomModal from '../Utils/CustomModal/CustomModal'
import { DeleteIcon, EmailIcon, InfoIcon } from '@chakra-ui/icons'

import { Link } from 'react-router-dom'

const UsersTableComponent = ({ setInfo, setError }) => {
  //for render optimization
  const renderCall = useRef(true)

  // import the dispatch object from App.js
  const { dispatch } = useContext(Context)

  const history = useHistory()

  // declare user var
  const [userList, setUserList] = useState(null)
  const [isLoading, setLoading] = useState(false)

  //fetch all the user
  const fetchUser = useCallback(async () => {
    const token = localStorage.getItem('token')
    console.log(token, ' qui')
    let config = {
      headers: {
        Authorization: 'Bearer ' + token,
      },
    }
    try {
      const data = await axios.get('users/accounts', config)
      console.log(data)
      populateUser(data)
      setLoading('true')
    } catch (err) {
      // se è un cliente che fa la richiesta elimina cliente
      if (err?.response?.data.status === 403) {
        console.log('lol')
        dispatch({
          type: 'SET_USER',
          user: '',
        })
        history.push('/')
      }

      console.log(err?.response?.data, 'in user table')
    }
  }, [])

  // handle delete users
  const handleDeleteUser = async (email) => {
    try {
      const token = await localStorage.getItem('token')

      let config = {
        headers: {
          Authorization: 'Bearer ' + token,
        },
      }
      console.log(email, ' ok')
      const response = await axios.delete(`users/delete?email=${email}`, {
        headers: {
          Authorization: 'Bearer ' + token,
        },
        data: {
          email: email,
        },
      })
      setInfo(response.data)
      fetchUser()
      setTimeout(() => setInfo(''), 3000)
    } catch (e) {
      console.log(e.request, 'quu')
      setError(e.response.data.message)
      setTimeout(() => setError(''), 3000)
    }
  }

  //populate the list according to the table
  const populateUser = useCallback((data) => {
    const list = data?.data?.map((user) => (
      <Tr key={user.idAccount}>
        <Td>{user.nome}</Td>
        <Td>{user.email}</Td>
        <Td>
          <CustomModal
            isCentered
            header={'Delete user'}
            icon={<DeleteIcon />}
            labeltool="delete user"
            blockScrollOnMount={false}
            size="xl"
          >
            <Flex alignItems="center" justifyContent="space-between">
              <Text mr="3">Are you sure you want to delete {user.email} ?</Text>
              <Button
                colorScheme="twitter"
                onClick={() => handleDeleteUser(user.email)}
              >
                Delete
              </Button>
            </Flex>
          </CustomModal>
          <Tooltip
            label="Contact user"
            aria-label="Contact tooltip"
            colorScheme="twitter"
            hasArrow
          >
            <Link
              to={{
                pathname: `adminHomePageChat/${user.email}`,
                state: { user: user },
              }}
            >
              <IconButton
                variant="outline"
                colorScheme="twitter"
                aria-label="send email"
                icon={<EmailIcon />}
                ml={[0, 2, 2, 6]}
                mt={[3, 0, 2, 0]}
              />
            </Link>
          </Tooltip>
          <Tooltip
            label="See info"
            aria-label="info tooltip"
            colorScheme="twitter"
            hasArrow
          >
            <Link
              to={{
                pathname: `adminHomePage/${user.email}`,
                state: { user: user },
              }}
            >
              <IconButton
                variant="outline"
                colorScheme="twitter"
                aria-label="see info"
                icon={<InfoIcon />}
                ml={[0, 0, 0, 6]}
                mt={[3, 2, 2, 0]}
              />
            </Link>
          </Tooltip>
        </Td>
      </Tr>
    ))

    setUserList(list)
  }, [])
  //when fetch is finished populate the list in the table

  useEffect(() => {
    if (renderCall.current) {
      fetchUser()
      populateUser()
    }

    return () => {
      renderCall.current = false
    }
  }, [userList, isLoading])

  return (
    <CustomTable
      w="80%"
      marginX="10"
      keys={['Nome', 'Email', 'Actions']}
      title="List all user"
      loading={isLoading ? 'false' : undefined}
    >
      {userList}
    </CustomTable>
  )
}

export default UsersTableComponent
