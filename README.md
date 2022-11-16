# Online-Job-Portal
Problem Statement: 
Online Job portal applications should meet the requirement of High availability. While deploying the 
application and scaling up resources according to the requirement, we need to consider all the 
application dependencies and make sure that all dependencies for the application are available with 
respective versions. 

Since this is an Online Job portal application, usage of applications is high based on the number of 
job seekers using the app. When users are increased our resources should be ready to scale up 
which is not possible with an on-premises setup until and unless you have huge resources. Even if 
you have resources it will take time for connecting those resources in a network and there are 
physical challenges that we need to face every day based on the usage of the application.

Solution: 
Dependency: All the application dependencies can be packed as a Docker Image along with the 
application, So when we run the Docker Image, the Application runs seamlessly without any issues 
with dependency issues. 

High Availability: The application can be available all the time by deploying in Kubernetes using K8’s 
deployments. The application can use a load balancer and auto-scaling in AWS, also utilize the 
resources and scale up immediately with a ‘pay for usage’ policy using AWS EKS. 



#move docker file into the naukri to create a image of the code file
#or
#u can edit it and create ur own version
