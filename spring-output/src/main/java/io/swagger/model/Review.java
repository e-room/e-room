package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.ReviewAnonymous;
import io.swagger.model.ReviewForm;
import io.swagger.model.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Review
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class Review   {
  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("room")
  private Room room = null;

  @JsonProperty("reviewForm")
  private ReviewForm reviewForm = null;

  @JsonProperty("createdAt")
  private OffsetDateTime createdAt = null;

  @JsonProperty("anonymous")
  private ReviewAnonymous anonymous = null;

  public Review id(Long id) {
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

  public Review room(Room room) {
    this.room = room;
    return this;
  }

  /**
   * Get room
   * @return room
   **/
  @Schema(description = "")
  
    @Valid
    public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public Review reviewForm(ReviewForm reviewForm) {
    this.reviewForm = reviewForm;
    return this;
  }

  /**
   * Get reviewForm
   * @return reviewForm
   **/
  @Schema(description = "")
  
    @Valid
    public ReviewForm getReviewForm() {
    return reviewForm;
  }

  public void setReviewForm(ReviewForm reviewForm) {
    this.reviewForm = reviewForm;
  }

  public Review createdAt(OffsetDateTime createdAt) {
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

  public Review anonymous(ReviewAnonymous anonymous) {
    this.anonymous = anonymous;
    return this;
  }

  /**
   * Get anonymous
   * @return anonymous
   **/
  @Schema(description = "")
  
    @Valid
    public ReviewAnonymous getAnonymous() {
    return anonymous;
  }

  public void setAnonymous(ReviewAnonymous anonymous) {
    this.anonymous = anonymous;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Review review = (Review) o;
    return Objects.equals(this.id, review.id) &&
        Objects.equals(this.room, review.room) &&
        Objects.equals(this.reviewForm, review.reviewForm) &&
        Objects.equals(this.createdAt, review.createdAt) &&
        Objects.equals(this.anonymous, review.anonymous);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, room, reviewForm, createdAt, anonymous);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Review {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    room: ").append(toIndentedString(room)).append("\n");
    sb.append("    reviewForm: ").append(toIndentedString(reviewForm)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    anonymous: ").append(toIndentedString(anonymous)).append("\n");
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
