# CS 3853 Computer Architecture - Cache & Virtual Memory Simulator

## Group Project Information
- **Course:** CS 3853 Computer Architecture, Spring 2024
- **Group Project:** Cache & Virtual Memory Simulator
- **Final Project Due:** Thu, May 2nd, 2024, 11:59 PM
- **Note:** No late assignments will be accepted.

## Objectives
The goal of this project is to simulate the internal operations of CPU caches and a simple virtual memory scheme. This involves creating a Level 1 cache simulator for a 32-bit CPU, mapping virtual memory to physical memory, and analyzing the results through performance comparison.

## Team Composition
- Work in groups of 2 to 3 students, with 3 being preferred.
- The project requires coding, testing, documenting results, and writing the final report.
- Every member must contribute to receive credit.

## Programming Languages and Tools
- You may use Python, C/C++, or Java to implement the simulator.

## Simulator Specifications
- Command line configurable cache simulation.
- Cache Size: 8 KB to 8 MB in powers of 2.
- Block Size: 8, 16, 32, 64 byte blocks.
- Associativity: direct-mapped, 2-way, 4-way, 8-way, or 16-way set associative.
- Replacement Policy: round-robin or random.
- Physical Memory: 1 MB to 4096 MB in powers of 2.
- Virtual address space: 4GB (32 bits).

## Input and Output
- The simulator accepts command-line arguments to configure cache and memory settings.
- Outputs include cache and memory simulation results, hit rates, miss rates, and CPI among others.

## Installation
Instructions on how to install or set up your project, including any dependencies or prerequisites.

## Usage
How to run the simulator, including the command-line arguments and examples of typical use cases.

```shell
CacheSim_v2.10.exe -s 512 -b 16 -a 4 -r rr -p 1024 -n 100 -u 75 -f Trace1.trc -f Trace2_4Evaluation.trc -f corruption1.trc
