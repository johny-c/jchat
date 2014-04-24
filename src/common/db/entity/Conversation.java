package common.db.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer clientGenId;
    private Integer serverGenId;
    private Integer starterUserId;
    private Set<Participant> participants = new HashSet<>();

    public Conversation() {
    }

    public Integer getClientGenId() {
        return clientGenId;
    }

    public void setClientGenId(Integer clientGenId) {
        this.clientGenId = clientGenId;
    }

    public Integer getServerGenId() {
        return serverGenId;
    }

    public void setServerGenId(Integer serverGenId) {
        this.serverGenId = serverGenId;
    }

    public Integer getStarterUserId() {
        return starterUserId;
    }

    public void setStarterUserId(Integer starterUserId) {
        this.starterUserId = starterUserId;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}
