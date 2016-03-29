# Spash

Spash is a command line tool for Big Data platforms that simulates a real Unix environment by providing most of the commands of a typical bash shell on top of **YARN, HDFS and Apache Spark**. 

Spash uses the HDFS APIs to execute simple file operations and Apache Spark to perform parallel computations on big datasets.

With Spash, executing operations on a Big Data cluster becomes as natural as writing `ssh user@hdfshost -p 2222` and then...

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

For those who don't remember the classic way of doing such things, here's a reminder:

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