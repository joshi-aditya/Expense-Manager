#!/bin/bash
read -p 'Enter stack name: ' STACK_NAME
echo "Deleting stack.."
STACK_ID=$(\
    aws cloudformation delete-stack \
    --stack-name ${STACK_NAME} \
)