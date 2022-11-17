# F-lab Project: calorie-counter

## 개요

* 본 프로젝트에서는 식단영양정보 서비스를 제공하는 시스템을 개발합니다.
* fatsecret 서비스는 기본적으로 사용자가 섭취한 음식을 입력하면 음식의 영양정보를 제공하는 서비스이며, 사용자들은 건강한 식단관리를 할 수있다는 장점이 있습니다.
* 서비스는 일반 이용자, 제공자, 관리자가 사용할 수 있습니다.

1. 일반 User: 일반회원이며 회원가입 후 서비스를 이용할 수 있다.
2. Provider: 식품에대한 세부 영양정보를 제공하는 전문가이며 서비스 관리자가 지정한다. 일반 이용자와 분리되는 별도의 가입절차를 거친다. 식단 정보를 신규 등록하고 삭제할 수 있다. 또한, 다른 Provider의 식단 정보 내용을 등록 심사할 수 있다.
3. Admin은 Provider의 가입 심사 제출서류를 확인하고, 가입 심사를 승인하거나 거절할 수 있다. 또한, 일반 사용자(User)와 Provider에 대한 약관 위반자를 강제 탈퇴할 수 있다. 식단정보가 부적절한 경우에 한하여 삭제할 수 있다.

## 기술 스택

* JDK 17
* Spring Boot 2.7.5
* MyBatis 3
* MySQL 8.0
* Gradle
* IntelliJ

## ERD (예시, 현재 논의 중)
https://www.erdcloud.com/d/thuYe7sMXNEzk7MPp

## 메뉴 구조도
![image](https://user-images.githubusercontent.com/106227771/196656935-e6472fc0-eba6-475d-b8f1-021e38725fe5.png)
![에프랩 v2 001](https://user-images.githubusercontent.com/113809660/197950013-517b2c5d-b651-4614-8f3f-e0b8ee40c16b.jpeg)
![에프랩 v2 002](https://user-images.githubusercontent.com/113809660/197950022-d8b58a99-4b86-4c1e-b260-4e9299589d8b.jpeg)

## 유저 시나리오 및 플로우 차트
[Wiki 참고](https://github.com/f-lab-edu/calorie-counter/wiki/%EC%9C%A0%EC%A0%80-%EC%8B%9C%EB%82%98%EB%A6%AC%EC%98%A4)
