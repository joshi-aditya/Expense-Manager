#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME
echo "Creating stack.."
STACK_ID=$(\
    aws cloudformation create-stack \
    --stack-name ${STACK_NAME} \
    --template-body file://csye6225-cf-networking.yml \
    --parameters ParameterKey=StackName,ParameterValue=${STACK_NAME} \
    | jq -r .StackId \
)
echo "Waiting on ${STACK_ID} create completion.."
aws cloudformation wait stack-create-complete --stack-name ${STACK_ID}
