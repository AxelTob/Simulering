import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.stats import t

def __box_plot(file, y_label, title):
    data = pd.Series(np.loadtxt(file))
    df = pd.DataFrame(data)
    ax = df.plot.box(showfliers=False)
    ax.set_ylabel(y_label)
    ax.set_title(title)
    plt.savefig(f'{title}.png')


def __confidence_intervall(file):

    x = pd.Series(np.loadtxt(file))
    m = x.mean() 
    s = x.std() 
    dof = len(x)-1 
    confidence = 0.95
    t_crit = np.abs(t.ppf((1-confidence)/2,dof))
    print((
            m-s*t_crit/np.sqrt(len(x)),
            m+s*t_crit/np.sqrt(len(x))
        ))


def average_time_finished_work():
    __confidence_intervall('extraminutes.txt')


def average_queing_time():
    __confidence_intervall('queue_averages.txt')


def box_plot_finished_work():
    __box_plot(
        file='extraminutes.txt',
        y_label='Minutes',
        title='Extra minutes Open after closing hours')


def box_plot_average_queing_time():
    __box_plot(
        file='queue_averages.txt',
        y_label='Minutes',
        title='Average time from arrival until filled')

# What is the average time when their work will have finished every day? Use 95% confidence intervals 
average_time_finished_work()
box_plot_finished_work()
# What is the average time from the arrival of a prescription until it has been filled? Use 95% confidence 
# intervals
average_queing_time()
box_plot_average_queing_time()