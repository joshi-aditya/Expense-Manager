## Getting Started

Clone the repository on your local machine

### Prerequisites

Install json parser in fedora using following command

```
sudo dnf install jq
```
### Stack workflow
#### Stack creation

1. To create networking stack:
    - Run the following script in terminal
	```
	bash csye6225-aws-cf-create-stack.sh
	```
    - Enter the name of the stack to be created

2. To create application stack:
    - run the following script in terminal
	```
	bash csye6225-aws-cf-create-application-stack.sh
	```
    - Enter the name of the stack to be created

#### Stack deletion
1. To delete networking stack:
    - Run the following script in terminal
	```
	bash csye6225-aws-cf-terminate-stack.sh
	```
    - Enter the name of the stack to be deleted

2. To delete application stack:
    - Run the following script in terminal
	```
	bash csye6225-aws-cf-terminate-application-stack.sh
	```
    - Enter the name of the stack to be deleted
