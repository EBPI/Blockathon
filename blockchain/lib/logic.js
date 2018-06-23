/**
 * Sample transaction
 * @param {org.ebpi.blockathon.sendShipment} sendShipment
 * @transaction
 *
 */
function doSendShipment(sendShipment) {
    const manufacturer = getCurrentParticipant();
    const manuID = manufacturer.PartnerID;
    const hash = sendShipment.DocumentHash;
    const docLoc = sendShipment.DocumentLocation;
    const procductList = sendShipment.ShippedProducts;
    const transprterList = sendShipment.transporterList;
    const destination = sendShipment.Destination;
    const costumer = sendShipment.Customer;
    const costID = costumer.PartnerID;
    const originalWeight = sendShipment.Weight;
    const clientRefrerence= sendShipment.ClientRefrerence
    let factory = getFactory();
    const identifier = uuidv4();
    let newShipment = factory.newResource('org.ebpi.blockathon', 'Shipment', identifier);
    newShipment.DocumentHash = hash;
    newShipment.ClientRefrerence=clientRefrerence
    newShipment.DocumentLocation = docLoc;
    newShipment.ShippedProducts = procductList;
    newShipment.transporterList = transprterList;
    newShipment.Destination = destination;
    newShipment.Customer = factory.newRelationship('org.ebpi.blockathon', 'Orderer', costID);
    newShipment.Supplier = factory.newRelationship('org.ebpi.blockathon', 'Manufacturer', manuID);
    newShipment.Weight = originalWeight;
    newShipment.ShipmentID = identifier;
    newShipment.handoverArray = [];

    return getAssetRegistry('org.ebpi.blockathon.Shipment')
        .then(function (ShipmentRegistry) {
             ShipmentRegistry.add(newShipment) .then (function () { return identifier } )
        })

}


/**
 * Sample transaction
 * @param {org.ebpi.blockathon.startHandover} startHandover
 * @transaction
 */
function doStartHandover(startHandover) {
    const HandoverID = uuidv4()
    let newHandover = factory.newResource('org.ebpi.blockathon', 'Handover', HandoverID);
    const givingParty = getCurrentParticipant();
    const givingId = givingParty.PartnerID;
    newHandover.giving = factory.newRelationship('org.ebpi.blockathon', 'TradePartner', givingId);

    const recivingParty = startHandover.nextHandler;
    const recivingId = recivingParty.PartnerID;
    newHandover.reciever = factory.newRelationship('org.ebpi.blockathon', 'TradePartner', recivingId);
    newHandover.confirmed = false
    newHandover.stateGiving = startHandover.state
    newHandover.stateReciever = ShipmentState.undefined
    return getAssetRegistry('org.ebpi.blockathon.Aanlevering')
        .then(function (AanleveringAssetRegistry) {
            return AanleveringAssetRegistry.get(FinalStatus.Kenmerk);
        })
        .then(function(Aanlevering) {
            Aanlevering.status.push(FinalStatus.newStatus)
            return getAssetRegistry('org.ebpi.hackathon.Aanlevering')
                .then(function (AanleveringAssetRegistry) {
                    return AanleveringAssetRegistry.update(Aanlevering);
                })
        })
}



/**
 * Sample transaction
 * @param {org.ebpi.blockathon.acceptHandover} acceptHandover
 * @transaction
 */
function doAcceptShipment(acceptHandover) {
    acceptShipment.shipment
    acceptShipment.state
}


function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        var output = v.toString(16);

        return output;
    });
}
    
