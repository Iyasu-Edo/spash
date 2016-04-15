# ![Spash](https://raw.githubusercontent.com/nerdammer/spash/master/resources/spash-logo.png?)

Spash is a command line tool for Big Data platforms that simulates a real Unix environment, providing most of the commands of a typical bash shell on top of **YARN, HDFS and Apache Spark**.

Spash uses the HDFS APIs to execute simple file operations and Apache Spark to perform parallel computations on big datasets.
With Spash, manipulating data on a Big Data cluster becomes *as natural as writing bash commands*.

Spash is still a *proof of concept* and **needs contributions** before going in production.

## Architecture

The Spash daemon runs on an edge node of a Big Data cluster and listens on incoming *SSH* connections on port `2222`. 
Clients can connect using the OS native terminal application, or [Putty](http://www.putty.org/) on Windows.

The Spash daemon will emulate a Unix OS and leverage the power of Spark to perform efficient computations on distributed data.   

![Spash Architecture](https://raw.githubusercontent.com/nerdammer/spash/master/resources/architecture.png?)

(*Spash Architecture*)


## The world before Spash
For those who don't remember the classic way of doing simple operations on HDFS, here's a reminder:

```console
bash$ hdfs dfs -ls /
#
##
### [wait for the JVM to load and execute]
...
##########################################
bash$ hdfs dfs -copyFromLocal myFile /
#
##
### [wait for the JVM to load and execute]
...
##########################################
bash$ 
```

## Spash makes it easier
Just run the Spash daemon on a node of your Big Data cluster, you can connect to it using `ssh user@hdfshost -p 2222` (the password is `user`) and then run all your favourite bash commands to manipulate data.

```console
user@spash:/# echo "Maybe I can provide a real life example..."
Maybe I can provide a real life example...

user@spash:/# echo "My content" > myFile

user@spash:/# ls
myFile

user@spash:/# cat myFile
My content

user@spash:/# echo text2 > myFile2

user@spash:/# ls -l
drwxr-xr-x 4 user supergroup 0 30 mar 18:34 myFile 
drwxr-xr-x 4 user supergroup 0 30 mar 18:35 myFile2

user@spash:/# cat myFile myFile2 > myFile3

user@spash:/# cat myFile3
My content
text2

user@spash:/# cat myFile3 | grep -v 2
My content

user@spash:/# exit
```

And this is just the tip of the iceberg. From your host, you can use **scp** (`scp myfile -P 2222 user@hdfshost:/`) to copy a file into hdfs. You can even use **FileZilla or WinSCP to browse HDFS**. 

With some code contributions, you will be able to **transfer files using these tools** in the future (now you can just browse).

## Contributing to the Project
Spash is an open source project and needs contributors.
**If you like the idea, fork it and start playing**.

Setting up the development environment is as easy as executing the following steps.

What you need:
- **You preferred IDE**: to build the software, that is 100% Java (IntelliJ or Eclipse are the preferred IDEs);
- **Docker**: to run a HDFS container;
- **SSH Client**: if you have Linux or Mac you already have it. If you use Windows you need (eg.) Putty to connect.

Fork the project then clone it into your machine to get the source code. Configure it as a Maven project in your IDE. Contribute using *pull requests*.

Start a HDFS *Docker* container using the following script:

```console
docker run -d -h hdfshost --name dfs -p 8020:8020 -p 50070:50070 -p 50010:50010 -p 50020:50020 -p 50075:50075 dockmob/hadoop -t pseudodistributed
```

The script above creates a container named `dfs` and binds all the necessary ports to the docker machine. In order to connect to the HDFS container, your docker machine should have hostname `hdfshost` according to your host machine. 
It means:

**On Windows or OS X**:
Your docker machine usually binds to the address `192.168.99.100`. 
Make sure that it is bound there by running `docker-machine ip default` (*"default"* is the standard name of the docker machine).
You need to append the row `192.168.99.100 hdfshost` to the `/etc/hosts` file (`C:\Windows\System32\Drivers\etc\hosts` in Windows)

**On Linux**:
Your docker machine is **your local host**.
Add `hdfshost` at the end of the row containg your loopback address (eg. `127.0.0.1 localhost hdfshost`).

The operations above should be executed only the first time you create the container. Once the container is created, you can use:
- `docker stop dfs` to stop the container;
- `docker start dfs` to start it again.

You can make any change to the source code, then:
- Start the `dfs` container;
- Compile the `core` project and run the `it.nerdammer.spash.shell.Spash` main class;
- Connect using your ssh client to `localhost` on port `2222`. Eg. `ssh user@localhost -p 2222`, using password `user`  when prompted;
- Enjoy.

## Installation
Binary packages are available for running Spash in a real cluster environment (eg. Cloudera CDH 5).
Look at the [releases section](https://github.com/nerdammer/spash/releases) to download the latest version.

Packages (*tar.gz*) need only to be extracted on a Spark client node (eg. the Master Node of the cluster). 

You need to configure the `spash.properties` file to set up some common properties, for instance, the address of your HDFS cluster.

The software can be started using the `spash.sh` command in the `bin` directory.

Spash binds by default to the `2222` port on the machine where it is launched (you can change the port from `spash.properties`).

## Supported Commands
Spash currently supports the following list of commands to manipulate data:
- cat
- cd
- echo
- exit
- grep
- head
- ls
- mkdir
- pwd
- rm
- rmdir
- write (>)

## Supported Environments
Spash has been tested on the following environments:
- Cloudera CDH 5
- Dockmob Docker Containers

