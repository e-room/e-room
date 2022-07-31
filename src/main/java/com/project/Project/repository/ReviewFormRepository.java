package com.project.Project.repository;

import com.project.Project.domain.review.ReviewForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewFormRepository extends JpaRepository<ReviewForm, Long> {
}
