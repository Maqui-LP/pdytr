#!/usr/bin/env python
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
import scipy.stats as stats
import statistics

def gaussian(x, mu, sig):
    return np.exp(-np.power(x - mu, 2.) / (2 * np.power(sig, 2.)))


data = [0.827271,0.893611,0.832934,0.882887,0.808945,0.783786,0.874918,0.898286,1.585325,1.201268,1.165804]
mean = statistics.mean(data)
sdv = statistics.stdev(data)
print("Media " + str(mean) + "Desviacion estandar " + str(sdv) +"\n")
x = np.linspace(mean - 2*sdv, mean + 2*sdv)
print("Graficando\n")
plt.plot(x, gaussian(x, mean, sdv),color = 'm', label="time")
plt.legend()
plt.savefig('time.png', dpi=300, bbox_inches='tight')
plt.show()

