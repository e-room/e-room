## E-Room

🏠 E-Room addresses the issue of information inequality that arises when young professionals search for housing.

[https://www.e-room.app/](https://www.e-room.app/)

| Position | Name | Github |
| --- | --- | --- |
| 💻 Server Developer | Hyuktae Kwon | [LarryKwon](https://github.com/LarryKwon) |
| 🎨 UI/UX Designer | Heebum Jeon | [Heebum Jeon](https://github.com/hbnhb) |
| 🎨 Graphic Designer | Bokwang Jeong | [jeongbokoang](https://github.com/jeongbokoang) |
| 💻 Server Developer | Seonghoon Jeong | [SeongHoon Jeong](https://github.com/jeongbokoangswa07016) |
| 💻 Front-End Developer | Hyojin Jung | [HYOJIN JUNG](https://github.com/injulme) |
<br>

## Project Architecture

![E-room PA](https://github.com/e-room/e-room/assets/53550707/60e593f1-2ee2-422e-916d-ca25c4d574be)

- A CI/CD pipeline has been established using GitHub Actions & Elastic Beanstalk.
- Development and production servers are separated, with the development server utilizing rolling deployment and the production server using zero-downtime deployment.
- The development server is deployed when a push is made to the `develop` branch, while the production server is deployed when a pull request is merged into the `master` branch.
- FE Repository: [e-room-web](https://github.com/e-room/e-room-web)
<br>

## ERD

![E-room ERD](https://github.com/e-room/e-room/assets/53550707/fe1e3461-cede-4edb-8336-da38fc614384)

<br>

## Tech Stack

- Java
- Spring Boot
- MySQL
- Gradle
- JPA, QueryDSL
- AWS - Elastic Beanstalk, RDS, S3, Route53, Certificate Manager, …
- GitHub Actions
<br>

## Branching Strategy

- `master` ← The branch deployed to the production server
- `release` ← The branch containing features for the next release
- `develop` ← The branch deployed to the development server
- `feature` ← Created in the format `issue/issue-number` for assigned tasks related to the issue
