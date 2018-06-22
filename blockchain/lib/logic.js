
/**
* Sample transaction
* @param {org.ebpi.blockathon.sendShipment} sendShipment
* @transaction
*/


function doSendShipment(sendShipment){
    const manufacturer= getCurrentParticipant();
    const manuID = manufacturer.PartnerID;
    const hash = sendShipment.DocumentHash;
    const docLoc = sendShipment.DocumentLocation;
    const procductList = sendShipment.ShippedProducts;
    const transprterList = sendShipment.transporterList;
    const destination= sendShipment.Destination;
    const costumer = sendShipment.Customer;
    const costID = costumer.PartnerID;
    const originalWeight = sendShipment.Weight;
    let factory = getFactory();
    let newShipment = factory.newResource('org.ebpi.blockathon', 'Shipment');
    newShipment.DocumentHash = hash;
    newShipment.DocumentLocation =docLoc;
    newShipment.ShippedProducts=procductList;
    newShipment.transporterList=transprterList;
    newShipment.Destination=destination;
    newShipment.Customer = factory.newRelationship('org.ebpi.blockathon', 'Orderer', costID);
    newShipment.Supplier =factory.newRelationship('org.ebpi.blockathon', 'Manufacturer', manuID);
    newShipment.Weight= originalWeight;
    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (shipmentRegistry) {
            return shipmentRegistry.add(newShipment)
        })

    }
