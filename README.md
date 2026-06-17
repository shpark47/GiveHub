# GiveHub

**GiveHub**는 기부 및 후원 관리 시스템을 위한 웹 애플리케이션입니다. 본 프로젝트는 Spring Boot를 기반으로 하며, 사용자 관리, 기부 활동, 뉴스, 결제 등의 핵심 기능을 제공합니다.

## 👥 팀 소개
총 5명의 팀원으로 구성되어 있으며, 효율적인 개발을 위해 역할을 분담하여 프로젝트를 진행합니다.

| 이름 | 프로필 |
| :--- | :--- |
| **박성훈** | https://github.com/shpark47 |
| **박래오** | https://github.com/desertdevv |
| **안효빈** | https://github.com/forqls |
| **손동준** | https://github.com/SonDong22 |
| **윤정호** | https://github.com/yoon-jeong-ho15 |

## 🛠 주요 기술 스택
* **Backend**: Java, Spring Boot
* **Database**: MyBatis, XML Mappers
* **Frontend**: HTML, CSS, JavaScript
* **Build Tool**: Gradle

## 📂 프로젝트 구조
```text
GiveHub/
├── src/main/java/kh/GiveHub/
│   ├── common/           # 공통 설정 (Mail, Security, WebMvc)
│   ├── donation/         # 기부 관련 기능
│   ├── image/            # 이미지 처리 기능
│   ├── mail/             # 이메일 발송 서비스
│   ├── member/           # 회원 관리 시스템
│   ├── news/             # 뉴스 및 게시판 관리
│   └── payment/          # 결제 시스템
└── src/main/resources/
    ├── mappers/          # MyBatis 매퍼 XML 파일
    └── static/           # 정적 자원 (CSS, 이미지 등)
