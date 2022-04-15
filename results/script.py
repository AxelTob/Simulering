import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from pathlib import Path


def normal():
    # box plot normal times
    data_1 = np.loadtxt("normaltimes0.1.txt")
    data_2 = np.loadtxt("normaltimes0.2.txt")
    data_3 = np.loadtxt("normaltimes0.5.txt")
    df = pd.DataFrame({
        "0.1": pd.Series(data_1),
        "0.2": pd.Series(data_2),
        "0.5": pd.Series(data_3)
    })

    # Plot the dataframe
    ax = df.plot.box()
    ax.set_xlabel("Probability of Special")
    ax.set_ylabel("Time in queue")
    ax.set_title("Normal customers")

    # Display the plot
    plt.savefig('normal_customers_figure.png')
    plt.show()

def special():
    # box plot special times
    data_1 = np.loadtxt("specialtimes0.1.txt")
    data_2 = np.loadtxt("specialtimes0.2.txt")
    data_3 = np.loadtxt("specialtimes0.5.txt")
    df = pd.DataFrame({
        "0.1": pd.Series(data_1),
        "0.2": pd.Series(data_2),
        "0.5": pd.Series(data_3)
    })

    # Plot the dataframe
    ax = df.plot.box()
    ax.set_xlabel("Probability of Special")
    ax.set_ylabel("Time in queue")
    ax.set_title("Special customers")

    # Display the plot
    plt.savefig('special_customers_figure.png')
    plt.show()

if __name__ == "__main__":

    if not Path("normaltimes0.1.txt"):
        print("ERROR: Must run MainSimulation to generate result files")
        exit()

    normal()
    special()