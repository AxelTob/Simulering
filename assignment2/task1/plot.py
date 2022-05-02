#!/usr/bin/env python3

import matplotlib.pyplot as plt

def plotData(filename):
    input = open(filename)
    lines = input.readlines()

    measurements = list(map(int, lines))

    plt.plot(measurements)
    plt.title("Lol")
    plt.ylabel("Customers in system")
    plt.xlabel("Measurement")
    plt.show()

plotData("1.csv")
plotData("2.csv")
plotData("3.csv")
