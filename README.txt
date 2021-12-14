Project folder:
mebernal-project06/

doc/
    -Javadoc

src/lazyTrees/LazySearchTree.java
    - Implementation of a binary search tree as implemented in the modules
      with modifications to use lazy deletion rather than explicit hard deletion.
      Updated to use garbage collection on lazily deleted nodes.
src/lazyTrees/SuperMarket.java
    - Simulates the market scenario of buying and adding items to a store's inventory.
      Implements BST with lazy deletion to keep track of total inventory
      ("deleted" + non deleted) and current inventory (non deleted only).
      Contains main().

resources/RUN.txt
    - Console output of SuperMarket.java, Item.java, and LazySearchTree.java

README.txt
    - Description of submitted files.