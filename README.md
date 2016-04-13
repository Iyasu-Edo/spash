# Spash

Spash is a command line tool for Big Data platforms that simulates a real Unix environment by providing most of the commands of a typical bash shell on top of **YARN, HDFS and Apache Spark**. 

Spash uses the HDFS APIs to execute simple file operations and Apache Spark to perform parallel computations on big datasets.
With Spash, executing operations on a Big Data cluster becomes *as natural as writing bash commands*.

## The state of the art
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
Once the Spash daemon has been launched on the cluster, you can connect using `ssh user@hdfshost -p 2222` and then run all your favourite bash commands to manipulate data.

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

## Contributing to the Project
Spash is an open source project and needs contributors to expand.
**If you like the idea, fork it and start playing**.

Setting up the development environment is as easy as executing the following steps.

What you need:
- You preferred IDE: to build the software, that is 100% Java (I use IntelliJ)
- Docker: to run a HDFS container
- SSH Client: if you have Linux or Mac you already have it. If you use Windows you need (eg.) Putty to connect.

Once you have cloned the project, configure it as a Maven project in your IDE.

Start a HDFS *Docker* container using the following script:
`docker run -d -h hdfshost --name dfs -p 8020:8020 -p 50070:50070 -p 50010:50010 -p 50020:50020 -p 50075:50075 dockmob/hadoop -t pseudodistributed`

The script creates a container named `dfs` and binds all the necessary ports to the docker machine. In order to connect to the pseudodistributed HDFS container, your docker machine should have hostname `hdfshost` in your host machine. This means:
- On Windows and OS X: you docker machine usually binds to the address `192.168.99.100`. You need to append the row `192.168.99.100 hdfshost` in the `/etc/hosts` file (`C:\Windows\System32\Drivers\etc\hosts` in Windows)
- On Linux: your docker machine is your local host. Add `hdfshost` at the end of the row containg your loopback address, eg. `127.0.0.1 localhost hdfshost`.

The operations above should be used only the first time you create the container. Once the container is created on your docker instance, you can use:
- `docker stop dfs` to stop the container;
- `docker start dfs` to start it again.

You can make any change to the source code. When you want to test the software:
- Start the `dfs` container;
- Compile and run the `it.nerdammer.spash.shell.Spash` main class;
- Connect from your ssh client to `localhost` on port `2222`. Eg. `ssh user@localhost -p 2222`, using password `user`  when prompted;
- Enjoy.

## Installation
A binary package is available for running it in a real cluster environment (eg. Cloudera CDH 5).
Look at the "Releases" section for more details.

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

## Disclaimer
Spash is still in **beta** status and can be considered a proof of concept.
