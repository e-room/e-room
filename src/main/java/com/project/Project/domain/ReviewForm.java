package com.project.Project.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PreRemove;

@Data
@SQLDelete(sql = "UPDATE review_form SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ReviewForm extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    /*
         거주지주소
         거주 유형(아파트 / 오피스텔 또는 원룸 빌라 주택)
         거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
         거주층 : 저층, 중층, 고층
         보증금 : 000만원
         월세 : 00만원
         관리비 : 몇호기준 얼마정도에요. 여름에는 에어컨을 틀면 추가적으로 ....
         5단계 선택(매우만족, 만족, 보통, 불만족, 매우 불만족)
            - 교통점수
            - 건물 및 단지 점수
            - 주변 및 환경 점수
            - 내부 점수
            - 생활 및 입지 점수
        장점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
                       단열 반려동물 키우기 방충 방음 엘레베이터 조용한동네 평지 마트/편의점 상가
        단점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
                        단열 반려동물 키우기 벌레 층간소음 엘레베이터 동네소음 언덕 마트/편의점 상가 학교/학원
        해당 거주지 만족도 : 별1개부터 5개까지 선택
     */
    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
