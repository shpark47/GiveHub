# GiveHub
기부허브
팀원 : 윤정호, 박성훈, 손동준, 안효빈, 박래오
프로젝트 설명 : 스프링부트를 이용해 여러 기부 사이트들을 벤치마킹해서 장단점을 보완 및 강화 후 차별화된 새로운 기부사이트를 만들어보는 프로젝트

# GiveHub

**GiveHub**는 기부 및 후원 관리 시스템을 위한 웹 애플리케이션입니다. 본 프로젝트는 Spring Boot를 기반으로 하며, 사용자 관리, 기부 활동, 뉴스, 결제 등의 핵심 기능을 제공합니다.

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

테이블 설계 (erd cloud 를 이용한 erd그리기)
![image](https://github.com/user-attachments/assets/39c8b299-1ab7-4789-8b95-987620c5c246)
