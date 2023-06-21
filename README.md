## E-Room

🏠 eroom은 사회초년생이 집을 구하면서 생기는 정보의 불평등 문제를 해결합니다.

[https://www.e-room.app/](https://www.e-room.app/)

| Position | Name | Github |
| --- | --- | --- |
| 💻 Server Developer | 권혁태 | [LarryKwon](https://github.com/LarryKwon) |
| 🎨 UI/UX Designer | 전희범 | [Heebum Jeon](https://github.com/hbnhb) |
| 🎨 Graphic Designer | 정보광 | [jeongbokoang](https://github.com/jeongbokoang) |
| 💻 Server Developer | 정성훈 | [SeongHoon Jeong](https://github.com/jeongbokoangswa07016) |
| 💻 Front-End Developer | 정효진 | [HYOJIN JUNG](https://github.com/injulme) |
<br>

## Project Architecture

![E-room PA](https://github.com/e-room/e-room/assets/53550707/d4a050fd-b22e-4cc1-8902-82526da49a61)

- Github Actions & Elastic Beanstalk을 활용해 CI/CD 파이프라인을 구축했습니다.
- 개발 서버와 운영 서버가 분리되어 있으며 개발 서버는 중단배포, 운영 서버는 무중단배포 채택하고 있습니다.
- 개발 서버는 develop 브랜치가 push되었을 때, 운영서버는 master 브랜치로 PR이 합쳐졌을 때를 트리거로 배포됩니다.
- FE Repository : [e-room-web](https://github.com/e-room/e-room-web)
<br>

## ERD

![E-room ERD](https://github.com/e-room/e-room/assets/53550707/fe1e3461-cede-4edb-8336-da38fc614384)

<br>

## Tech Stack

- Java
- SpringBoot
- MySQL
- Gradle
- JPA, QueryDSL
- AWS - Elastic Beanstalk, RDS, S3, Route53, Certificate Manager, …
- Github Actions
<br>

## Branching Strategy

- master ← 운영 서버에 배포되어있는 브랜치
- release ← 다음 릴리즈에 나갈 기능들이 포함되어있는 브랜치
- develop ← 개발 서버에 배포되어있는 브랜치
- feature ← issue/이슈넘버 형태로 생성하여 해당 이슈의 할당된 작업을 하는 브랜치
