## Getting started

Clone the repository on your local machine

### Stack workflow
#### Stack/Network creation

* To create the stack, run the following script in terminal
```
bash csye6225-aws-networking-setup.sh
```
* Enter the name of the stack, and details of CIDR blocks for VPC and subnets to be created. Here's a sample input:
```
Enter the STACK_NAME: test
Enter the CIDR block details for test-csye6225-vpc: 10.0.0.0/24
Enter the CIDR details for public subnet 1: 10.0.0.0/28
Enter the CIDR details for public subnet 2: 10.0.0.16/28
Enter the CIDR details for public subnet 3: 10.0.0.32/28
Enter the CIDR details for private subnet 1: 10.0.0.48/28
Enter the CIDR details for private subnet 2: 10.0.0.64/28
Enter the CIDR details for private subnet 3: 10.0.0.80/28
```

#### Stack/Network teardown
* To delete the stack, run the following script in same directory as of create stack in terminal
```
bash csye6225-aws-networking-teardown.sh
```
* Enter the name of the stack to be deleted. Here's a sample:
```
Enter the STACK_NAME: test
```
