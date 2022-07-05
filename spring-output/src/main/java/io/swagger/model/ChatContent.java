package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.ChatContent;
import io.swagger.model.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ChatContent
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class ChatContent   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("chatRoom")
  private ChatRoom chatRoom = null;

  @JsonProperty("parent")
  private ChatContent parent = null;

  @JsonProperty("readMsg")
  private Boolean readMsg = null;

  @JsonProperty("message")
  private String message = null;

  @JsonProperty("createdAt")
  private OffsetDateTime createdAt = null;

  public ChatContent id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(description = "")
  
    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ChatContent chatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
    return this;
  }

  /**
   * Get chatRoom
   * @return chatRoom
   **/
  @Schema(description = "")
  
    @Valid
    public ChatRoom getChatRoom() {
    return chatRoom;
  }

  public void setChatRoom(ChatRoom chatRoom) {
    this.chatRoom = chatRoom;
  }

  public ChatContent parent(ChatContent parent) {
    this.parent = parent;
    return this;
  }

  /**
   * Get parent
   * @return parent
   **/
  @Schema(description = "")
  
    @Valid
    public ChatContent getParent() {
    return parent;
  }

  public void setParent(ChatContent parent) {
    this.parent = parent;
  }

  public ChatContent readMsg(Boolean readMsg) {
    this.readMsg = readMsg;
    return this;
  }

  /**
   * 읽었는지 여부를 확인하기 위한 기능
   * @return readMsg
   **/
  @Schema(description = "읽었는지 여부를 확인하기 위한 기능")
  
    public Boolean isReadMsg() {
    return readMsg;
  }

  public void setReadMsg(Boolean readMsg) {
    this.readMsg = readMsg;
  }

  public ChatContent message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   **/
  @Schema(description = "")
  
    public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ChatContent createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   **/
  @Schema(description = "")
  
    @Valid
    public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatContent chatContent = (ChatContent) o;
    return Objects.equals(this.id, chatContent.id) &&
        Objects.equals(this.chatRoom, chatContent.chatRoom) &&
        Objects.equals(this.parent, chatContent.parent) &&
        Objects.equals(this.readMsg, chatContent.readMsg) &&
        Objects.equals(this.message, chatContent.message) &&
        Objects.equals(this.createdAt, chatContent.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, chatRoom, parent, readMsg, message, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatContent {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    chatRoom: ").append(toIndentedString(chatRoom)).append("\n");
    sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
    sb.append("    readMsg: ").append(toIndentedString(readMsg)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
