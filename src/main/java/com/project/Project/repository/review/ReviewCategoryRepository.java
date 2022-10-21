package com.project.Project.repository.review;

import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewCategoryRepository extends JpaRepository<ReviewCategory, Long> {

    @Query("select distinct rc from ReviewCategory rc where rc.type = :type ")
    public Optional<ReviewCategory> findByType(@Param("type") ReviewCategoryEnum type);
}
