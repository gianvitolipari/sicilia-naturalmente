import React from 'react'
import { ListItem, Button, Icon } from 'react-native-elements'
import { AntDesign } from '@expo/vector-icons'

const CardComponent = (props) => {
  return (
    <ListItem.Swipeable
      onPress={props.onSelect}
      rightContent={
        <Button
          title="Delete"
          icon={{ name: 'delete', color: 'white' }}
          buttonStyle={{ minHeight: '100%', backgroundColor: 'red' }}
        />
      }
    >
      <AntDesign name="creditcard" size={24} color="black" />

      <ListItem.Content
        style={{
          flexDirection: 'row',
          alignItems: 'center',
          justifyContent: 'space-between',
        }}
      >
        <ListItem.Title>{props.last4}</ListItem.Title>
        {props.isSelected && (
          <ListItem.Title
            style={{
              backgroundColor: '#42ba96',
              paddingHorizontal: 2,
              fontFamily: 'Inter_400Regular',
              color: 'white',
            }}
          >
            Selezionato
          </ListItem.Title>
        )}
      </ListItem.Content>
      <ListItem.Chevron />
    </ListItem.Swipeable>
  )
}

export default CardComponent
