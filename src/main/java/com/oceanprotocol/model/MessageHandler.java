/**
 * Response class handle the messages from ocean network
 */
package com.oceanprotocol.model;

public class MessageHandler {
	String message;
	String fileContent;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	@Override
	public String toString() {
		return "MessageHandler [message=" + message + ", fileContent=" + fileContent + "]";
	}
	
}
