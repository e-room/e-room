package com.project.Project.repository;

import com.project.Project.domain.enums.DTypeEnum;
import com.project.Project.domain.enums.KeywordEnum;
import com.project.Project.domain.review.ReviewKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {

    @Query("select distinct rk from ReviewKeyword rk where rk.keywordType = :type and rk.dType = :dType")
    Optional<ReviewKeyword> findByTypeAndDType(@Param("type") KeywordEnum type, @Param("dType") DTypeEnum dType);

    @Query("select distinct rk from ReviewKeyword rk where rk.keywordType = :type")
    List<ReviewKeyword> findByType(@Param("type") KeywordEnum type);

    @Query("select distinct rk from ReviewKeyword rk where rk.dType = :dType")
    List<ReviewKeyword> findByDType(@Param("dType") DTypeEnum dType);
}
