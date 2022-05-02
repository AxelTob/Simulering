#!/usr/bin/env python3

import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
from scipy.stats import t

def __confidence_intervall(filename, title):
    input = open(filename)
    lines = input.readlines()

    metadata = lines[0].split(',')
    serviceTime = float(metadata[0])
    measurementInterval = float(metadata[1])

    x = list(map(int, lines[1:]))

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
        f'mean: {np.round(m,4)}, '
        f'std_mean: {np.round(std_mean,4)}, '
        f't_crit: {np.round(t_crit,4)}, '
        f'ci: [{np.round(lower,4)}, {np.round(upper,4)}]')

def plotData(filename):
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
    plt.show()

# Task 1 - 3
plotData("1.csv")
plotData("2.csv")
plotData("3.csv")

# Task 4 - 6 
__confidence_intervall("4.csv", title="Task4")
__confidence_intervall("5.csv", title="Task5" )
__confidence_intervall("6.csv", title="Task6" )

