// create reducer
export function reducer(state, action) {
  switch (action.type) {
    case 'SET_USER':
      return { ...state, user: action.user, error: '' }
    case 'LOGOUT':
      return { ...state, user: '', error: '' }
    case 'LOADING':
      return { ...state, loading: true }
    case 'END_LOADING':
      return { ...state, loading: false }
    case 'ERROR_AUTH':
      return {
        ...state,
        user: '',
        error: action.error,
      }
    case 'CLEAR_ERRORS':
      return {
        ...state,
        error: '',
      }
    default:
      return state
  }
}
