package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.Member;
import io.swagger.model.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Building
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class Building   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("roomList")
  @Valid
  private List<Room> roomList = null;

  @JsonProperty("owner")
  @Valid
  private List<Member> owner = null;

  public Building id(Long id) {
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

  public Building roomList(List<Room> roomList) {
    this.roomList = roomList;
    return this;
  }

  public Building addRoomListItem(Room roomListItem) {
    if (this.roomList == null) {
      this.roomList = new ArrayList<Room>();
    }
    this.roomList.add(roomListItem);
    return this;
  }

  /**
   * Get roomList
   * @return roomList
   **/
  @Schema(description = "")
      @Valid
    public List<Room> getRoomList() {
    return roomList;
  }

  public void setRoomList(List<Room> roomList) {
    this.roomList = roomList;
  }

  public Building owner(List<Member> owner) {
    this.owner = owner;
    return this;
  }

  public Building addOwnerItem(Member ownerItem) {
    if (this.owner == null) {
      this.owner = new ArrayList<Member>();
    }
    this.owner.add(ownerItem);
    return this;
  }

  /**
   * Get owner
   * @return owner
   **/
  @Schema(description = "")
      @Valid
    public List<Member> getOwner() {
    return owner;
  }

  public void setOwner(List<Member> owner) {
    this.owner = owner;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Building building = (Building) o;
    return Objects.equals(this.id, building.id) &&
        Objects.equals(this.roomList, building.roomList) &&
        Objects.equals(this.owner, building.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, roomList, owner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Building {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    roomList: ").append(toIndentedString(roomList)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
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
