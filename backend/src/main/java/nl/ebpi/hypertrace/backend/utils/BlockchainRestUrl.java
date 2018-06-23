package nl.ebpi.hypertrace.backend.utils;

public enum BlockchainRestUrl {

	PRODUCT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/api/org.ebpi.blockathon.Product/{id}"),
	PRODUCTS("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/api/org.ebpi.blockathon.Product"),
	ADD_SHIPMENT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/api/org.ebpi.blockathon.Shipment"),
	SEND_SHIPMENT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT_NIKE + "/api/org.ebpi.blockathon.sendShipment"),
	QUERY_SHIPMENT("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT_NIKE + "/api/org.ebpi.blockathon.Shipment?filter={query}");

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
