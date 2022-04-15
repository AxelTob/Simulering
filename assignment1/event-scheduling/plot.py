#!/usr/bin/env python3

import matplotlib.pyplot as plt

def plotData(dist, prio):
    dataName = dist+prio
    input = open(f"{dataName}.csv")
    lines = input.readlines()

    asInQueue = []
    bsInQueue = []

    for line in lines[0:500]:
        a, b = line.strip().split(',')
        asInQueue.append(int(a))
        bsInQueue.append(int(b))

    plt.plot(asInQueue, label = "Type A")
    plt.plot(bsInQueue, label = "Type B")
    plt.title(f"{dist} arrival distribution, {prio} prioritized")
    plt.legend()
    plt.ylabel("Jobs in queue")
    plt.xlabel("Measurement")
    plt.savefig(f"{dataName}.png")
    plt.show()

plotData("Constant", "B")
plotData("Exponential", "B")
plotData("Constant", "A")
