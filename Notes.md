# Notes

## Bonus Ideas
- Settings, play music, night mode
- Leaderboard
- Supercharge, obstacle blast, high frequency sound ?? (better with sideline collection of balls)
  - https://stackoverflow.com/questions/31866814/javafx-multithreading-flashing-lights effects ??

## Ideas
- ~~Obstacle, Star, SwitchColor inheriting abstract GameElements~~
- ~~ObstacleTypes inheriting abstract Obstacle~~
- ~~GameElements, CheckCollision, Action, Offset~~
- ~~Generic could be implemented for game screens, Main game screen, Load game~~
- ~~Layout with fxml, transitions through changing scenes~~
- ~~Classes like Main Game screen and Load game not necessary and can be implemented better with fxml, instead things like Database would be a good idea~~
- ~~A class with its controller methods are intertwined, load corresponding fxml in class and handle in controller~~
- Maintain midLine, highScoreLine
- Generic classes for Database, as Database for games for load screens, Database of Players for leaderboard.
- ? Ball within Game instead of Player to side-line game info with details
- ~~Controller interacts with other *controllers and fxml* but only interacts with logic of it's superceding class~~
- ~~Could use the concept of __flags__ to communicate between logic and controller~~
- Cloning in Player, Database as immutable generic class
- **IMP** May need to create destroy function for game, (see if instance is garbage collected)
- **IMP** No one is holding an instance of GamePlay which creates new game
- Controllers could be viewed as data and game state passing utilities. 

## Misc
- Threading (**IMP. FOR BETTER PERFORMANCE** [here](https://www.developer.com/java/data/multithreading-in-javafx.html))
- ~~Transitions could be done via Scene replacement in a single stage~~
- ~~maintain .gitignore~~
- Load games via creating overridden constructors
- ~~? Loading fxml while creation~~
- Every Loading and Saving would be treated as new player creation but with same parameters (Leaderboard)

## Classes
* ~~MainPage : New Game, Load Game, Exit Game~~
* ~~LoadGamePage ?~~
* Game
* GameElements (Interface / Class) ?
* Ball
* Obstacle
* Star
* SwitchColor
* ~~PauseGamePage ?~~
* ~~EndGamePage ?~~
* LeaderBoard

## TODO
* ~~Understand code workflow (b)~~
* ~~Exceptions / Interfaces / Generic~~
* ~~Ask bhavya/osheen on how to get gifs/assets~~

### Resources
- https://gist.github.com/Roland09/71ef45f14d0ec2a353e6 (Particle system)
- https://github.com/crt09/Color-Switch (assets)

### Exceptions
- Collision
- Fell Down
- Not enough stars for restart
- Std. IO/Class Not Found/

### UML & USE CASE notes
- ~~Add constructors~~
- ~~Add sorting for players~~
- ~~Add actor for enter name ?~~
- ~~what are extension points ?~~
- ~~Can generalize interact with obstacle ?~~

## Resources Deadline 2
- Color Combination https://designschool.canva.com/wp-content/uploads/sites/2/2016/01/100_Brilliant_Color_combinations_Updated1.pdf

## Notes gui:
- Pref width 450, height 700
- Color Yellow: F6DF0E Purple: 8E11FE Blue: 32E1F4 Pink: FD0082
- keep in mind, referencing of loaders & objects
- Line is fkd, sometimes it passes through wrong colors and when ball falls down on the line, it doesn't collide
- on Collision with obstacle, first stop bg music and then play music
- For all screen interchanges, assert root container size