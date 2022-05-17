#!/usr/bin/env python3
import random

N = 20

random.seed(1337)

# Positions for students
for _ in range(N):
    x = random.randint(0, 19)
    y = random.randint(0, 19)
    direction = random.randint(0,7)
    squares_conuter = random.randint(1,10)
    print(f"{x},{y},{direction},{squares_conuter}")
