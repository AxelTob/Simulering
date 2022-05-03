#!/usr/bin/env python
# coding: utf-8


import pandas as pd
import numpy as np
import scipy.stats as st
import statsmodels.stats.api as sms
import matplotlib.pyplot as plt



month_df = pd.read_csv('months_till_boat.csv')
wealth_df = pd.read_csv('wealth_per_month.csv')

mean = month_df['Months Till Boat'].mean()
print(f'mean: {mean}')

std = month_df['Months Till Boat'].std(ddof=0)
print(f'std {std}')

mean_std = month_df['Months Till Boat'].std()/np.sqrt(len(month_df['Months Till Boat']))
print(f'mean_std: {mean_std}')

print(f'CI: {sms.DescrStatsW(month_df["Months Till Boat"]).tconfint_mean()}')

fig = plt.figure(figsize =(6, 4))
plt.boxplot([month_df['Months Till Boat']],showfliers=False, showmeans=True)
plt.ylabel('Months till boat')
plt.xticks([])
plt.savefig('Task3_Boxplot.png')

fig = plt.figure(figsize =(6, 4))
plt.xlim([0,170])
plt.xlabel('months')
plt.ylabel('wealth')
start = 0
for i in range(10):
    end = start + month_df.iloc[i]['Months Till Boat']-1
    plt.plot(wealth_df[start:end]['Month'], wealth_df[start:end]['Wealth'])
    start = end +1 
plt.savefig('Task3_ExampleWealthPlot.png')

