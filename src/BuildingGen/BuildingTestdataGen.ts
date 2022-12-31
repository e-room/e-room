import axios, {AxiosResponse} from "axios";

const apiUrl = `https://business.juso.go.kr/addrlink/addrLinkApi.do`
const confmKey = `devU01TX0FVVEgyMDIyMTAxNTIzNTcyODExMzA2Njk=`
const currentPage = 1
const countPerPage = 100
const resultType = `json`


class AddressDto {
    sido!: string | null
    siGunGu!: string | null
    eupMyeon!: string | null
    roadName!: string | null
    buildingNumber!: string | null
}

class RoadAddrAPIResponseDto {
    detBdNmList!: string | null
    engAddr!: string | null
    rn!: string | null
    emdNm!: string | null
    zipNo!: string | null
    roadAddrPart2!: string | null
    emdNo!: string | null
    sggNm!: string | null
    jibunAddr!: string | null
    siNm!: string | null
    roadAddrPart1!: string | null
    bdNm!: string | null
    admCd!: string | null
    udrtYn!: string | null
    lnbrMnnm!: string | null
    roadAddr!: string | null
    lnbrSlno!: string | null
    buldMnnm!: string | null
    bdKdcd!: string | null
    liNm!: string | null
    rnMgtSn!: string | null
    mtYn!: string | null
    bdMgtSn!: string | null
    buldSlno!: string | null
}

function requestToAddrAPI() {
    return async (keyword: string): Promise<AxiosResponse> => {
        try {
            return axios({
                url: apiUrl,
                method: 'get',
                params: {
                    confmKey: confmKey,
                    currentPage: currentPage,
                    countPerPage: countPerPage,
                    resultType: resultType,
                    keyword: keyword
                }
            })
        } catch (error) {
            throw new Error(`Error: ${error}`)
        }
    }
}

function requestToBuildingGen() {
    return async (address: AddressDto): Promise<AxiosResponse> => {
        try {
            return axios({
                url: 'http://43.200.50.204:8080/building/',
                method: 'post',
                data: {
                    addressDto: {
                        siDo: address.sido,
                        siGunGu: address.siGunGu,
                        eupMyeon: address.eupMyeon,
                        roadName: address.roadName,
                        buildingNumber: address.buildingNumber
                    },
                    buildingOptionalDto: {
                        buildingName: null,
                        hasElevator: true
                    }

                }
            })
        } catch (error) {
            throw new Error(`Error: ${error}`)
        }
    }
}

const delay = (ms: number) => {
    return new Promise((resolve) => setTimeout(() => {
        resolve(ms);
    }, ms))
}

async function main() {
    const keyword = '영통구'
    try {
        const buildingList = (await requestToAddrAPI()(keyword)).data?.results?.juso
        const addressList = buildingList.map((building: RoadAddrAPIResponseDto) => {
            console.log(JSON.stringify(building, null, 2));
            const address: AddressDto = new AddressDto()
            address.sido = building.siNm
            address.siGunGu = building.sggNm
            address.eupMyeon = building.emdNm
            address.roadName = building.rn
            address.buildingNumber = building.buldSlno == "0" ? building.buldMnnm : building.buldMnnm + '-' + building.buldSlno
            return address
        })
        for (const address of addressList) {
            try {
                const response = await requestToBuildingGen()(address)
                const building = response.data
                console.log(JSON.stringify(building, null, 2))
                // await delay(1000);
            } catch (e: Error | any) {
                console.error(e)
            }
        }
    } catch (e) {
        console.error(e)
    }
}

main()
    .then()
    .catch(err => console.error(err))
    .finally(() => process.exit())