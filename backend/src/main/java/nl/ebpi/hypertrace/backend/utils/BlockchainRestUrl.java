package nl.ebpi.hypertrace.backend.utils;

public enum BlockchainRestUrl {

	PRODUCT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/api/org.ebpi.blockathon.Product/{id}"),
	PRODUCTS("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/api/org.ebpi.blockathon.Product"),
	SEND_SHIPMENT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT_NIKE + "/api/org.ebpi.blockathon.sendShipment"),
	QUERY_SHIPMENT_ON_ORDERID("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT_NIKE + "/api/org.ebpi.blockathon.Shipment?filter={query}"),
	QUERY_SHIPMENT_ON_TRANSPORTERID("http://192.168.0.103:3000/api/queries/Q17?SearchPartner={query}");
	private final String url;

	private BlockchainRestUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	private class Constants {
		private static final String IP_ADDRESS = "192.168.0.103";
		private static final String PORT = "3000";
		private static final String PORT_NIKE = "3001";
	}
}
