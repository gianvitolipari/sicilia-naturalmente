/*
 * Reusable table component
 * Alfredo Lipari
 */

import React from 'react'
import {
  Table,
  Thead,
  Tbody,
  Tfoot,
  Tr,
  Th,
  TableCaption,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalCloseButton,
  Divider,
} from '@chakra-ui/react'

import { Skeleton } from '@chakra-ui/skeleton'

const CustomTable = (props) => {
  return (
    <Skeleton
      height="50px"
      isLoaded={props.loading}
      startColor="blue.500"
      endColor="orange.500"
      width="80%"
      marginX="auto"
    >
      <Table {...props} variant="striped" colorScheme="twitter">
        <TableCaption>{props.title}</TableCaption>
        <Thead>
          <Tr>
            {props.keys?.map((key) => (
              <Th key={key}>{key}</Th>
            ))}
          </Tr>
        </Thead>
        <Tbody>{props.children}</Tbody>
        <Tfoot>{props.foot}</Tfoot>
      </Table>
      {props.ismodal === 'true' && (
        <Modal
          isOpen={props.open}
          onClose={props.onClose}
          colorScheme="twitter"
        >
          <ModalContent p="5">
            <ModalHeader>{props.selcat?.title}</ModalHeader>
            <Divider mb="2" />
            <ModalCloseButton />
            <ModalBody>{props.modalbody}</ModalBody>
          </ModalContent>
        </Modal>
      )}
    </Skeleton>
  )
}

export default CustomTable
