
{


    /**
     *  Placer Order
     * @param {org.ebpi.blockathon.Order} PlaceOrder
     * @transaction
     */


    function sendShipment(shipment){
        const manufacturer= getCurrentParticipant()
        const manuID = manufacturer.PartnerID
        const hash = shipment.DocumentHash
        const docLoc = shipment.DocumentLocation
        const procductList = shipment.ShippedProducts
        const transprterList = shipment.transporterList
        const destination= shipment.Destination
        const customer = shipment.Customer
        const costID = costumer.PartnerID
        const originalWeight = shipment.Weight
        let factory = getFactory()
        let newShipment = factory.newResource('org.ebpi.blockathon', 'Shipment')
        newshipment.DocumentHash = hash
        newshipment.DocumentLocation =docLoc
        newshipment.ShippedProducts=procductList
        newshipment.transporterList=transprterList
        newshipment.Destination=destination
        newshipment.Customer = factory.newRelationship('org.ebpi.blockathon', 'Orderer', costID)
        newshipment.Supplier =factory.newRelationship('org.ebpi.blockathon', 'Manufacturer', manuID)
        newshipment.Weight= originalWeight
        return getAssetRegistry('org.ebpi.blockathon.Shipment')
            .then(function (shipmentRegistry) {
                return shipmentRegistry.add(newshipment)
            })
    }
}