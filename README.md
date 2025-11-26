# File Organizer CLI
A basic file organizer that organizes your files into their respective folders.

**Example:**
If you are inside the `Downloads` folder, then all the recognized files will be moved.
All `.png` or `.jpg` files will be moved to `Downloads/Images` folder.

## Installation

1. Build the Jar file:
    ```bash
    ./gradlew shadowJar
    ```
    If successful, find the output in `app/build/libs`.

2. Install on the system
    **Move**
    ```bash
    mkdir -p ~/bin
    mv app/build/libs/app-all.jar ~/bin/organizer.jar 
    ```
    Rename it to whatever you want.
    It doesn't matter.
    Make sure `~/bin` is on the PATH.
    <br>
    You can also move it to the `/bin` instead of the user's `bin`
    
    **Create a wrapper script**
    ```bash
    # Create the file
    touch ~/bin/organize
    
    # Edit it using your desired editor
    nano ~/bin/organize
    ```
   **Paste this**
    ```bash
    #!/bin/bash
    java -jar ~/bin/organizer.jar "$@"
    ```
    **Make it executable**
   ```bash
   chmod +x ~/bin/organize
    ```

## Running
Navigate to the folder you want to organize then run:
```bash
# Dry run - will only list the actions to be done
organize --dry-run

# Actual action
organize
```
By default, it will scan the current folder you are in.

    
