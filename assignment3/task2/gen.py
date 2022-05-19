#!/usr/bin/env python3
import random


N = 20*1000

random.seed(123)


for _ in range(N):
    x = random.randint(0, 19)
    y = random.randint(0, 19)
    direction = random.randint(0,7)
    squares_counter = random.randint(1,10)
    speed = 2.0
    print(f"{x},{y},{direction},{squares_counter},{speed}")

for _ in range(N):
    x = random.randint(0, 19)
    y = random.randint(0, 19)
    direction = random.randint(0,7)
    squares_counter = random.randint(1,10)
    speed = 4.0
    print(f"{x},{y},{direction},{squares_counter},{speed}")

for _ in range(N):
    x = random.randint(0, 19)
    y = random.randint(0, 19)
    direction = random.randint(0,7)
    squares_counter = random.randint(1,10)
    speed = round(random.uniform(1,7), 3)
    print(f"{x},{y},{direction},{squares_counter},{speed}")