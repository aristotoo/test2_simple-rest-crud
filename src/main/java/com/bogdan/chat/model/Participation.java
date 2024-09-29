package com.bogdan.chat.model;

public class Participation {
    private User user;
    private ChatRoom chatRoom;

    private Participation(ParticipationBuilder builder){
        this.user = builder.user;
        this.chatRoom = builder.chatRoom;
    }
    //default constructor for ORM
    public Participation(){}

    public User getUser() {
        return user;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    @Override
    public String toString() {
        return "Participation{" +
                " user=" + user +
                ", chatRoom=" + chatRoom +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participation that)) return false;

        if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) return false;
        return getChatRoom() != null ? getChatRoom().equals(that.getChatRoom()) : that.getChatRoom() == null;
    }

    @Override
    public int hashCode() {
        int result = getUser() != null ? getUser().hashCode() : 0;
        result = 31 * result + (getChatRoom() != null ? getChatRoom().hashCode() : 0);
        return result;
    }

    public static class ParticipationBuilder {
        private User user;
        private ChatRoom chatRoom;

        public ParticipationBuilder setUser(User user) {
            this.user = user;
            return this;
        }

        public ParticipationBuilder setChatRoom(ChatRoom chatRoom) {
            this.chatRoom = chatRoom;
            return this;
        }

        public Participation build(){
            return new Participation(this);
        }
    }
}
