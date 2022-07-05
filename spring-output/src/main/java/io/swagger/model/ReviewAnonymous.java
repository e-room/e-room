package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReviewAnonymous
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-07-05T06:53:15.751Z[GMT]")


public class ReviewAnonymous   {
  @JsonProperty("isAnonymous")
  private Boolean isAnonymous = null;

  @JsonProperty("anonymousName")
  private String anonymousName = null;

  public ReviewAnonymous isAnonymous(Boolean isAnonymous) {
    this.isAnonymous = isAnonymous;
    return this;
  }

  /**
   * Get isAnonymous
   * @return isAnonymous
   **/
  @Schema(description = "")
  
    public Boolean isIsAnonymous() {
    return isAnonymous;
  }

  public void setIsAnonymous(Boolean isAnonymous) {
    this.isAnonymous = isAnonymous;
  }

  public ReviewAnonymous anonymousName(String anonymousName) {
    this.anonymousName = anonymousName;
    return this;
  }

  /**
   * Get anonymousName
   * @return anonymousName
   **/
  @Schema(description = "")
  
    public String getAnonymousName() {
    return anonymousName;
  }

  public void setAnonymousName(String anonymousName) {
    this.anonymousName = anonymousName;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewAnonymous reviewAnonymous = (ReviewAnonymous) o;
    return Objects.equals(this.isAnonymous, reviewAnonymous.isAnonymous) &&
        Objects.equals(this.anonymousName, reviewAnonymous.anonymousName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isAnonymous, anonymousName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewAnonymous {\n");
    
    sb.append("    isAnonymous: ").append(toIndentedString(isAnonymous)).append("\n");
    sb.append("    anonymousName: ").append(toIndentedString(anonymousName)).append("\n");
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
