### Frontend 

##### Post Service

The frontend posts service requests selected by the user through `postmsg` API to the backend. The format of request is:

```kotlin
{	
   "roomid": int,	
   "requestdetail": string,
    "timestamp": string
}
```

##### Receive Response

The frontend receives request response from the backend through `getmsg` API. The format of response is:

```kotlin
{	
   "code": 200 - Success; 400 - Bad Request
}
```
