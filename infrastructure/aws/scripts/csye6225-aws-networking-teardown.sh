#!/bin/bash

read -p 'Enter the STACK_NAME: ' sname
vpcName="$sname-csye6225-vpc"

#reading variables from txt
. "./$vpcName.txt"
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
echo "Route deleted!"

#deleting public route table
echo "Deleting public route table..."
aws ec2 delete-route-table --route-table-id $publicRouteTableId
echo "Deleted public route table!"

#deleting private route table
echo "Deleting private route table..."
aws ec2 delete-route-table --route-table-id $privateRouteTableId
echo "Deleted private route table!"

#detaching internet gateway
echo "Detaching internet gateway..."
aws ec2 detach-internet-gateway --internet-gateway-id $internetGatewayId --vpc-id $vpcid
echo "Detached internet gateway from VPC!"

#deleting internet gateway
echo "Deleting internet gateway..."
aws ec2 delete-internet-gateway --internet-gateway-id $internetGatewayId
echo "Deleted internet gateway!"

#deleting vpc
echo "Deleting vpc..."
aws ec2 delete-vpc --vpc-id $vpcid
echo "Deleted VPC!"

echo "======== Networking stack cleared ========="
