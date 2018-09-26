#!/bin/bash

read -p 'Enter the STACK_NAME: ' sname
vpcName="$sname-csye6225-vpc"
internetGatewayName="$name-csye6225-InternetGateway"
publicRouteTableName="$name-csye6225-public-route-table"
read -p "Enter the CIDR block details for $vpcName: " vpcCidrBlock
availabilityZoneA="us-east-1a"
availabilityZoneB="us-east-1b"
availabilityZoneC="us-east-1c"
read -p "Enter the CIDR details for subnet 1: " subnetCidr1
read -p "Enter the CIDR details for subnet 2: " subnetCidr2
read -p "Enter the CIDR details for subnet 3: " subnetCidr3


#creating vpc
aws_response_vpc=$(aws ec2 create-vpc --cidr-block $vpcCidrBlock)
if [ $? -ne 0 ]; then
    echo "Failed during creating a VPC"
    exit 1
else
    echo "VPC created!"
fi
vpcId=$(echo -e "$aws_response_vpc" | grep VpcId | awk '{print $2}' | tr -d '"' | tr -d ',')
echo "VpcId=$vpcId" > "$vpcName".txt

#naming the vpc
aws ec2 create-tags --resources "$vpcId" --tags Key=Name,Value="$vpcName"

#creating subnet1
aws_response_subnet1=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr1 --availability-zone $availabilityZoneA)
if [ $? -ne 0 ]; then
    echo "Failed during creating Subnet1"
    exit 1
else
    echo "Subnet1 created!"
fi
subnetId1=$(echo -e "$aws_response_subnet1" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet2
aws_response_subnet2=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr2 --availability-zone $availabilityZoneB)
if [ $? -ne 0 ]; then
    echo "Failed during creating Subnet2"
    exit 1
else
    echo "Subnet2 created!"
fi
subnetId2=$(echo -e "$aws_response_subnet2" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet3
aws_response_subnet3=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr3 --availability-zone $availabilityZoneC)
if [ $? -ne 0 ]; then
    echo "Failed during creating Subnet3"
    exit 1
else
    echo "Subnet3 created!"
fi
subnetId3=$(echo -e "$aws_response_subnet3" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

echo "SubnetId1=$subnetId1" >> "$vpcName".txt
echo "SubnetId2=$subnetId2" >> "$vpcName".txt
echo "SubnetId3=$subnetId3" >> "$vpcName".txt

#creating internet gateway
aws_response_igw=$(aws ec2 create-internet-gateway)
internetGatewayId=$(echo -e "$aws_response_igw" | grep InternetGatewayId | awk '{print $2}' | tr -d '"' | tr -d ',')

#naming the internet gateway
aws ec2 create-tags --resources "$internetGatewayId" --tags Key=Name,Value="$internetGatewayName"

#attaching Internet Gateway to VPC
aws ec2 attach-internet-gateway --internet-gateway-id $internetGatewayId --vpc-id $vpcId

#creating a public route table
aws_response_rtable=$(aws ec2 create-route-table --vpc-id $vpcId)
publicRouteTableId=$(echo -e "$aws_response_rtable" | grep RouteTableId | awk '{print $2}' | tr -d '"' | tr -d ',')

#naming the public route table
aws ec2 create-tags --resources "$publicRouteTableId" --tags Key=Name,Value="$publicRouteTableName"

#attaching subnets to route table
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId1
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId2
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId3

#creating public route
aws ec2 create-route --route-table-id $publicRouteTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $internetGatewayId

echo "InternetGatewatId=$internetGatewayId" >> "$vpcName".txt
echo "RouteTableId=$publicRouteTableId" >> "$vpcName".txt
