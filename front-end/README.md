### Frontend 

#### Post Service

The frontend posts service requests selected by the user through `postmsg` API to the backend. The format of request is:

```kotlin
{	
   "roomid": int,	
   "requestdetail": string,
    "timestamp": string/Timestamp
}
```

The frontend receives request response from the backend through `getmsg` API. The format of response is:

```kotlin
{	
   "code": 
}
```

