import React, { useState } from 'react'
import MapView, { Marker } from 'react-native-maps'
import { StyleSheet, Text, View, Dimensions } from 'react-native'
import { StatusBar } from 'expo-status-bar'

const DoveSiamo = () => {
  /* setting the initial region */
  const [region, setRegion] = useState({
    latitude: 38.1156879,
    longitude: 13.3612671,
    latitudeDelta: 0.009,
    longitudeDelta: 0.009,
  })

  return (
    <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
      <MapView
        mapPadding={{ top: 10, left: 100 }}
        style={styles.map}
        showsUserLocation
        region={region}
        onRegionChangeComplete={(region) => setRegion(region)}
        onMarkerPress={() => console.log('marker cliccato')}
      >
        <Marker
          coordinate={{ latitude: 51.5078788, longitude: -0.0877321 }}
          pinColor="blue"
        />
      </MapView>
      <StatusBar style="light" />
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
  map: {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
  },
})

export default DoveSiamo
