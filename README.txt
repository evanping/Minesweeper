=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  A 2D array of type int represents the current internal game state of
  Minesweeper. Each row/column index corresponds to a certain plot that
  can contain a state: (flagged square, covered bomb, numbered square).
  An array is maintained throughout the game as the current board, and
  some 2D arrays are saved as previous game states. I chose to use type
  int within the 2D array because different numbers can be used to
  represent certain states of a plot with low memory use.

  2. Collections
  I used a Stack of 2D arrays to store previous game states so that a
  player can undo their moves. A Stack is helpful because of its first
  in first out structure, which makes it easy to retrieve the most
  recent states. This also allows for infinite undo functionality up
  to the beginning of the game.

  3. Recursion
  Recursion is used in the PlayTurn() method in the case where the
  square clicked on has zero nearby mines. It is used to recurse
  through and uncover neighboring zero-mine plots every time a
  zero-mine plot is uncovered. Recursion is a better option than
  iteration because the program uses DFS through neightboring plots.
  This will reveal all plots with no nearby bombs at once.

  4. JUnit testable components
  I implemented distinct JUnit tests for an encapsulated minesweeper
  model. The tests do not rely on any graphical components and test
  unique calls of functions in the Minesweeper class.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  The GameBoard class instantiates a Minesweeper object, which is the model
  for the game. As the user clicks the game board, the model is updated via
  mouse and key listeners. Whenever the model is updated, the game board
  repaints itself to reflect the user's actions and updates its status JLabel
  to reflect the current state of the game.

  The Minesweeper class is a model of the game, completely independent of
  the view and controller. The class provides methods to update the game
  state, including resetting the board, generating mines and numbered plots,
  uncovering and flagging plots, and even undo-ing moves.

  The RunMinesweeper class sets up the top-level frame and widgets for the
  GUI. The main frame contains the minesweeper board in the center, and a
  tool bar with "Reset" and "Undo" buttons at the top. The bottom also has
  a status panel that updates as the game progresses. Additionally, there is
  a second text panel that contains instructions for the game.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  The most significant challenge when implementing the game was getting the
  ball rolling. At first, building Minesweeper with only tic-tac-toe for
  reference was intimidating, and it was difficult to determine a clear path
  of action as there were so many functionalities to be modified/implemented.
  I started by reconstructing the design so that the GUI could display a
  Minesweeper board, which made implementing the game logic easier.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  There is good separation of functionality in my game. The private state of the
  game is well encapsulated and there are getter functions that do not allow
  modification of the game's internal state. However, I did create a few setter
  functions that allow modification of the internal state, but are only used in
  the JUnit tests. I would try to modify this so that the game state is completely
  encapsulated.