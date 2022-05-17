#!/usr/bin/env python3
import random

N = 50

random.seed(1337)

# Positions for students
for _ in range(N):
    x = random.randint(0, 9)
    y = random.randint(0, 9)
    print(f"{x},{y}")
