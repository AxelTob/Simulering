import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.stats import t


def plot_one_run(file):
    x = pd.Series(np.loadtxt(file))
    ax = x.plot()
    ax.set_ylabel('People in Q')
    ax.set_xlabel('measurements')
    plt.savefig(f'{"one plotty"}.png')


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
    std_mean = s/np.sqrt(len(x))
    dof = len(x)-1 
    confidence = 0.95
    t_crit = np.abs(t.ppf((1-confidence)/2,dof))

    lower = m - std_mean*t_crit
    upper = m + std_mean*t_crit

    print(f'mean: {np.round(m,4)}, '
        f'std_mean: {np.round(std_mean,4)}',
        f't_crit: {np.round(t_crit,4)}, '
        f'ci: [{np.round(lower,4)}, {np.round(upper,4)}]')


def average_time_finished_work():
    __confidence_intervall('extra_minutes_open.txt')


def average_queing_time():
    __confidence_intervall('queue_average_queuing_time.txt')


def box_plot_finished_work():
    __box_plot(
        file='extra_minutes_open.txt',
        y_label='Minutes',
        title='Extra minutes Open after closing hours')


def box_plot_average_queing_time():
    __box_plot(
        file='queue_average_queuing_time.txt',
        y_label='Minutes',
        title='Average time from arrival until filled')

# What is the average time when their work will have finished every day? Use 95% confidence intervals 
print('Average time to close store, after opening hours: \n')
average_time_finished_work()
box_plot_finished_work()
# What is the average time from the arrival of a prescription until it has been filled? Use 95% confidence 
# intervals
print('Average time spent in queue\n')
average_queing_time()
box_plot_average_queing_time()

# For queue size average
print('Queue size averages\n')
__confidence_intervall('queue_size_measurements.txt')

#plot one run
print('One run\n')
plot_one_run('queue_one_run.txt')