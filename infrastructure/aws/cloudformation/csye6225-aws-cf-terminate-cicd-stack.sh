#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME

echo "Deleting stack.."
STACK_ID=$(\
    aws cloudformation delete-stack \
    --stack-name ${STACK_NAME} \
)

echo "Waiting on deletion.."
aws cloudformation wait stack-delete-complete --stack-name ${STACK_NAME}
echo "Stack ${STACK_NAME} deleted successfully!"
