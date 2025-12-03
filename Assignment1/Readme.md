# Assignment 1: Producer-Consumer Application

## Overview

This project implements the **Producer-Consumer** design pattern in Java. It demonstrates thread safety using `BlockingQueue` and utilizes the **Poison Pill** strategy for graceful shutdown.

## How it Works

The application separates the generation of data from its processing using two distinct threads that communicate via a shared buffer.

### Workflow

1.  **Initialization:** The Main class creates a shared `ArrayBlockingQueue` with a fixed capacity (Bounded Buffer).
2.  **The Producer:**
    - Generates `DataItem` objects containing integers.
    - Places them into the queue.
    - **Blocking Behavior:** If the queue is full, the Producer automatically pauses (blocks) until space becomes available.
3.  **The Consumer:**
    - Continuously retrieves items from the queue.
    - **Blocking Behavior:** If the queue is empty, the Consumer automatically pauses (blocks) until new data arrives.
    - Processes the data (simulated by printing to the console).
4.  **Graceful Shutdown (Poison Pill):**
    - When the Producer finishes its work, it inserts a special "Stop Item" (null payload) into the queue.
    - When the Consumer retrieves this specific item, it recognizes the signal to stop and exits its loop safely.

### Key Concepts Used

- **Thread Safety:** The `BlockingQueue` implementation handles all synchronization internally, preventing data corruption without the need for manual `synchronized` blocks.
- **Backpressure:** Because the queue is bounded (fixed size), the Consumer controls the flow. The Producer cannot overwhelm the system if the Consumer is slow.
- **Poison Pill Pattern:** A design strategy used to shut down consumers by sending a recognizable "end-of-stream" object rather than abruptly killing the thread.

## Prerequisites

- Java JDK 8 or higher.
- Terminal / Command Line.

## Setup and Compilation

Open your terminal to the project root directory (e.g., `~/Desktop/Intuit/Assignment1`).

**1. Create the output directory:**

```bash
mkdir out
```

**2. Compile the source and test files:**

```bash
javac -d out src/*.java test/*.java
```

## How to Run

Execute the manual tests using the class path created in the `out` directory:

```bash
java -cp out Assignment1.test.ManualTests
```

## Sample Output

```text
Running Manual Tests...

Test Data Integrity... [Producer] Generated: Item-1
[Producer] Generated: Item-2
[Producer] Generated: Item-3
[Producer] Generated: Item-4
[Producer] Generated: Item-5
[Producer] Generated: Item-6
[Producer] Generated: Item-7
[Producer] Generated: Item-8
[Producer] Generated: Item-9
[Producer] Generated: Item-10
[Producer] Generated: Item-11
[Producer] Generated: Item-12
[Producer] Generated: Item-13
[Producer] Generated: Item-14
[Producer] Generated: Item-15
```
