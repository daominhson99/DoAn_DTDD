package chatweb;

public class Message {
    private int id;
    private String sender;
    private String content;

    public Message(int id, String sender, String content) {
        this.id = id;
        this.sender = sender;
        this.content = content;
    }

    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getContent() { return content; }
}
