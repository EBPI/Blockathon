package nl.ebpi.hypertrace.backend.utils;

public enum FrontEndUrl {

	RECEIVER_QRCODE("http://" + Constants.IP_ADDRESS + ":" + Constants.PORT + "/manufacturer?receiver={receiver}&shipment={shipment}");

	private final String url;

	private FrontEndUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	private class Constants {
		private static final String IP_ADDRESS = "www.hypertrace.nl";
		private static final String PORT = "8090";
	}
}
