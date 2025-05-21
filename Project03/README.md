Mock File System
---
In the program, I implement a model of a computers file system using recursion and trees.   
The program stores and manipulates files and directories as MacOS or Window's file system would.
Here is a list of the following commands you can enter through scanner:  
- create fileName: creates a new file and reads characters from keyboard input saving it to the "file" until it contains a '~' char
- cat fileName: prints all the contents of the given file
- rm filename: removes the file with the given name from the current directory
- mkdir dirName: creates a new directory of the given name
- rmdir dirName: removes the directory and all its contents from the current directory if it exists
- cd dirName: changes into the given directory if it is in the current directory we're in. if dirName = "/", goes to root dir of file sys.  if dirName = "..", we cd into the parent of current dir.  reports an error otherwise.
