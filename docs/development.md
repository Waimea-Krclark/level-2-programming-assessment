# Development Log

The development log captures key moments in your application development:

- **Design ideas / notes** for features, UI, etc.
- **Key features** completed and working
- **Interesting bugs** and how you overcame them
- **Significant changes** to your design
- Etc.

---

## Date: 25/03/2025

### Movement Boundary Error
If the player is at the top most row and tries to move up, or the bottom most row and tries to move down, the boundary wall that is there to stop the player it's checked for an index to see if it is a wall or not, but as it is a single index the game crashes.

Old Code and error:

![MoveBug.png](screenshots/MoveBug.png)
![BorderCrash.gif](screenshots/BorderCrash.gif)

Fixed Code\
I added a check to see if the index was valid before attempting to even move to prevent any out of bounds errors from occuring due to player movement.

![MoveBugFixed.png](screenshots/MoveBugFixed.png)
---

## Date: 27/03/2025

### Maze Generation
I currently have a simple maze generation script to place walls randomly inside the play area, this system does not work well as it was not always winnable, with the door and player being able to spawn in closed off areas.

![OldMazeCode.png](screenshots/OldMazeCode.png)


---

## Date: xx/xx/20xx

Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes.

![example.png](screenshots/example.png)

---

## Date: xx/xx/20xx

Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes.

![example.png](screenshots/example.png)

---

## Date: xx/xx/20xx

Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes. Example description and notes.

![example.png](screenshots/example.png)

---


