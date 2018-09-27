#!/bin/bash

read -p 'Enter the STACK_NAME: ' sname
vpcName="$sname-csye6225-vpc"
internetGatewayName="$sname-csye6225-InternetGateway"
publicRouteTableName="$sname-csye6225-public-route-table"
privateRouteTableName="$sname-csye6225-private-route-table"
read -p "Enter the CIDR block details for $vpcName: " vpcCidrBlock
read -p "Enter the CIDR details for public subnet 1: " subnetCidr1
read -p "Enter the CIDR details for public subnet 2: " subnetCidr2
read -p "Enter the CIDR details for public subnet 3: " subnetCidr3
read -p "Enter the CIDR details for private subnet 1: " subnetCidr4
read -p "Enter the CIDR details for private subnet 2: " subnetCidr5
read -p "Enter the CIDR details for private subnet 3: " subnetCidr6

availabilityZoneA="us-east-1a"
availabilityZoneB="us-east-1b"
availabilityZoneC="us-east-1c"

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
    echo "Failed during creating Public Subnet1"
    exit 1
else
    echo "Public Subnet1 created!"
fi
subnetId1=$(echo -e "$aws_response_subnet1" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet2
aws_response_subnet2=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr2 --availability-zone $availabilityZoneB)
if [ $? -ne 0 ]; then
    echo "Failed during creating Public Subnet2"
    exit 1
else
    echo "Public Subnet2 created!"
fi
subnetId2=$(echo -e "$aws_response_subnet2" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet3
aws_response_subnet3=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr3 --availability-zone $availabilityZoneC)
if [ $? -ne 0 ]; then
    echo "Failed during creating Public Subnet3"
    exit 1
else
    echo "Public Subnet3 created!"
fi
subnetId3=$(echo -e "$aws_response_subnet3" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet4
aws_response_subnet4=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr4 --availability-zone $availabilityZoneA)
if [ $? -ne 0 ]; then
    echo "Failed during creating Private Subnet1"
    exit 1
else
    echo "Private Subnet1 created!"
fi
subnetId4=$(echo -e "$aws_response_subnet4" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet5
aws_response_subnet5=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr5 --availability-zone $availabilityZoneB)
if [ $? -ne 0 ]; then
    echo "Failed during creating Private Subnet2"
    exit 1
else
    echo "Private Subnet2 created!"
fi
subnetId5=$(echo -e "$aws_response_subnet5" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

#creating subnet6
aws_response_subnet6=$(aws ec2 create-subnet --vpc-id $vpcId --cidr-block $subnetCidr6 --availability-zone $availabilityZoneC)
if [ $? -ne 0 ]; then
    echo "Failed during creating Private Subnet3"
    exit 1
else
    echo "Private Subnet3 created!"
fi
subnetId6=$(echo -e "$aws_response_subnet6" | grep SubnetId | awk '{print $2}' | tr -d '"' | tr -d ',')

echo "SubnetId1=$subnetId1" >> "$vpcName".txt
echo "SubnetId2=$subnetId2" >> "$vpcName".txt
echo "SubnetId3=$subnetId3" >> "$vpcName".txt
echo "SubnetId4=$subnetId4" >> "$vpcName".txt
echo "SubnetId5=$subnetId5" >> "$vpcName".txt
echo "SubnetId6=$subnetId6" >> "$vpcName".txt

#creating internet gateway
aws_response_igw=$(aws ec2 create-internet-gateway)
if [ $? -ne 0 ]; then
    echo "Failed during creating Internet Gateway"
    exit 1
else
    echo "Internet Gateway created!"
fi
internetGatewayId=$(echo -e "$aws_response_igw" | grep InternetGatewayId | awk '{print $2}' | tr -d '"' | tr -d ',')

#naming the internet gateway
aws ec2 create-tags --resources "$internetGatewayId" --tags Key=Name,Value="$internetGatewayName"

#attaching Internet Gateway to VPC
aws ec2 attach-internet-gateway --internet-gateway-id $internetGatewayId --vpc-id $vpcId

#creating a public route table
aws_response_rtable=$(aws ec2 create-route-table --vpc-id $vpcId)
if [ $? -ne 0 ]; then
    echo "Failed during creating Public Route Table"
    exit 1
else
    echo "Public Route Table created!"
fi
publicRouteTableId=$(echo -e "$aws_response_rtable" | grep RouteTableId | awk '{print $2}' | tr -d '"' | tr -d ',')

#naming the public route table
aws ec2 create-tags --resources "$publicRouteTableId" --tags Key=Name,Value="$publicRouteTableName"

#attaching subnets to route table
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId1
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId2
aws ec2 associate-route-table --route-table-id $publicRouteTableId --subnet-id $subnetId3
echo "Subnets associated with public route table"

#creating public route
aws ec2 create-route --route-table-id $publicRouteTableId --destination-cidr-block 0.0.0.0/0 --gateway-id $internetGatewayId
if [ $? -ne 0 ]; then
    echo "Failed during creating Public Route Table"
    exit 1
else
    echo "Public Route Table created!"
fi

#creating a private route table
aws_response_privatertable=$(aws ec2 create-route-table --vpc-id $vpcId)
if [ $? -ne 0 ]; then
    echo "Failed during creating Private Route Table"
    exit 1
else
    echo "Private Route Table created!"
fi
privateRouteTableId=$(echo -e "$aws_response_privatertable" | grep RouteTableId | awk '{print $2}' | tr -d '"' | tr -d ',')

#naming the private route table
aws ec2 create-tags --resources "$privateRouteTableId" --tags Key=Name,Value="$privateRouteTableName"

#attaching subnets to private route table
aws ec2 associate-route-table --route-table-id $privateRouteTableId --subnet-id $subnetId4
aws ec2 associate-route-table --route-table-id $privateRouteTableId --subnet-id $subnetId5
aws ec2 associate-route-table --route-table-id $privateRouteTableId --subnet-id $subnetId6
echo "Subnets associated with Private Route Table"

echo "InternetGatewayId=$internetGatewayId" >> "$vpcName".txt
echo "PublicRouteTableId=$publicRouteTableId" >> "$vpcName".txt
echo "PrivateRouteTableId=$privateRouteTableId" >> "$vpcName".txt

echo "==============Networking setup using AWS CLI complete==============="
