game
====

First project for CompSci 308 Fall 2017

By: Yashas Manjunatha

I started this project on January 12th, 2018 and finished on January 22nd, 2018, spending about 10 - 15 hours total on the project.

I was the only developer on this project.

I used code from lab_bounce as starter code to setup the game with JavaFX.

The Shapeout.java file contains the main method and is used to start the game. It requires the Level.java and Block.java classes.

A level .txt resource file is required to import the level layout. The file must be titled "Level [Insert Level #].txt" and must be formatted with the first line containing the number of sides of the level shape, then the next line containing the number of blocks, followed by this many lines in the format "[Block X-Position] [Block Y-Position] [Block Height] [Block Width] [Block Type]". The Block type is encoded as 'N' for normal block, '+' for extra life block, and 'P' for power-up block.

Once you start the Shapeout.java game, instructions will appear for the game.

One simplification to the requirements I made was that when the ball hits a power-up block, the power-up is activated for the rest of the level and cannot be toggled off.

Cheat Keys:
* Key '1', '2', and '3' to jump to the corresponding level.
* Key 'L' to add an extra life.
* Key 'F' to toggle the flip paddle controls power up.
* Key 'B' to toggle the long paddle power up to activate.
* Key 'S' to toggle the speed up power up to activate.

A known bug is interaction of the ball with corners of blocks and paddles causes irrational behaviour and sometimes the ball bounces back the same way it came instead of normal bouncing behaviour. This is caused by the detection of collisions to two different sides simultaneously.

The extra feature I added in my game is the different shaped levels.

I feel like this was a great introduction assignment for this class to get me thinking about design principles and understand how much I don't know about design.