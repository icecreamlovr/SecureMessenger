# Messenger

Secure message system with End-to-End encryption.

Collaborator: Yue He, Yucheng Gong

### Install dependencies

- Java (17 or above)
  - Download openjdk and install: https://jdk.java.net/archive/
  - Make sure to download the version that is compatible with your hardware

- Gradle (7.6 or above)
  - Install using sdkman https://sdkman.io/ by running the following commands:

```bash
$ curl -s "https://get.sdkman.io" | bash
$ sdk install gradle 7.6
```

- Node.js (18 or above)
  - Download from nodejs.org and install: https://nodejs.org/en/download/

- MySQL
  - Download from mysql.com and install: https://dev.mysql.com/downloads/mysql/
  - The installer will ask you to select password for the root user. Memorize it.
  - Verify MySQL installation:

```bash
$ sudo /usr/local/mysql/support-files/mysql.server start
```

```bash
$ /usr/local/mysql/bin/mysql -u root -p
 mysql> show tables;
```


### How to run locally
#### 1. Clone the repository

```bash
$ git clone git@github.com:icecreamlovr/SecureMessenger.git
```

#### 2. Start the server

```bash
$ cd SecureMessenger
$ gradle bootRun
```

#### 3. Verify
Open browser, go to http://localhost:8081/.
