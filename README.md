# KeyWorld

| Gradesheet | Team Info | Demo  |  Wiki |  Trello  |
|:----------:|:---------:|:-----:|:-----:|:--------:|
|[<img src="https://eecs441.eecs.umich.edu/img/admin/grades3.png">][grade_sheet]|[<img src="https://eecs441.eecs.umich.edu/img/admin/team.png">][team_contract]|[<img src="https://eecs441.eecs.umich.edu/img/admin/video.png">][demo_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/wiki.png">][wiki_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/trello.png">][process_page]|

![Elevator Pitch](/assets/Pitch.jpeg)

[grade_sheet]: https://docs.google.com/spreadsheets/d/1X3MaZ2m3UsdjcJARJQvdTjxElceTxeJx2uY92idq3xk/edit?usp=sharing
[team_contract]: https://docs.google.com/document/d/1YL6lX2RmaLtHDfeRCVXXIFOjAGVMu9O5j3gxOPFHA2Y/edit?usp=sharing
[demo_page]: https://www.youtube.com/watch?v=VIcOGEhsXGM&feature=youtu.be
[wiki_page]: https://github.com/eecs441staff/441template/wiki
[process_page]: https://trello.com/b/MA7frBMt/project-management

## Getting Started
KeyWorld is an app that applies NFC technology to solve the hotel check-in related issues. We help hotels and their guests to easily make the room a home. 

This app is developed for Android. The languages used for development are mainly Kotlin and Python. The front-end relies on the following APIs:
1. NFC reader API: https://developer.mozilla.org/en-US/docs/Web/API/Web_NFC_API
2. WiFi information API: https://developer.mozilla.org/en-US/docs/Web/API/Network_Information_API

The back-end server includes both a card module and a service module to deal with the customer information verification and service requests respectively. It communicates with a database for customer & room information.

## Model and Engine
### User Story Map
The following figure shows our user storymap.
![Storymap](/assets/storymap.png)
Users go through four stages with our app, which are: hotel setup, guest door opening, wifi connection, and room service. 
1. Hotel Setup: Hotels can upload room, key, and guest information, coonfigure wifi and instruction tags, and connect to the app to their property management system; 
2. Guest Door Opening: Guests can receive a virtual card from the hotel and open the door with it;
3. Wifi Connection: Guests can automatically connect to wifi through NFC tags
4. Room Service: Guests can request room service and get room instructions.
### Engine Architecture
The following figure shows our engine architecture.
![Engine](/assets/engine.png)
Our engine architecture has three main blocks: resident front-end, hotel front-end, and back-end (including the database).
TODO: add blocks for the instructions

**Virtual key:** 
In the beginning, the hotel needs to upload the room-key information from the hotel front-end through the key managament block to the card module in the back-end, and then the card module will save the corresponding room-key information in the database of room info table.

Then, a guest can request the virtual key with a code and his/her last name. The information is then sent to the backend, and next the card module will fetch the key from the database and send it back to the guest's application.

If the guest wants to open the door, he/she can activate the NFC card emulator on the front-end and approach the phone to the NFC reader on the door. After a successful match, the door should be open.

**NFC tag reader**
When a guest wants to connect to the hotel wifi with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the wifi configuration information the wifi conection block will automatically parse the information and connect the phone to the hotel wifi.

When a guest wants to read the room instruction with the NFC tag, the NFC card reader will be activated. Once the phone catched the NFC signal with the instruction information, the app will send a request to the service module in the back-end, which will then fetch the detailed inistruction and send it back to the guest's application.

**Room service**
Guests can request room service through the room service request block on the resident front-end, and the request will be sent to the customer service block on the hotel front-end via the service module on the back-end. Once the hotel receives the request, it can perform the appropriate service. 

## APIs and Controller
### RoomUpload
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `RoomID` | JSON | Int | Room ID |
| `Key` | JSON | Int | The key information of the room |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** 
TODO
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isFound` | JSON | Boolean | Weather the guest is found in the database|

### GuestUpload
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `RoomID` | JSON | Int | Room ID |
| `GuestFirstName` | JSON | String | Guest's first name |
| `GuestLastName` | JSON | String | Guest's last name |
| `AvailabilityStartTime` | JSON | Date(TODO) | When the key starts the availability |
| `AvailabilityEndTime` | JSON | Date(TODO) | When the key ends the availability |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** 
TODO
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isFound` | JSON | Boolean | Weather the guest is found in the database|

**Example**

### KeyFetch
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `GuestLastName` | JSON | String | Guest's last name |
| `CheckinCode` | JSON | Integer | Guest's ceck in code |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns**
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isFound` | JSON | Boolean | Weather the guest is found in the database|
| `VirtualKey` | JSON | TODO | The virtual key information for the card emulator |
| `AvailabilityStartTime` | JSON | Date(TODO) | When the key starts the availability |
| `AvailabilityEndTime` | JSON | Date(TODO) | When the key ends the availability |

### InstructionFetch
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `InstructionID` | JSON | Int | instruction ID |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** TODO
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `InstructionDetail` | JSON | Object(HTML)(TODO) | The detailed instruction|


**Example**

### RoomServiceRequest
**Request Parameters**
| Key        | Location | Type   | Description      |
| ---------- | -------- | ------ | ---------------- |
| `RoomID` | JSON | Int | Guest's room ID |
| `RequestDetail` | JSON | String | The request detail |
| `RequestTime` | JSON | Date(TODO) | The request time |

**Response Codes**
| Code              | Description            |
| ----------------- | ---------------------- |
| `200 OK`          | Succeed |
| `400 Bad Request` | Invalid parameters |

**Returns** TODO
| Key        | Location       | Type   | Description  |
| ---------- | -------------- | ------ | ------------ |
| `isFound` | JSON | Boolean | Weather the guest is found in the database|


**Example**


### Third-Party SDKs
| SDK        | Description  |
| ---------- | -------------- | 
| android.nfc | Support Android basic NFC functions |
| android.nfc.tech | Support Android advanced NFC functions |
| android.nfc.cardemulation |  Support Android NFC card emulation |
| android.net.wifi | Support Android wifi suggestions and connection |
| TODO | Live Chat |


## View UI/UX
leave this section blank for now.  You will populate it with your UI/UX design in a latter assignment. 
## Team Roster
| Team member | Contribution |
|:-----------:|:------------:|
|Yuanqi Guo||
|Xinrui Zhao||
|Ruiyu Li||
|Yixin Shi||
|Churong Ji||
|Ruge Xu||
