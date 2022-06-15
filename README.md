# KeyWorld

| Gradesheet | Team Info | Demo  |  Wiki |  Trello  |
|:----------:|:---------:|:-----:|:-----:|:--------:|
|[<img src="https://eecs441.eecs.umich.edu/img/admin/grades3.png">][grade_sheet]|[<img src="https://eecs441.eecs.umich.edu/img/admin/team.png">][team_contract]|[<img src="https://eecs441.eecs.umich.edu/img/admin/video.png">][demo_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/wiki.png">][wiki_page]|[<img src="https://eecs441.eecs.umich.edu/img/admin/trello.png">][process_page]|

![Elevator Pitch](/assets/441.jpeg)
![Team](/assets/441team.jpeg)

[grade_sheet]: https://docs.google.com/spreadsheets/d/1X3MaZ2m3UsdjcJARJQvdTjxElceTxeJx2uY92idq3xk/edit?usp=sharing
[team_contract]: https://docs.google.com/document/d/1YL6lX2RmaLtHDfeRCVXXIFOjAGVMu9O5j3gxOPFHA2Y/edit?usp=sharing
[demo_page]: https://www.youtube.com/watch?v=VIcOGEhsXGM&feature=youtu.be
[wiki_page]: https://github.com/eecs441staff/441template/wiki
[process_page]: https://trello.com/b/MA7frBMt/project-management

## Getting Started
documentation on how to build and run your project. For now, simply list and provide a link to all 3rd-party tools, libraries, SDKs, APIs your project will rely on directly, that is, you don't need to list libraries that will be installed automatically as dependencies when installing the libraries you rely on directly. List both front-end and back-end dependencies. 
## Model and Engine (15% of Project Documentation grade)
The following figure shows our Storymap.
![Storymap](/assets/storymap.png)
Our storymap has four main stages, which are: hotel setup, guest door opening, wifi connection and room service. Hotel setup is mainly for hotels to upload room key information and configure devices, guest door opening is mainly from applying for a hotel to successfully using the virtual card received to open the door, connecting to wifi is for residents to automatically connect to Wi-Fi through NFC tags, and room service is for residents to request room service from the hotel through application.

The following figure shows our engine architecture.
![Engine](/assets/engine.png)
Our engine architecture has four main blocks: External devices,  resident front-end, back-end and database, and hotel front-end.

First, the hotel needs to upload the room-key information before they can officially start using the application. The hotel needs to upload the information from the hotel front-end through the key managament block to the card module in the back-end, and then the card module will save the corresponding room-key information in the database of room info table.

When a resident wants to apply for a stay, he or she can initiate a request to the back-end through the room service request block on the resident front-end. After the back-end service module records the resident's information in the customer info table of the database, it sends the information to the customer service block at the hotel front-end. After the hotel confirmed, the key can be sent to the resident from the key management block to the card module in the back-end, and finally transmitted to the NFC Card Emulator in the resident front-end. At this point, the resident has successfully received the virtual card.

If the resident wants to open the door, he/she can try the NFC reader on the door to read the key saved in the NFC card emulator on the front of the resident. After a successful match, the door will open automatically.

If a resident wants to connect to the hotel Wi-Fi, he/she needs to find the NFC tag with the Wi-Fi configuration information first. After successfully finding it, he/she can use the NFC tag reader on the resident front-end to read this tag, and the program will automatically parse the Wi-Fi configuration information and allow the phone to successfully connect to the hotel Wi-Fi.

Finally, if a resident needs any hotel service, they can also request hotel service through the room service request block on the resident front-end, and the request will be sent to the customer service block on the hotel front-end via the service module on the back-end. Once the hotel receives the request, it can perform the appropriate service.

## APIs and Controller (15% of Project Documentation grade)
describe how your front-end would communicate with your engine: list and describe your APIs. This is only your initial design, you can change them again later, but you should start thinking about and designing how your front end will communicate with your engine. If you're using existing OS subsystem(s) or 3rd-party SDK(s) to implement your engine, describe how you will interact with these.
## View UI/UX
leave this section blank for now.  You will populate it with your UI/UX design in a latter assignment. 
## Team Roster
a list of team members and each member's contribution. You may simply list each member's full name for now, leaving the contribution description to the end of term. Should you want to make your GitHub public at the end of term, what do you want visitors (potential employer) to know about your contribution to this project?
