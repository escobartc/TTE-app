# Tech Trend Emporium

Welcome to TECH TREND EMPORIUM!

This repository contains the code developed for the BackEnd challenge, where the proposed requirements outlined in the [Tech Trend Emporium Confluence page](https://confluence.endava.com/display/DevDisc/Tech+Trend+Emporium) are fulfilled.

## Git Methodology
Trunk-based methodology has been utilized for this project. In brief, Trunk-based development is a workflow where developers directly commit their changes to the main branch, avoiding long-lived feature branches.

To create a Pull Request (PR), approval from both developers and a test coverage exceeding 60% are required.

### Continuous Integration (CI)
Our CI pipeline automates the build, test, and quality assurance processes for our application.

- Build and Test: The CI pipeline compiles the application and executes unit tests to verify code functionality and quality.
- Jacoco: We use Jacoco to generate code coverage reports, ensuring that our tests adequately cover the codebase.
- DockerHub: Docker images built during the CI process are stored in DockerHub. This allows for easy access and deployment of the application in different environments.

## Continuous Deployment (CD)
Our CD pipeline automates the deployment process for new changes to the main branch.

- Build and Upload to AWS ECR: The CD pipeline builds the Docker image and uploads it to an AWS Elastic Container Registry (ECR), ensuring that the latest version of the application is available for deployment.
- Deploy to AWS ECS: The CD pipeline deploys the Docker container to an AWS Elastic Container Service (ECS) cluster, ensuring that the application is running and accessible to users.
- SonarCloud Integration: SonarCloud is integrated into our CD pipeline to analyze code quality and ensure that our codebase meets predefined quality standards.

## Technologies Used

- Docker: Containerization technology used to package and deploy the application consistently across different environments.
- AWS (Amazon Web Services): Cloud platform utilized for hosting, container orchestration, and storage of Docker images.
- JaCoCo: Code coverage tool used to assess the comprehensiveness of our test suite.
- SonarCloud: Continuous code quality analysis tool used to detect and address code issues, vulnerabilities, and technical debt.

## Testing

You can view the metrics of our application at the following link: [SonarCloud Metrics](https://sonarcloud.io/summary/overall?id=escobartc_TTE-app)

We achieved a code coverage of 72.6% across the entire codebase.

## Postman

You can find all the information about the endpoints in Postman, along with environment variables, in the repository.
