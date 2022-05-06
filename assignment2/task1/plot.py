#!/usr/bin/env python3

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy.stats import t

def __confidence_intervall(filename, title, skipTransient):
    input = open(filename)
    lines = input.readlines()

    metadata = lines[0].split(',')
    serviceTime = float(metadata[0])
    measurementInterval = float(metadata[1])

    x = list(map(int, lines[1:]))

    if skipTransient:
        numTransientSamples = serviceTime/measurementInterval
        x = x[int(numTransientSamples):]

    m = np.mean(x) 
    std = np.std(x)
    dof = len(x)-1 
    confidence = 0.95
    t_crit = np.abs(t.ppf((1-confidence)/2,dof))
    std_mean = std/np.sqrt(len(x))
    lower = m - std_mean*t_crit
    upper = m + std_mean*t_crit

    print(f'{title} - '
        f'mean: {np.round(m,5)}, '
        f'std_mean: {np.round(std_mean,5)}, '
        f't_crit: {np.round(t_crit,5)}, '
        f'ci: [{np.round(lower,5)}, {np.round(upper,5)}]')

def plotData(filename, outputName):
    input = open(filename)
    lines = input.readlines()

    metadata = lines[0].split(',')
    serviceTime = float(metadata[0])
    measurementInterval = float(metadata[1])

    measurements = list(map(int, lines[1:]))

    plt.plot([x*measurementInterval for x in range(len(measurements))], measurements)
    plt.ylabel("Customers in system")
    plt.xlabel("Time [s]")
    plt.axvline(x=serviceTime, label=f'x = {int(serviceTime)}', color='red')
    plt.legend(loc='lower right')
    plt.savefig(outputName)
    plt.show()

# Task 1 - 3
plotData("1.csv", outputName="1.png")
plotData("2.csv", outputName="2.png")
plotData("3.csv", outputName="3.png")

# Task 4 - 6 
__confidence_intervall("4.csv", title="Task4", skipTransient=False)
__confidence_intervall("5.csv", title="Task5", skipTransient=False)
__confidence_intervall("6.csv", title="Task6", skipTransient=False)

__confidence_intervall("4.csv", title="Task4b", skipTransient=True)
__confidence_intervall("5.csv", title="Task5b", skipTransient=True)
__confidence_intervall("6.csv", title="Task6b", skipTransient=True)
