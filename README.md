# Cloud Application - Serverless function

- [Application Server Repository](https://github.com/GokulaKrishnanRGK/cloud-webapp-server)
- [IaC Infrastructure Repository in GCP](https://github.com/GokulaKrishnanRGK/tf-gcp-infra)
- [IaC Infrastructure Repository in AWS](https://github.com/GokulaKrishnanRGK/tf-aws-infra)

## Repo Details
- Send email verification on sign-up
- Triggered via pub-sub CDN event

## Features

- **CI/CD Pipeline**: Implemented a CI/CD pipeline using GitHub Actions, integrating application tests, and validating Terraform and Packer configurations.
- **Terraform Infrastructure**: Developed Terraform scripts to deploy cloud infrastructure, including:
  - Auto-Scaling Groups for dynamic resource management.
  - Load Balancers to manage traffic efficiently.
  - Serverless cloud functions triggered by Pub/Sub CDN events for scalable and event-driven processing.

## Technologies Used

- **Cloud Platform**: Google Cloud Platform (GCP)
- **Infrastructure as Code (IaC)**: Terraform
- **Image Building**: Packer
- **Programming Languages and Frameworks**: 
  - Java
  - Spring Boot
  - Hibernate
- **Database**: MySQL
- **Scripting and OS**: 
  - Bash
  - Linux
- **Version Control**: Git
   
### Additional Instructions
- Deploy cloud infrasttructure from from [IaC Infrastructure Repository](https://github.com/GokulaKrishnanRGK/tf-gcp-infra)
- Generate server Packer build from [Application Server Repository](https://github.com/GokulaKrishnanRGK/cloud-webapp-server)
