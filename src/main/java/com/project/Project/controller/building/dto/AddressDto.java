package com.project.Project.controller.building.dto;

import com.project.Project.domain.embedded.Address;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Builder
public class AddressDto {

    /**
     * 시도
     */
    @NotBlank
    @Size(max=20)
    private String siDo;

    /**
     * 시군구
     */
    @NotBlank
    @Size(max=20)
    private String siGunGu;

    /**
     * 읍면
     */
    @NotNull
    @Size(max=20)
    private String eupMyeon;

    /**
     * 도로명
     */
    @NotBlank
    @Size(max=20)
    private String roadName;

    @NotBlank
    @Size(max=20)
    private String buildingNumber;


    @Override
    public String toString() {
        return  siDo + " " +  siGunGu + " " + eupMyeon +  " " +  roadName + " " ;
    }

    public static Address toAddress(AddressDto addressDto){
        return Address.builder()
                .siDo(addressDto.getSiDo())
                .siGunGu(addressDto.getSiGunGu())
                .eupMyeon(addressDto.getEupMyeon())
                .roadName(addressDto.getRoadName())
                .buildingNumber(addressDto.getBuildingNumber())
                .build();
    }
}
