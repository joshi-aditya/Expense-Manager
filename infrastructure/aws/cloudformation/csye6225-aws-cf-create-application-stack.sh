#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME

vpc_id=$(aws ec2 describe-vpcs --query "Vpcs[0].VpcId" --output text)
echo $vpc_id
subnet_id=$(aws ec2 describe-subnets --query "Subnets[2].SubnetId" --output text)

echo $subnet_id

echo "Creating stack.."

STACK_ID=$(\aws cloudformation create-stack --stack-name ${STACK_NAME} \
	--template-body file://csye6225-aws-cf-application.json\
	--parameters ParameterKey=StackName,ParameterValue=${STACK_NAME} ParameterKey=VpcId,ParameterValue=${vpc_id} ParameterKey=SubnetId,ParameterValue=${subnet_id} \
   | jq -r .StackId \
)
	
	
#Job Done!
echo "Waiting on ${STACK_ID} create completion.."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
echo "EC2 Instances and security groups created!"
