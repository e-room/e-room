package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.Member;
import io.swagger.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Room
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class Room   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("reviewList")
  @Valid
  private List<Review> reviewList = null;

  @JsonProperty("memberList")
  @Valid
  private List<Member> memberList = null;

  public Room id(Long id) {
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

  public Room reviewList(List<Review> reviewList) {
    this.reviewList = reviewList;
    return this;
  }

  public Room addReviewListItem(Review reviewListItem) {
    if (this.reviewList == null) {
      this.reviewList = new ArrayList<Review>();
    }
    this.reviewList.add(reviewListItem);
    return this;
  }

  /**
   * Get reviewList
   * @return reviewList
   **/
  @Schema(description = "")
      @Valid
    public List<Review> getReviewList() {
    return reviewList;
  }

  public void setReviewList(List<Review> reviewList) {
    this.reviewList = reviewList;
  }

  public Room memberList(List<Member> memberList) {
    this.memberList = memberList;
    return this;
  }

  public Room addMemberListItem(Member memberListItem) {
    if (this.memberList == null) {
      this.memberList = new ArrayList<Member>();
    }
    this.memberList.add(memberListItem);
    return this;
  }

  /**
   * Get memberList
   * @return memberList
   **/
  @Schema(description = "")
      @Valid
    public List<Member> getMemberList() {
    return memberList;
  }

  public void setMemberList(List<Member> memberList) {
    this.memberList = memberList;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Room room = (Room) o;
    return Objects.equals(this.id, room.id) &&
        Objects.equals(this.reviewList, room.reviewList) &&
        Objects.equals(this.memberList, room.memberList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, reviewList, memberList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Room {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    reviewList: ").append(toIndentedString(reviewList)).append("\n");
    sb.append("    memberList: ").append(toIndentedString(memberList)).append("\n");
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
