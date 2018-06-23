/**
 * Sample transaction
 * @param {org.ebpi.blockathon.sendShipment} sendShipment
 * @transaction
 *
 */
function doSendShipment(sendShipment) {
    // const manuID = getCurrentParticipant().PartnerID;
    // const hash = sendShipment.DocumentHash;
    // const docLoc = sendShipment.DocumentLocation;
    // const procductList = sendShipment.ShippedProducts;
    // const transprterList = sendShipment.transporterList;
    // const destination = sendShipment.Destination;
    // const costumer = sendShipment.Customer;
    // const costID = costumer.PartnerID;
    // const originalWeight = sendShipment.Weight;
    // const clientReference = sendShipment.ClientReference
    let factory = getFactory();
    const identifier = uuidv4();
    let newShipment = factory.newResource('org.ebpi.blockathon', 'Shipment', identifier);
    newShipment.DocumentHash = sendShipment.DocumentHash;
    newShipment.ClientReference = sendShipment.ClientReference
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
    newHandover.final = startHandover.final
    var ontvangenShipment = startHandover.shipment
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
    var ikke =getCurrentParticipant()
    var ontvangenShipment = acceptHandover.shipment
    var vorigeOwner = acceptHandover.previousHandler
    var status = acceptHandover.state

    var deArray = ontvangenShipment.handoverArray
    var deHandover = deArray[deArray.length - 1]
    if (deHandover.giving == vorigeOwner && deHandover.reciever == ikke) {
        confirmed = true
    }
    else {
        confirmed = false
    }

    if (acceptHandover.status && deHandover.status){

    }
    else {
        deHandover.final = false
    }
    deHandover.stateReciever = status
    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (ShipmentAssetRegistry) {
            return ShipmentAssetRegistry.update(ontvangenShipment);

        })


}


function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        var output = v.toString(16);

        return output;
    });
}
    
