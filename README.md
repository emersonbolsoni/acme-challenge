# Acme Device Control 
This is an API solution to control Devices created based on a challenge for a technical position. 

All the CRUD operations are contemplated in this project.

## Dependencies

This application requires PostgreSQL container to run. Before generate the application container, make sure you have PostgreSQL container up and running.

The JUnit tests *(requirement to compile)* depend on this database.

## Documentation
All the documentation can be visited on link `http://localhost:8080/swagger-ui/index.html` after the application running on container

## Sequence to run the project

### **1 - Create a POSTGRESQL container**

First, we will create a new volume using command: `docker volume create postgresql_volume`

To make sure you have the new volume created, run this command: `docker volume ls` and you'll see the volume list you have created.

Use this command to create and run the container 

`docker run --name postgres_database -p 5432:5432 -e POSTGRES_PASSWORD=Postgres2025! --volume postgresql_volume:/var/lib/postgresql -d postgres`

If everything is OK, you should receive a code confirming your container is up and running. 

To make sure, run the command `docker ps` and see if your container information was returned.

Now, we need to capture the IP Address of container to build our application. To do that, run the command `docker network inspect bridge` and you'll see the json structure for network and also the IPV4 information for postgresql image.

You should get some like this:

```json
[
    {
        "Name": "bridge",
        "Id": "94937464703ddaf42b9d38ce78be6431f353198e0022a015b6120c231312b512",
        "Created": "2025-04-16T17:28:07.87104628Z",
        "Scope": "local",
        "Driver": "bridge",
        "EnableIPv4": true,
        "EnableIPv6": false,
        "IPAM": {
            "Driver": "default",
            "Options": null,
            "Config": [
                {
                    "Subnet": "172.17.0.0/16",
                    "Gateway": "172.17.0.1"
                }
            ]
        },
        "Internal": false,
        "Attachable": false,
        "Ingress": false,
        "ConfigFrom": {
            "Network": ""
        },
        "ConfigOnly": false,
        "Containers": {
            "ce9bd6f3d918c99e3c9397712c56d8d0da9cdb4a8caa9bcf681d0e5a8366915e": {
                "Name": "postgres_database",
                "EndpointID": "16c97160b25ea8edc48d5b60e7d4bd1498096a868147ced1ab845b4696735d83",
                "MacAddress": "ca:00:18:86:35:2c",
                "IPv4Address": "172.17.0.2/16",
                "IPv6Address": ""
            }
        },
        "Options": {
            "com.docker.network.bridge.default_bridge": "true",
            "com.docker.network.bridge.enable_icc": "true",
            "com.docker.network.bridge.enable_ip_masquerade": "true",
            "com.docker.network.bridge.host_binding_ipv4": "0.0.0.0",
            "com.docker.network.bridge.name": "docker0",
            "com.docker.network.driver.mtu": "65535"
        },
        "Labels": {}
    }
]
```
The information we needed is inside **Containers** node **IPv4Address**, in this case, the value is **172.17.0.2**

### **2 - Compile and run the Acme container**

Now, we'll prepare the Acme container to run. Inside the root directory (same as Dockerfile file directory) we must change the ip address to show how microservice can reach database. 

To do that, replace the IP Address inside file `application.properties` in the line where contains `spring.datasource.url`

After that, you can run the command line `docker build -t acme-springboot .` and the image will be build.

At least, we need to run that image. To do that, run the command `docker run --name acme -p 8080:8080 -d acme-springboot` and the confirmation should be displayed.


## Improvements
- [ ] Authentication OAuth 2.0
- [ ] Rate Limit Control
- [ ] Spike Arrest
- [ ] Prevent SQL Injection
- [ ] HATEOAS
- [ ] Apply certified to use https
- [ ] Use a constant to 'State' field
- [ ] Register who and when the changes are made
- [ ] Define a custom error payload return with custom error codes 