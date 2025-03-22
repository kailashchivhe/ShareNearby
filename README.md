# ShareNearby

This repository contains the source code for a simple Android application, "ShareNearby," designed to facilitate peer-to-peer chat communication between nearby devices using the Nearby Connections API.

## Description

ShareNearby provides a user-friendly interface to quickly and easily establish chat sessions with other devices in close proximity. It leverages the Nearby Connections API, allowing for direct, peer-to-peer communication without relying on a central server or internet connection.

Key features:

* **Host and Discoverer Roles:** The app supports two distinct roles: "Host" (advertiser) and "Discoverer."
* **Name Input:** Users can enter a name that will be displayed to other users during the connection process.
* **Device Discovery:** The "Discoverer" role discovers nearby devices running ShareNearby in "Host" mode.
* **Connection Request:** The "Discoverer" can request a connection with a discovered "Host."
* **Connection Acceptance:** The "Host" can accept or reject connection requests.
* **Chat Communication:** Once a connection is established, both devices can exchange text messages.
* **Simple UI:** Intuitive user interface for easy navigation and chat communication.

## Getting Started

### Installation

1.  Clone the repository to your local machine:

    ```bash
    git clone https://github.com/kailashchivhe/ShareNearby.git
    ```

2.  Open the project in Android Studio.
3.  Build and run the application on your Android device or emulator.

### Usage

1.  Open the ShareNearby application on two or more Android devices.
2.  On the device intended to be the "Host," enter a name and select the "Host" option. The device will begin advertising its presence.
3.  On the device intended to be the "Discoverer," enter a name and select the "Discoverer" option. The device will begin discovering nearby "Hosts."
4.  The "Discoverer" will display a list of discovered "Hosts." Select the desired "Host" from the list.
5.  The "Discoverer" will send a connection request to the selected "Host."
6.  The "Host" will receive the connection request and can choose to accept or reject it.
7.  If the "Host" accepts, a chat communication session will be established between the two devices.
8.  Both devices can now exchange text messages within the chat interface.

## Dependencies

* [Nearby Connections API (Google Play Services)](https://developers.google.com/nearby/connections/overview)

## License

This project is licensed under the [Specify License Here, e.g., MIT License] - see the [LICENSE](LICENSE) file for details.

## Author

* Kailash Chivhe - [https://github.com/kailashchivhe](https://github.com/kailashchivhe)

## Acknowledgments

* Thanks to Google for providing the Nearby Connections API.
