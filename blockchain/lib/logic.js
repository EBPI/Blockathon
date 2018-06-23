/**
 * Sample transaction
 * @param {org.ebpi.blockathon.sendShipment} sendShipment
 * @transaction
 *
 */
function doSendShipment(sendShipment) {
    let factory = getFactory();
    const identifier = uuidv4();
    let newShipment = factory.newResource('org.ebpi.blockathon', 'Shipment', identifier);
    newShipment.DocumentHash = sendShipment.DocumentHash;
    newShipment.ClientReference = sendShipment.ClientReference;
    newShipment.DocumentLocation = sendShipment.DocumentLocation;
    newShipment.ShippedProducts = sendShipment.ShippedProducts;
    newShipment.transporterList = sendShipment.transporterList;
    newShipment.Destination = sendShipment.Destination;
    newShipment.Customer = factory.newRelationship('org.ebpi.blockathon', 'Orderer', sendShipment.Customer.PartnerID);
    newShipment.Supplier = factory.newRelationship('org.ebpi.blockathon', 'Manufacturer', getCurrentParticipant().PartnerID);
    newShipment.Weight = sendShipment.Weight;
    newShipment.ShipmentID = identifier;
    newShipment.handoverArray = [];

    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (ShipmentRegistry) {
            ShipmentRegistry.add(newShipment).then(function () {
                return identifier
            })
        })
}


/**
 * Sample transaction
 * @param {org.ebpi.blockathon.startHandover} startHandover
 * @transaction
 */
function doStartHandover(startHandover) {
    const HandoverID = uuidv4()
    let factory = getFactory();
    let newHandover = factory.newResource('org.ebpi.blockathon', 'Handover', HandoverID);
    const givingParty = getCurrentParticipant();
    const givingId = givingParty.PartnerID;
    newHandover.giving = factory.newRelationship('org.ebpi.blockathon', 'TradePartner', givingId);

    const recivingParty = startHandover.nextHandler;
    const recivingId = recivingParty.PartnerID;
    newHandover.reciever = factory.newRelationship('org.ebpi.blockathon', 'TradePartner', recivingId);
    newHandover.confirmed = false
    newHandover.stateGiving = startHandover.state
    newHandover.stateReciever = "undefined"
    newHandover.finalHandover = startHandover.finalHandover
    var ontvangenShipment = startHandover.shipment
    ontvangenShipment.handoverArray.push(newHandover);
    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (ShipmentAssetRegistry) {
            return ShipmentAssetRegistry.update(ontvangenShipment);
        })
}


/**
 * Sample transaction
 * @param {org.ebpi.blockathon.acceptHandover} acceptHandover
 * @transaction
 */
function doAcceptHandover(acceptHandover) {
    var ikke = getCurrentParticipant()
    var ontvangenShipment = acceptHandover.shipment
    var vorigeOwner = acceptHandover.previousHandler
    var status = acceptHandover.state

    var deArray = ontvangenShipment.handoverArray
    var deHandover = deArray[deArray.length - 1]
    deHandover.confirmed =(deHandover.giving == vorigeOwner && deHandover.reciever == ikke)

    if (acceptHandover.finalHandover && deHandover.finalHandover) {
        ontvangenShipment.completed = true
        deHandover.finalHandover=true
    }
    else {
        deHandover.finalHandover = false
    }
    deHandover.stateReciever = status
    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (ShipmentAssetRegistry) {
            return ShipmentAssetRegistry.update(ontvangenShipment);
        })
}

/**
 * Sample transaction
 * @param {org.ebpi.blockathon.changeReputation} changeReputation
 * @transaction
 */
function doChangeReputation(changeReputation) {
var mutatedManufacturer = changeReputation.manufacturer;
if (changeReputation.good){mutatedManufacturer.Reputation++;}
else{mutatedManufacturer.Reputation--}
    return getParticipantRegistry('org.ebpi.blockathon.Manufacturer')
        .then(function (ManufacturerParticipantRegistry) {
            return ManufacturerParticipantRegistry.update(mutatedManufacturer);
        })
}




function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        var output = v.toString(16);

        return output;
    });
}
    
/**
 * Sample transaction
 * @param {org.ebpi.blockathon.deleteShipments} deleteShipments
 * @transaction
 */
function dodeleteShipments() {
    // The existing shipments 
var aShipment;
// Get the shipment asset registry.
return getAssetRegistry('org.ebpi.blockathon.Shipment')
  .then(function (shipmentAssetRegistry) {
    // Get the factory for creating new asset instances.
    var factory = getFactory();
    // Remove the shipments from the shipment asset registry. Note that
    // one vehicle is specified as a vehicle instance, and the other
    // vehicle is specified by the ID of the vehicle.
    return vehicleAssetRegistry.removeAll(aShipment);
  })
  .catch(function (error) {
    // Add optional error handling here.
  });
}
