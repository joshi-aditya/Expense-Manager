## Getting Started

Clone the repository on your local machine

### Prerequisites

Install json parser in fedora using following command

```
sudo dnf install jq
```
### Stack workflow
#### Stack creation

* To create the networking stack, run the following script in terminal
```
bash csye6225-aws-cf-create-stack.sh
```
* Enter the name of the stack to be created
* To create the application stack, run the following script in terminal
```
bash csye6225-aws-cf-create-application-stack.sh
```
* Enter the name of the stack to be created

#### Stack deletion
* To delete the stack, run the following script in terminal
```
bash csye6225-aws-cf-terminate-stack.sh
```
* Enter the name of the stack to be deleted
* To delete the stack, run the following script in terminal
```
bash csye6225-aws-cf-terminate-application-stack.sh
```
* Enter the name of the stack to be deleted
