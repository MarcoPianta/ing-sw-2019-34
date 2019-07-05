# ing-sw-2019-34
What is Adrenalina
A War Card BoardGame for 3 to 5 players that plays in about 60 minutes.

Index of the ReadMe
- How to run the game
- External libraries used
- Specification covered

Usage
The game require Java 8 or later version to run.

Client
$ java -jar client.jar [cli/gui] [ip-address]

Option
[cli/gui] - type cli or gui to chose from Command Line Interface and Graphical User Interface (default: cli)
[ip-address] - you can specify the ip address of the server (default: localhost)

Server
$ java -jar server.jar [ip-address]

External libreries used
JSON

Game-Specific met requirements:
The game is realized with the complete-rules

Game-agnostic met requirements:

Server side

Implemented rules' game with JavaSE
Instantiated only once
It supports matches with Socket and RMI simultaneously

Client side

Implemented with JavaSE
GUI implemented with Swing
On startup the player can choose the type of connection (Socket or RMI)
On startup the player can choose the type of user interface (CLI or GUI)

Match start
A new match start when almost 3 player up to 5 are connected togheter
The match starts once there are almost 3 logged players. When 3 players connect to the match, a timer is initialized. If the timer expires the game start.

During the match

The players must follow the game's rules
The disconnection is handled both when it happens automatically and manually
The game continues, skipping the disconnected player
The players must do a move within a timer's duration
If at some point the number of player in the match is less then 3, the match ends with the remaining player's victory

Advanced functionalities

Multiple matches: the server can handle more matches at a time (players are handled with queues and rooms)

Developers
$ Davide Polimi (https://github.com/DavidePolimi)
$ Marco Pianta (https://github.com/MarcoPianta)
$ Francesco Vaghi (https://github.com/FrancescoVaghi)
