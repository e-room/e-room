package com.project.Project.unit.controller.review.dto;

import com.project.Project.controller.building.dto.AddressDto;
import com.project.Project.controller.review.dto.ReviewBaseDto;
import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.controller.review.dto.ReviewResidencePeriodDto;
import com.project.Project.controller.review.dto.ReviewScoreDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ReviewRequestDtoTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void ReviewCreateDto_GOOD() { // todo : image multipart list
        // given
        String advantageDescription = "길지 풍부하게 보이는 곳이 밝은 있으랴? 싹이 있으며, 따뜻한 바이며, 일월과 청춘 때문이다. 스며들어 사랑의 끓는 무엇을 부패를 말이다. 자신과 품으며, 이상이 같지 천지는 위하여 인생에 속잎나고, 바이며, 말이다. 속잎나고, 그들의 피부가 영원히 얼마나 이것을 그리하였는가? 인간이 용감하고 영락과 품고 전인 있으랴? 실현에 심장의 동산에는 얼음에 기쁘며, 사는가 위하여서. 것이 스며들어 그들에게 불어 보내는 크고 인간의 품고 이 말이다. 품으며, 하는 찬미를 봄바람을 피가 하는 그들을 그러므로 봄바람이다. 그들에게 목숨을 가슴이 무엇을 행복스럽고 것이다.";
        String disadvantageDescription = "길지 풍부하게 보이는 곳이 밝은 있으랴? 싹이 있으며, 따뜻한 바이며, 일월과 청춘 때문이다. 스며들어 사랑의 끓는 무엇을 부패를 말이다. 자신과 품으며, 이상이 같지 천지는 위하여 인생에 속잎나고, 바이며, 말이다. 속잎나고, 그들의 피부가 영원히 얼마나 이것을 그리하였는가? 인간이 용감하고 영락과 품고 전인 있으랴? 실현에 심장의 동산에는 얼음에 기쁘며, 사는가 위하여서. 것이 스며들어 그들에게 불어 보내는 크고 인간의 품고 이 말이다. 품으며, 하는 찬미를 봄바람을 피가 하는 그들을 그러므로 봄바람이다. 그들에게 목숨을 가슴이 무엇을 행복스럽고 것이다.";

        ReviewRequestDto.ReviewCreateDto reviewCreateDto = ReviewRequestDto.ReviewCreateDto.builder()
                .address(
                        AddressDto.builder().siDo("경기도")
                                .siGunGu("수원시 영통구")
                                .eupMyeon("")
                                .roadName("매영로425번길")
                                .buildingNumber("4")
                                .build()
                )
                .reviewResidencePeriodDto(ReviewResidencePeriodDto.builder().residenceStartYear(2020).residenceDuration(12).build())
                .reviewBaseDto(ReviewBaseDto.builder().deposit(800).monthlyRent(50).managementFee(10).netLeasableArea(13.0).build())
                .reviewScoreDto(ReviewScoreDto.builder().traffic(3.0).buildingComplex(4.0).surrounding(1.0).internal(5.0).livingLocation(3.0)
                        .residenceSatisfaction(5.0).build())
                .advantageKeywordList(List.of("PARKING", "VENTILATION", "TOWN_NOISE"))
                .advantageDescription(advantageDescription)
                .disadvantageKeywordList(List.of("MART_CONVENIENCE_STORE", "DAY_LIGHTING"))
                .disadvantageDescription(disadvantageDescription)
                .reviewImageList(new ArrayList<>())
                .build();

        // when
        Set<ConstraintViolation<ReviewRequestDto.ReviewCreateDto>> violations = validator.validate(reviewCreateDto);

        for (ConstraintViolation<ReviewRequestDto.ReviewCreateDto> violation : violations) {
            System.err.println(violation.getMessage());
        }

        // then
        assertEquals(0, violations.size());
    }

    @Test
    void ReviewCreateDto_BAD() {
        // given
        String advantageDescription = "메롱";
        String disadvantageDescription = "청춘 인간이 시들어 무한한 풀이 힘있다. 것은 그림자는 커다란 맺어, 그들은 것이다. 일월과 기관과 피가 보이는 소담스러운 하여도 날카로우나 것은 때문이다. 거선의 생의 착목한는 찾아 그들을 것이다.보라, 쓸쓸한 살 철환하였는가? 방지하는 아름답고 끓는 넣는 것이다. 미묘한 구할 청춘의 있는가? 되려니와, 굳세게 사랑의 쓸쓸하랴? 그들의 꾸며 얼음과 심장은 가슴이 얼마나 열락의 부패뿐이다. 되는 그들은 못할 피고, 하여도 천고에 새 인생에 것이다. 그들에게 그들의 따뜻한 실현에 봄바람이다.\n" +
                "\n" +
                "때에, 그들은 피고 그들은 산야에 것은 싹이 풀이 물방아 사막이다. 기관과 우리 이상 동산에는 그들은 청춘은 것이다. 가는 없으면 위하여서 튼튼하며, 그들의 이상의 얼마나 앞이 청춘의 봄바람이다. 사람은 산야에 목숨을 피다. 밝은 인간에 뭇 트고, 심장의 것이다. 이는 것은 수 얼마나 소금이라 사람은 하는 사막이다. 찾아다녀도, 보배를 그들의 이상의 우는 있는 용기가 어디 교향악이다. 주는 발휘하기 붙잡아 피다. 천자만홍이 오직 우리 가치를 우리는 그리하였는가? 가지에 아니더면, 청춘은 그것은 설레는 살았으며, 영락과 이것이다. 두기 행복스럽고 두손을 있는 얼음 불어 대고, 힘있다.\n" +
                "\n" +
                "청춘의 길을 별과 이것을 있다. 그들의 그들의 장식하는 있다. 있는 이상이 그와 부패뿐이다. 같은 풀이 그들은 품었기 튼튼하며, 것이다. 천하를 속에서 넣는 가는 간에 행복스럽고 얼음에 뿐이다. 품에 곧 옷을 같이 열매를 우리 위하여 품었기 말이다. 얼음과 노래하며 수 사는가 가는 것이다. 그것을 우리의 따뜻한 봄바람을 있는 위하여서, 청춘에서만 그것은 인간에 것이다. 관현악이며, 무엇을 못할 우리 할지니, 얼음과 없으면 열락의 피고 이것이다. 무엇을 하는 용감하고 이것은 온갖 약동하다. 속에서 불어 인간은 황금시대다.";

        List<MultipartFile> reviewImageList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            reviewImageList.add(new MultipartFile() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public String getOriginalFilename() {
                    return null;
                }

                @Override
                public String getContentType() {
                    return null;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public long getSize() {
                    return 0;
                }

                @Override
                public byte[] getBytes() throws IOException {
                    return new byte[0];
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return null;
                }

                @Override
                public void transferTo(File dest) throws IOException, IllegalStateException {

                }
            });
        }

        ReviewRequestDto.ReviewCreateDto reviewCreateDto = ReviewRequestDto.ReviewCreateDto.builder()
                .address(
                        AddressDto.builder()
                                .siDo("경기도")
                                .siGunGu("수원시 영통구")
                                .eupMyeon("")
                                .roadName("매영로425번길")
                                .buildingNumber("4")
                                .build()
                )
                .reviewResidencePeriodDto(ReviewResidencePeriodDto.builder().residenceStartYear(2045).residenceDuration(-1).build())
                .reviewBaseDto(ReviewBaseDto.builder().deposit(-800).monthlyRent(-50).managementFee(-10).netLeasableArea(-13.0).build())
                .reviewScoreDto(ReviewScoreDto.builder().traffic(5.5).buildingComplex(-1.0).surrounding(6.6).internal(5.1).livingLocation(-1.0)
                        .residenceSatisfaction(5.5).build())
                .advantageKeywordList(List.of("PARKING@", "VENTILATION~~", "앍TOWN_NOISE")) // 잘못된 Enum 값 3개
                .advantageDescription(advantageDescription) // 50자 미만
                .disadvantageKeywordList(List.of("MART_CONVENIENCE_STORE히히", "ㅁㅁ")) // 잘못된 Enum 값 2개
                .disadvantageDescription(disadvantageDescription) // 500자 초과
                .reviewImageList(reviewImageList) // 5개 이상의 이미지
                .build();


        // when
        Set<ConstraintViolation<ReviewRequestDto.ReviewCreateDto>> violations = validator.validate(reviewCreateDto);

        for (ConstraintViolation<ReviewRequestDto.ReviewCreateDto> violation : violations) {
            System.err.println(violation.getMessage() + " | actual : " + violation.getInvalidValue());
        }

        // then
        assertEquals(23, violations.size());
    }
}
