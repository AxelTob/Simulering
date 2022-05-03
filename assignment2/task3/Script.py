#!/usr/bin/env python
# coding: utf-8


import pandas as pd
import numpy as np
import scipy.stats as st
import statsmodels.stats.api as sms
import matplotlib.pyplot as plt

params = [5000,10000,20000]
month_df_dict = {}
wealth_df_dict = {}

for param in params:
    month_df_dict[param] = pd.read_csv(f'months_till_boat_{param}.csv')
    wealth_df_dict[param] = pd.read_csv(f'wealth_per_month_{param}.csv')

for key in month_df_dict.keys():
    print(f"Monthly savings: {key}")
    df = month_df_dict[key]
    mean = df['Months Till Boat'].mean()
    print(f'mean: {mean}')

    std = df['Months Till Boat'].std(ddof=0)
    print(f'std {std}')

    mean_std = df['Months Till Boat'].std()/np.sqrt(len(df['Months Till Boat']))
    print(f'mean_std: {mean_std}')

    print(f'CI: {sms.DescrStatsW(df["Months Till Boat"]).tconfint_mean()} \n')


fig = plt.figure(figsize =(6, 4))
plt.boxplot([month_df_dict[5000]['Months Till Boat'],month_df_dict[10000]['Months Till Boat'],month_df_dict[20000]['Months Till Boat']],showfliers=False, showmeans=True)
plt.ylabel('Months till boat')
plt.xlabel('Savings per month')
plt.xticks([1,2,3], params)
plt.savefig('Task3_Boxplot.png')


for key in month_df_dict.keys():
    fig = plt.figure(figsize =(6, 4))
    plt.xlim([0,170])
    plt.xlabel('months')
    plt.ylabel('wealth')
    start = 0
    for i in range(10):
        end = start + month_df_dict[key].iloc[i]['Months Till Boat']-1
        plt.plot(wealth_df_dict[key][start:end]['Month'], wealth_df_dict[key][start:end]['Wealth'])
        start = end +1 
    plt.savefig(f'Task3_ExampleWealthPlot_{key}.png')

