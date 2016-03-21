

import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/wst", configurator = GetHttpSessionConfigurator.class)
public class WS {
	private Session wsSession;
	private HttpSession httpSession;

	@OnOpen
	public void onOpen(Session session, EndpointConfig config) {
		this.wsSession = session;
		this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
	}

	@OnMessage
	public void onMessage(String message) {
		if (httpSession.getAttribute("time") == null) {
			sendMessage("Session time is null");
		} else {
			sendMessage(Long.toString((long) httpSession.getAttribute("time")));
		}
	}

	private void sendMessage(String message) {
		try {
			wsSession.getBasicRemote().sendText(message);
		} catch (Exception e) {
		}
	}
}
