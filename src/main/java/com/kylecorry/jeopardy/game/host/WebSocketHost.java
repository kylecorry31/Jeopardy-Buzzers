package com.kylecorry.jeopardy.game.host;

import com.kylecorry.jeopardy.game.WebSocketMessages;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Objects;

/**
 * A host using a web socket
 */
public class WebSocketHost implements Host {

    private Session session;
    private HostState state;

    /**
     * Default constructor
     * @param session the web socket session
     */
    public WebSocketHost(Session session) {
        this.session = Objects.requireNonNull(session);
        this.state = HostState.DISCONNECTED;
    }

    @Override
    public void setState(HostState state) {
        this.state = state;
        switch (state){
            case UNLOCKED:
                send(WebSocketMessages.HOST_UNLOCKED);
                break;
            case LOCKED:
                send(WebSocketMessages.HOST_LOCKED);
                break;
            case PLAYER_BUZZED_IN:
                send(WebSocketMessages.HOST_PLAYER_BUZZED_IN);
                break;
        }
    }

    @Override
    public HostState getState() {
        if (state != HostState.DISCONNECTED){
            send(WebSocketMessages.PING);
        }
        return state;
    }

    private void send(String message){
        try {
            if (!session.isOpen()){
                return;
            }
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, session);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof WebSocketHost))
            return false;
        WebSocketHost host = (WebSocketHost) o;
        return Objects.equals(session, host.session);
    }

    @Override
    public String toString() {
        return "Web Socket Host (" + state + ")";
    }
}
