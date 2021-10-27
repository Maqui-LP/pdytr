#!/usr/bin/env python
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import scipy.stats as stats
import statistics

def gaussian(x, mu, sig):
    return np.exp(-np.power(x - mu, 2.) / (2 * np.power(sig, 2.)))


data = [272,250,294,283,254,291,289,258,257,284,270]
mean = statistics.mean(data)
sdv = statistics.stdev(data)
print("Media " + str(mean) + "Desviacion estandar " + str(sdv) +"\n")
x = np.linspace(mean - 2*sdv, mean + 2*sdv)
print("Graficando\n")
plt.plot(x, gaussian(x, mean, sdv),color = 'm', label="time")
plt.legend()
plt.savefig('time.png', dpi=300, bbox_inches='tight')
plt.show()