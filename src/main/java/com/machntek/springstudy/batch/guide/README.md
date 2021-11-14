## Create an Intermediate Processor
배치 프로세싱의 일반적인 패러다임은 데이터를 수집하고, 변환하고, 다른곳에 보내는 것이.

ItemProcessor<I,O> 에서 I, O는 같은 타입이 아니어도 된다.

@EnableBatchProcessing 애노테이션은 작업을 지원하고 번거로운 작업을 단축해주는 많은 중요한 빈들을 추가한다.

### @SpringBootApplication
#### @Configuration
애플리케이션 컨텍스트에 대한 빈 정의의 소스로 클래스에 태그를 지정함

#### @EnableAutoConfiguration
클래스 경로 설정, 기타 빈 및 다양한 프로퍼티 설정을 기반으로 빈 추가를 시작하도록 스프링부트에 지시.

예를 들어 spring-webmvc가 클래스 경로에 있는 경우 이 애노테이션은 애플리케이션을 웹 애플리케이션으로 플래그 지정하고 DispatcherServlet 설정과 같은 주요 동작을 활성화한다.

#### @ComponentScan
해당 패키지 아래에 components, configurations, services를 찾도록 지시하여 컨트롤러를 찾도록 한다.

## Build an executable JAR
Gradle 또는 Maven을 사용하여 CLI에서 애플리케이션을 실행할 수 있습니다.

필요한 모든 종속성, 클래스 및 리소스를 포함하는 단일 실행 가능한 JAR 파일을 빌드하고 실행할 수도 있습니다. 실행 가능한 jar를 빌드하면 개발 수명 주기 전반에 걸쳐 다양한 환경 등에서 서비스를 애플리케이션으로 쉽게 제공, 버전 지정 및 배포할 수 있습니다.
