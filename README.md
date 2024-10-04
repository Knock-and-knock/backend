# 🚪 똑똑: Knock-knock

신한DS SW 아카데미 2차 팀 프로젝트

**🥇 신한DS SW 아카데미 6회차 최종프로젝트 최우수상 수상작**

![슬라이드1](https://github.com/user-attachments/assets/fd35c88a-7cee-42ee-a329-854ccc2a60d9)

#### 🎥 시연 영상 보러가기([Click](https://youtu.be/Yr2v2oFtGDk))
#### 📙 발표자료 보러가기([Click](https://github.com/Knock-and-knock/backend/blob/main/docs/%5B2%EC%A1%B0%5D%EB%98%91%EB%98%91_%EB%B0%9C%ED%91%9C%EC%9E%90%EB%A3%8C_%EC%88%98%EC%A0%95.pdf))
#### 🖥️ 사이트 바로가기([Click](https://knock-knock-sh.site/))

<br/>

## 💡 프로젝트 소개

**고령자 및 사회 취약 계층을 위한 음성 기반 대화형 금융 및 복지 지원 플랫폼**

- 우리 사회는 급속한 디지털화와 함께 다양한 계층 간 격차가 심화되고 있습니다. 이 과정에서 고령자, 장애인, 저소득층, 다문화 가정 등 금융 약자와 사회 취약 계층의 디지털 소외와 금융 접근성 문제가 심각해지고 있습니다. 은행 지점의 감소와 온라인 서비스의 확대로 인해 디지털에 익숙하지 않거나 접근이 어려운 이들의 금융 서비스 이용이 더욱 힘들어지고 있습니다. 

- 금융 약자와 사회 취약 계층의 금융 접근성을 높이고 동시에 그들의 사회적 연결과 삶의 질을 향상 시킬 수 있는 서비스를 개발하고자 합니다. 음성 기반의 대화형 카드 서비스는 이용자들이 쉽게 금융 서비스를 이용할 수 있게 하며, 동시에 일상적인 대화를 통해 정서적 지원과 필요한 정보를 제공합니다. 

<img src="https://github.com/user-attachments/assets/f12d86dc-8e16-45b9-8e30-29223de8ad4f" width="70%">

<img src="https://github.com/user-attachments/assets/a2d5cf55-065c-43ac-a1bb-2bc51371fa95" width="70%">


<br/>

### ✔️ 서비스 구조도

<img src="https://github.com/user-attachments/assets/653e6193-e58e-4f49-90e8-e53b80fd4f2e" width="70%">

<br/>

## 👪 팀원 소개 및 역할

**개발기간: 2024.07.24 ~ 2024.09.11**

<table>
  <tr>
<td align="center"><b>권대현</b></td>
    <td align="center"><b>정민교</b></td>
    <td align="center"><b>양승건</b></td>
    <td align="center"><b>홍정민</b></td>
    <td align="center"><b>이두리</b></td>
    <td align="center"><b>손동희</b></td>
    <td align="center"><b>정성진</b></td>
  </tr>
  <tr>
    <td align="center"><a href="https://github.com/DevHyun2"><img src="https://avatars.githubusercontent.com/DevHyun2" width="100px;" alt="">
    <td align="center"><a href="https://github.com/MinkyoDev"><img src="https://avatars.githubusercontent.com/MinkyoDev" width="100px;" alt="">
    <td align="center"><a href="https://github.com/YangxGeon"><img src="https://avatars.githubusercontent.com/YangxGeon" width="100px;" alt="">
    <td align="center"><a href="https://github.com/wjdals898"><img src="https://avatars.githubusercontent.com/wjdals898" width="100px;" alt="">
    <td align="center"><a href="https://github.com/lee21330"><img src="https://avatars.githubusercontent.com/lee21330" width="100px;" alt="">
    <td align="center"><a href="https://github.com/Sondonghee123"><img src="https://avatars.githubusercontent.com/Sondonghee123" width="100px;" alt="">
    <td align="center"><a href="https://github.com/cocopg"><img src="https://avatars.githubusercontent.com/cocopg" width="100px;" alt="">
    </td>
  </tr>
</table>

### ✔️ 세부 역할 분담

#### 🔹Backend

- 권대현: 복지 예약 / 카드 내역 / 이상 거래 탐지

- 정민교: PM / 똑똑이 / 대화방, 대화 내역 / CICD

- 양승건: 카드발급 / 알림 / 소비 리포트

- 홍정민: 로그인 / 회원가입 / 매칭 / CICD

#### 🔹Frontend

- 이두리: 메인 / 소비 내역 / 알림 페이지 / 로그인 / 회원가입

- 손동희: 복지 / 소비 리포트 / 생체 로그인

- 정성진: 카드 발급 / 매칭 / 똑똑이 / 메인

<br/>

## 📒 주요 내용

### ✔️ 간편 로그인 및 보호자-피보호자 매칭 구현

#### - 회원가입 및 간편 로그인

고령자분들의 손쉬운 사용을 위하여 간결한 회원가입 과정과 간편 로그인 기능을 구현하였습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/1a2c7631-34ab-45b5-b2b4-416048711994" alt="보호자회원가입" width="200px" />
    <img src="https://github.com/user-attachments/assets/5ae933e5-e115-41db-ab55-dd9504f228be" alt="생체로그인" width="200px" />
</div>

#### - 보호자-피보호자 매칭

보호자로 회원가입 시 피보호자와 매칭을 진행하게 되고 매칭된다면 대신 정보 입력, 이상징후 알림 받기 등의 서비스를 이용할 수 있습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/f60ffecf-cbfd-4813-902b-b07d3a25c539" alt="보호자매칭화면" width="200px" />
    <img src="https://github.com/user-attachments/assets/dc987764-f2d8-47b9-b159-4a7f68e2eba6" alt="피보호자매칭" width="200px" />
</div>

### ✔️ 카드 발급 및 소비 관련 기능

#### - 개인 및 가족 카드 발급

어르신의 카드 사용을 손쉽게 하기 위해서 보호자가 가족카드를 발급할 수 있도록 하였습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/c78ccee6-b53f-455e-9259-372364cd5f74" alt="개인카드발급" width="200px" />
    <img src="https://github.com/user-attachments/assets/7f969343-a321-4294-a4dc-174e0e90f9a8" alt="가족카드신청" width="200px" />
</div>

#### - 소비 내역, 소비 리포트 확인

발급한 카드의 소비 내역과 소비 리포트를 확인할 수 있습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/950bc988-a3f5-4f88-90f6-6ee97f712bb8" alt="소비내역" width="200px" />
    <img src="https://github.com/user-attachments/assets/09b9bf18-7c68-4da5-98e2-3a735eb8e436" alt="소비리포트" width="200px" />
</div>

#### - 소비 이상징후 알림

보호자는 피보호자의 카드 사용 내역에서 이상 거래가 탐지된다면 알림을 받을 수 있습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/02cc2744-1651-4e78-b9fe-f2f4d7841f53" alt="소비이상징후알림" width="200px" />
</div>

### ✔️ 복지 서비스 예약 및 결제

#### - 복지 서비스 예약

보호자는 피보호자의 복지 서비스를 대신 예약할 수 있습니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/d00dd4c9-d862-4c6c-aebc-dd7ec7ba6032" alt="보호자예약" width="200px" />
    <img src="https://github.com/user-attachments/assets/3e4d0cc3-48c4-48fd-8fb7-d39cda49c7bc" alt="보호자예약확인" width="200px" />
    <img src="https://github.com/user-attachments/assets/bfd4fca7-9818-49ce-a776-f9e946d10259" alt="보호자예약취소" width="200px" />
</div>

### ✔️ 대화형 챗봇 '똑똑이'

똑똑이는 LLM과 TTS 모델을 사용하여 사용자와 대화를 통해 상호작용합니다. 똑똑이와 대화를 통하여 어르신들은 화면을 보지 않고 다양한 서비스를 이용할 수 있습니다.

예를 들어 원하는 날짜와 시간을 알려준다면 똑똑이가 복지 서비스를 대신 예약해 줍니다.
<div style="display: flex;">
    <img src="https://github.com/user-attachments/assets/00b38939-209b-44ba-9dc3-7088416e2336" alt="똑똑이일상대화" width="200px" />
    <img src="https://github.com/user-attachments/assets/d65ec333-e8d9-4214-9d90-1dc6e6c997f9" alt="똑똑이복지로" width="200px" />
</div>




<div style="display: flex;">
    <img src="" alt="" width="200px" />
    <img src="" alt="" width="200px" />
</div>

<br/>

## 📲 서비스 이용

<img src="https://github.com/user-attachments/assets/d43648a6-7f4f-494a-8b4e-cb8df00d594c" width="80%">

<br/>

## 🗃️ 시스템 아키텍처

<img src="https://github.com/user-attachments/assets/7c6cd372-0317-4fb6-afce-e92f3fcc8437" width="80%">

<br/>

## 🛠 기술 스택

#### - Front-end
<img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white" alt="HTML5"> <img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white" alt="CSS3"> <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=white" alt="React"> <img src="https://img.shields.io/badge/PWA-5A0FC8?style=for-the-badge&logo=pwa&logoColor=white" alt="PWA">

#### - Back-end
<img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot"> <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security">

#### - AI
<img src="https://img.shields.io/badge/OpenAI-412991?style=for-the-badge&logo=openai&logoColor=white" alt="OpenAI">

#### - Data
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">

#### - Deploy
<img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker"> <img src="https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white" alt="GitHub Actions"> <img src="https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white" alt="AWS"> <img src="https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white" alt="Nginx">

#### - Collaboration Tools
<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub"> <img src="https://img.shields.io/badge/Miro-FFD700?style=for-the-badge&logo=miro&logoColor=white" alt="Miro"> <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white" alt="Notion"> <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Figma">
