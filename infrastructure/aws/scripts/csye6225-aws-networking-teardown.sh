#!/bin/bash

read -p 'Enter the STACK_NAME: ' sname
vpcName="$sname-csye6225-vpc"

#reading variables from txt
. "./$vpcName.txt"
if [ $? -ne 0 ]; then
    echo "Failed to read file. Make sure the VPC details file exists!"
    exit 1
fi

vpcid=$(echo $VpcId)
subnetId1=$(echo $SubnetId1)
subnetId2=$(echo $SubnetId2)
subnetId3=$(echo $SubnetId3)
subnetId4=$(echo $SubnetId4)
subnetId5=$(echo $SubnetId5)
subnetId6=$(echo $SubnetId6)
internetGatewayId=$(echo $InternetGatewayId)
publicRouteTableId=$(echo $PublicRouteTableId)
privateRouteTableId=$(echo $PrivateRouteTableId)

#deleting all subnets
echo "Deleting all subnets..."
aws ec2 delete-subnet --subnet-id $subnetId1
aws ec2 delete-subnet --subnet-id $subnetId2
aws ec2 delete-subnet --subnet-id $subnetId3
aws ec2 delete-subnet --subnet-id $subnetId4
aws ec2 delete-subnet --subnet-id $subnetId5
aws ec2 delete-subnet --subnet-id $subnetId6
echo "All Subnets deleted!"

#deleting route
echo "Deleting route..."
aws ec2 delete-route --route-table-id $publicRouteTableId --destination-cidr-block 0.0.0.0/0
if [ $? -ne 0 ]; then
    echo "Failed during deleting route"
    exit 1
else
    echo "Route deleted!"
fi

#deleting public route table
echo "Deleting public route table..."
aws ec2 delete-route-table --route-table-id $publicRouteTableId
if [ $? -ne 0 ]; then
    echo "Failed during deleting public route table"
    exit 1
else
    echo "Deleted public route table!"
fi

#deleting private route table
echo "Deleting private route table..."
aws ec2 delete-route-table --route-table-id $privateRouteTableId
if [ $? -ne 0 ]; then
    echo "Failed during deleting private route table"
    exit 1
else
    echo "Deleted private route table!"
fi

#detaching internet gateway
echo "Detaching internet gateway..."
aws ec2 detach-internet-gateway --internet-gateway-id $internetGatewayId --vpc-id $vpcid
if [ $? -ne 0 ]; then
    echo "Failed during detaching internet gateway from VPC"
    exit 1
else
    echo "Detached internet gateway from VPC!"
fi

#deleting internet gateway
echo "Deleting internet gateway..."
aws ec2 delete-internet-gateway --internet-gateway-id $internetGatewayId
if [ $? -ne 0 ]; then
    echo "Failed during deleting internet gateway"
    exit 1
else
    echo "Deleted internet gateway!"
fi

#deleting vpc
echo "Deleting vpc..."
aws ec2 delete-vpc --vpc-id $vpcid
if [ $? -ne 0 ]; then
    echo "Failed during deleting VPC"
    exit 1
else
    rm -rf $vpcName.txt
    echo "Deleted VPC!"
fi

echo "======== Networking stack cleared ========="
