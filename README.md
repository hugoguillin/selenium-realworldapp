## Project description
The aim of this project is to showcase one way of creating an E2E test automation framework for [a web application](https://github.com/hugoguillin/realworld-app) using Selenium WebDriver.

The tech stack used in this project is:
- **Java 17**
- **Spring Boot** for managing dependency injection 
- **Selenium WebDriver** for interacting with the web application
- **RestAssured** for making HTTP requests to the web application's API
- **TestNG** as the test runner
- **Maven**
- **Allure Report** for test reporting
- **Docker**

The main features provided by this framework are:
- **Page Object Model** design pattern to create a clear separation between the test code and the page code
- **Network traffic interception** to wait for requests to be completed. See [NetworkInterceptor](https://github.com/hugoguillin/selenium-realworldapp/blob/main/src/main/java/com/realworld/seleniumrealworldapp/infra/NetworkInterceptor.java)
- **Mock request responses** to avoid heavy test setup and to be able to test different scenarios. See [AuthorDetailPage > goToFavoritedArticles](https://github.com/hugoguillin/selenium-realworldapp/blob/main/src/main/java/com/realworld/seleniumrealworldapp/pageObjects/AuthorDetailPage.java#L40)
- Skip user login via UI to speed up test execution. Instead, user auth data is set directly in the browser's local storage. See [BaseTest > setUp](https://github.com/hugoguillin/selenium-realworldapp/blob/main/src/test/java/com/realworld/seleniumrealworldapp/base/BaseTest.java#L50)
- Run tests **in parallel** via a GitHub Actions workflow. See [.github/workflows/selenium.yml](https://github.com/hugoguillin/selenium-realworldapp/blob/main/.github/workflows/selenium.yml). An Allure report is generated after each test run. See [more recent report](https://hugoguillin.github.io/selenium-realworldapp)
- Run tests in parallel in your local machine using Selenium Grid. See [docker-compose-local.yml](https://github.com/hugoguillin/selenium-realworldapp/blob/main/.docker/docker-compose-local.yml)

Be aware that this tests can only be run on Chromium browsers (Chrome, Edge, etc.), since some features dependent on DevTools protocol (network interception, request mocking) can only be implemented using Chromium-based drivers at the time of writing. When the BiDi apis are ready for these features, I will update the project accordingly.

## How to run the tests
### Prerequisites
- **Java 17** installed
- **Maven** installed
- **Docker** and **Docker Compose** installed
- **Chromium browser** installed

### Spin up the web application
1. Open a terminal, navigate to the `.docker` folder and run
```bash
docker-compose -f docker-compose-local.yml up -d
```
This will also start a Selenium Grid with 4 Chrome nodes.
2. Seed the database with some data
```bash
docker-compose -f docker-compose-local.yml exec -T app npm run sqlz -- db:seed:all
```
3. Sign up the default user for the tests
```bash
docker-compose -f docker-compose-local.yml exec node1 sh -c "curl -X POST 'http://app:3000/api/users' -H 'Content-Type: application/json' -d '{\"user\": {\"username\": \"selenium-user\",\"email\": \"selenium@realworld.com\",\"password\": \"selenium@realworld.com\"}}'"
```
4. Open the web application in your browser at [http://localhost:3000](http://localhost:3000)

### Run the tests
There are two ways to run the tests. From the root folder of the project, run one of the following commands:
1. Run tests in parallel against the Selenium Grid containers
```bash
mvn test -Dspring.profiles.active=remote -Dsurefire.suiteXmlFiles=testSuites/allTests.xml
```
2. Run individual tests in your local machine, using you IDE or the terminal
```bash
mvn test -Dtest=ArticleDetailTests
```
