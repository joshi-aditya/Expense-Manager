#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME

vpc_id=$(aws ec2 describe-vpcs --filters "Name=isDefault,Values=false" --query "Vpcs[*].VpcId" --output text)
echo $vpc_id
subnet_id=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=${vpc_id}" "Name=mapPublicIpOnLaunch,Values=true" "Name=availabilityZone,Values=us-east-1a" --query 'Subnets[*].SubnetId'  --output text)

subnet_id2=$(aws ec2 describe-subnets --filters "Name=vpc-id,Values=${vpc_id}" "Name=mapPublicIpOnLaunch,Values=true" "Name=availabilityZone,Values=us-east-1b" --query 'Subnets[*].SubnetId'  --output text)

echo $subnet_id

domain_name=$(aws route53 list-hosted-zones --query 'HostedZones[0].Name' --output text)
bucket_name=${domain_name}"csye6225.com"
echo $bucket_name

export CODE_DEPLOY_EC2_SERVICE_ROLE=CodeDeployEC2ServiceRole
export CODE_DEPLOY_EC2_SERVICEROLE_SERVICE=ec2.amazonaws.com
export CODE_DEPLOY_EC2_S3_NAME=CodeDeploy-EC2-S3

echo "Creating stack.."

STACK_ID=$(\aws cloudformation create-stack --stack-name ${STACK_NAME} \
	--template-body file://csye6225-aws-cf-application.json\
	--parameters ParameterKey=StackName,ParameterValue=${STACK_NAME} ParameterKey=VpcId,ParameterValue=${vpc_id} ParameterKey=SubnetId,ParameterValue=${subnet_id} ParameterKey=SubnetId2,ParameterValue=${subnet_id2} ParameterKey=BucketName,ParameterValue=${bucket_name} ParameterKey=CodeDeployEC2ServiceRoleService,ParameterValue=$CODE_DEPLOY_EC2_SERVICEROLE_SERVICE ParameterKey=CodeDeployEC2ServiceRoleName,ParameterValue=$CODE_DEPLOY_EC2_SERVICE_ROLE ParameterKey=CodeDeployEC2S3Name,ParameterValue=$CODE_DEPLOY_EC2_S3_NAME \
	--capabilities CAPABILITY_IAM \
	--capabilities CAPABILITY_NAMED_IAM \
 	| jq -r .StackId \
)
	
	
#Job Done!
echo "Waiting on ${STACK_ID} create completion.."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
if [ $? -ne 0 ]; then
	echo "Application Stack creation failed!"
    exit 1
else
    echo "EC2 Instance, RDS, security groups, DynamoDB Table and S3 Bucket created!"
fi