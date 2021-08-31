import React from 'react'

import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
} from '@chakra-ui/modal'
import { IconButton } from '@chakra-ui/button'
import { Tooltip } from '@chakra-ui/tooltip'
import { useDisclosure } from '@chakra-ui/hooks'

const CustomModal = (props) => {
  // declare state for the modals
  const { isOpen, onOpen, onClose } = useDisclosure()

  //dispatch action to select the category to the parent and open modal
  const openModal = () => {
    onOpen()
  }

  return (
    <>
      <Tooltip
        colorScheme="twitter"
        aria-label="add tooltip"
        hasArrow
        label={props.labeltool}
      >
        <IconButton
          variant="outline"
          colorScheme="twitter"
          aria-label="delelte product"
          onClick={openModal}
          icon={props.icon}
        />
      </Tooltip>
      <Modal {...props} isOpen={isOpen} onClose={onClose} colorScheme="twitter">
        <ModalContent p="5">
          <ModalHeader>{props.header}</ModalHeader>
          <ModalCloseButton />
          {props.children}
        </ModalContent>
      </Modal>
    </>
  )
}

export default CustomModal
