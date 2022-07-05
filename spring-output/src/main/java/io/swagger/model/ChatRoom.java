package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.ChatContent;
import io.swagger.model.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ChatRoom
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class ChatRoom   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("host")
  private Member host = null;

  @JsonProperty("guest")
  private Member guest = null;

  @JsonProperty("lastMessage_id")
  private Long lastMessageId = null;

  @JsonProperty("chatContentList")
  @Valid
  private List<ChatContent> chatContentList = null;

  @JsonProperty("updatedAt")
  private OffsetDateTime updatedAt = null;

  public ChatRoom id(Long id) {
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

  public ChatRoom host(Member host) {
    this.host = host;
    return this;
  }

  /**
   * Get host
   * @return host
   **/
  @Schema(description = "")
  
    @Valid
    public Member getHost() {
    return host;
  }

  public void setHost(Member host) {
    this.host = host;
  }

  public ChatRoom guest(Member guest) {
    this.guest = guest;
    return this;
  }

  /**
   * Get guest
   * @return guest
   **/
  @Schema(description = "")
  
    @Valid
    public Member getGuest() {
    return guest;
  }

  public void setGuest(Member guest) {
    this.guest = guest;
  }

  public ChatRoom lastMessageId(Long lastMessageId) {
    this.lastMessageId = lastMessageId;
    return this;
  }

  /**
   * 가장 최근 메시지를 나타내는 id
   * @return lastMessageId
   **/
  @Schema(description = "가장 최근 메시지를 나타내는 id")
  
    public Long getLastMessageId() {
    return lastMessageId;
  }

  public void setLastMessageId(Long lastMessageId) {
    this.lastMessageId = lastMessageId;
  }

  public ChatRoom chatContentList(List<ChatContent> chatContentList) {
    this.chatContentList = chatContentList;
    return this;
  }

  public ChatRoom addChatContentListItem(ChatContent chatContentListItem) {
    if (this.chatContentList == null) {
      this.chatContentList = new ArrayList<ChatContent>();
    }
    this.chatContentList.add(chatContentListItem);
    return this;
  }

  /**
   * 채팅 내용
   * @return chatContentList
   **/
  @Schema(description = "채팅 내용")
      @Valid
    public List<ChatContent> getChatContentList() {
    return chatContentList;
  }

  public void setChatContentList(List<ChatContent> chatContentList) {
    this.chatContentList = chatContentList;
  }

  public ChatRoom updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   **/
  @Schema(description = "")
  
    @Valid
    public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChatRoom chatRoom = (ChatRoom) o;
    return Objects.equals(this.id, chatRoom.id) &&
        Objects.equals(this.host, chatRoom.host) &&
        Objects.equals(this.guest, chatRoom.guest) &&
        Objects.equals(this.lastMessageId, chatRoom.lastMessageId) &&
        Objects.equals(this.chatContentList, chatRoom.chatContentList) &&
        Objects.equals(this.updatedAt, chatRoom.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, host, guest, lastMessageId, chatContentList, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChatRoom {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    host: ").append(toIndentedString(host)).append("\n");
    sb.append("    guest: ").append(toIndentedString(guest)).append("\n");
    sb.append("    lastMessageId: ").append(toIndentedString(lastMessageId)).append("\n");
    sb.append("    chatContentList: ").append(toIndentedString(chatContentList)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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
