# AVL-Project
AVL Tree-based indexing system for fast word search and storage with frequency and location tracking (file &amp; line).
## Overview
This project implements an efficient indexing system using an AVL Tree data structure.  
It is designed to store and retrieve words from text files.

## Key Features
- Efficient search, insert, and delete operations (O(log n))
- Maintains a balanced tree using AVL rotations
- Tracks word frequency
- Stores word locations (file name and line number)

## Why AVL Tree?
Unlike a normal Binary Search Tree, the AVL Tree automatically balances itself after every operation.  
This guarantees consistent performance and avoids worst-case scenarios (O(n)).

## System Capabilities
- Build index from multiple files
- Search for any word and display its occurrences
- Delete words from the index
- Traverse the tree (Post-order)

## Technologies Used
- Java
- AVL Tree (Data Structure)
- Linked List (for storing occurrences)

## Learning Outcome
This project demonstrates how data structures can be used to build efficient and scalable search systems.
