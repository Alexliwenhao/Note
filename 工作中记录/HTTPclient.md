# generated-requests.http
```shell
###  
POST {{host}}/userservice/member/login  
Accept: application/json;charset=UTF-8  
Content-Type: application/json  
  
{  
  "username": "admin",  
  "password": "adminP@ssw0rd"  
}  
  
> {% client.global.set("auth_token_admin", response.body.data.token); %}  
  
<> 2023-04-23T134915.200.json  
<> 2023-04-20T114656.200.json  
  
###  
GET http://{{host}}/commonservice/dashboard/projectVariable?projectId=2351  
Authorization: {{auth_token_admin}}  
Content-Type: application/json  
  
<> 2023-04-20T115110.200.json  
  
###  
GET http://{{host}}/pipelineservice/pipelineProd/gitCommitInfo?id=1841&buildId=5  
Authorization: {{auth_token_admin}}  
Content-Type: application/json  
  
<> 2023-04-23T135030.200.json
```

# http-client.env.json
```
{  
  "local": {  
    "name": "local",  
    "host": "127.0.0.1:8090"  
  },  
  "qa": {  
    "name": "qa",  
    "host": "10.0.3.4:30333"  
  }  
}
```