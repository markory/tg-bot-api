package de.markory.notibot.response.types;

public class Chat {
	
	public enum Type {
		group_chat,
		user_chat;
	}

	protected Type chatType;
	
	protected Chat(Type type) {
		this.chatType = type;
	}
	
	public Type getChatType() { return chatType; }
}
