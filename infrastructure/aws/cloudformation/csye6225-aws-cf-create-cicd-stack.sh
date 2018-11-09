read -p 'Enter stack name: ' STACK_NAME
echo $STACK_NAME

export TRAVIS_UPLOAD_TO_S3_POLICY_NAME=Travis-Upload-To-S3
export TRAVIS_CODE_DEPLOY_POLICY_NAME=Travis-Code-Deploy
export CODE_DEPLOY_SERVICE_ROLE=CodeDeployServiceRole
export CODE_DEPLOY_SERVICEROLE_SERVICE=codedeploy.amazonaws.com
export CODE_DEPLOY_EC2_SERVICE_ROLE=CodeDeployEC2ServiceRole
export CODE_DEPLOY_EC2_SERVICEROLE_SERVICE=ec2.amazonaws.com
export CODE_DEPLOY_EC2_S3_NAME=CodeDeploy-EC2-S3
export account_id=$(aws sts get-caller-identity --query "Account" --output text)
export region=us-east-1
export application_name=csye6225-fall2018
export resourse1="arn:aws:iam::"$account_id":user/travis-ci"
export resourse2="arn:aws:codedeploy:"$region":"$account_id":application:"$application_name
export resourse3="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.OneAtATime"
export resourse4="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.HalfAtATime"
export resourse5="arn:aws:codedeploy:"$region":"$account_id":deploymentconfig:CodeDeployDefault.AllAtOnce"
domain_name=$(aws route53 list-hosted-zones --query 'HostedZones[0].Name' --output text)
bucket_name="code-deploy."$domain_name"csye6225.com"

STACK_ID=$(\aws cloudformation create-stack --stack-name ${STACK_NAME} \
--template-body file://csye6225-cf-cicd.json \
--parameters ParameterKey=Resource1,ParameterValue=$resourse1 ParameterKey=Resource2,ParameterValue=$resourse2 ParameterKey=Resource3,ParameterValue=$resourse3 ParameterKey=Resource4,ParameterValue=$resourse4 ParameterKey=Resource5,ParameterValue=$resourse5 ParameterKey=TravisUploadToS3PolicyName,ParameterValue=$TRAVIS_UPLOAD_TO_S3_POLICY_NAME ParameterKey=TravisCDPolicyName,ParameterValue=$TRAVIS_CODE_DEPLOY_POLICY_NAME   ParameterKey=CodeDeployServiceRoleService,ParameterValue=$CODE_DEPLOY_SERVICEROLE_SERVICE  ParameterKey=CodeDeployServiceRoleName,ParameterValue=$CODE_DEPLOY_SERVICE_ROLE ParameterKey=ApplicationName,ParameterValue=${application_name} ParameterKey=BucketName,ParameterValue=${bucket_name} ParameterKey=CodeDeployEC2ServiceRoleService,ParameterValue=$CODE_DEPLOY_EC2_SERVICEROLE_SERVICE ParameterKey=CodeDeployEC2ServiceRoleName,ParameterValue=$CODE_DEPLOY_EC2_SERVICE_ROLE ParameterKey=CodeDeployEC2S3Name,ParameterValue=$CODE_DEPLOY_EC2_S3_NAME \
--capabilities CAPABILITY_IAM \
--capabilities CAPABILITY_NAMED_IAM \
| jq -r .StackId \
)

echo "Waiting on ${STACK_ID} create completion.."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
if [ $? -ne 0 ]; then
	echo "Stack ${STACK_NAME} creation failed!"
    exit 1
else
    echo "Stack ${STACK_NAME} created successfully!"
fi
echo "User policies created!"
echo "User roles and profiles created !"
echo "S3 Bucket created !"
