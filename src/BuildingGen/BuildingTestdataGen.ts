import axios, {AxiosResponse} from "axios";

const apiUrl = `https://business.juso.go.kr/addrlink/addrLinkApi.do`
const confmKey = `devU01TX0FVVEgyMDIzMDIyODIyNDI1MjExMzU1NDM=`
let currentPage = 1
const countPerPage = 20
const resultType = `json`


interface commonResults {
    errorMessage: string,
    countPerPage: string,
    totalCount: string,
    errorCode: string,
    currentPage: string
}


interface justApiInterface {
    results: {
        common: commonResults
    },
    juso: RoadAddrAPIResponseDto[]
}


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

function getTotalCountOfQuery() {
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

function requestToAddrAPI() {
    return async (keyword: string): Promise<AxiosResponse> => {
        try {
            console.log(currentPage)
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
                url: 'http://localhost:8080/building/',
                method: 'post',
                headers: {
                    mocking: 65
                },
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
                        hasElevator: false
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

async function batchInsert(keyword: string) {
    try {
        const buildingList = (await requestToAddrAPI()(keyword)).data?.results?.juso
        const addressList = buildingList.map((building: RoadAddrAPIResponseDto) => {
            // console.log(JSON.stringify(building, null, 2));
            const address: AddressDto = new AddressDto()
            address.sido = building.siNm
            address.siGunGu = building.sggNm
            address.eupMyeon = building.emdNm
            address.roadName = building.rn
            address.buildingNumber = building.buldSlno == "0" ? building.buldMnnm : building.buldMnnm + '-' + building.buldSlno
            return address
        })
        const promises: Promise<any>[] = []
        addressList.map((address: AddressDto) => {
            try {
                const response = requestToBuildingGen()(address)
                promises.push(response)
            } catch (e: Error | any) {
                console.error(e)
            }
        })
        await Promise.all(promises)
          .then((result) => {
              console.log(`batch complete ${currentPage}`)
          })
          .catch((err) => console.error(err))
    } catch (e) {
        console.error(e)
    }
}

async function main() {
    const keywordList = [
        '대전광역시 서구 복수동',
        '대전광역시 서구 도마1동',
        '대전광역시 서구 도마2동',
        '대전광역시 서구 정림동',
        '대전광역시 서구 변동',
        '대전광역시 서구 용문동',
        '대전광역시 서구 탄방동',
        '대전광역시 서구 둔산1동',
        '대전광역시 서구 둔산2동',
        '대전광역시 서구 둔산3동',
        '대전광역시 서구 괴정동',
        '대전광역시 서구 가장동',
        '대전광역시 서구 내동',
        '대전광역시 서구 갈마1동',
        '대전광역시 서구 갈마2동',
        '대전광역시 서구 월평1동',
        '대전광역시 서구 월평2동',
        '대전광역시 서구 월평3동',
        '대전광역시 서구 만년동',
        '대전광역시 서구 가수원동',
        '대전광역시 서구 도안동',
        '대전광역시 서구 관저1동',
        '대전광역시 서구 관저2동',
        '대전광역시 서구 기성동',
        '대전광역시 유성구 진잠동',
        '대전광역시 유성구 학하동',
        '대전광역시 유성구 원신흥동',
        '대전광역시 유성구 상대동',
        '대전광역시 유성구 온천1동',
        '대전광역시 유성구 온천2동',
        '대전광역시 유성구 노은1동',
        '대전광역시 유성구 노은2동',
        '대전광역시 유성구 노은3동',
        '대전광역시 유성구 신성동',
        '대전광역시 유성구 전민동',
        '대전광역시 유성구 구즉동',
        '대전광역시 유성구 관편동',
        '대전광역시 대덕구 오정동',
        '대전광역시 대덕구 대화동',
        '대전광역시 대덕구 회덕동',
        '대전광역시 대덕구 비래동',
        '대전광역시 대덕구 송촌동',
        '대전광역시 대덕구 중리동',
        '대전광역시 대덕구 법1동',
        '대전광역시 대덕구 법2동',
        '대전광역시 대덕구 신탄진동',
        '대전광역시 대덕구 석봉동',
        '대전광역시 대덕구 덕암동',
        '대전광역시 대덕구 목상동',
    ]
    for (let l = 0; l < keywordList.length; l++) {
        const keyword = keywordList[l]
        console.log(keyword)
        const totalCountResponse: justApiInterface = (await getTotalCountOfQuery()(keyword)).data
        const totalCount = Number(totalCountResponse.results.common.totalCount)
        console.log(totalCount)


        for (let i = 1; i < Math.ceil(totalCount / countPerPage) + 1; i++) {
            currentPage = i
            await batchInsert(keyword)
        }
    }
}


main()
  .then()
  .catch(err => console.error(err))
  .finally(() => process.exit())