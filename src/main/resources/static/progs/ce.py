import numpy as np
import pandas as pd
data = pd.DataFrame(data=pd.read_csv('c1.csv'))
# this gives all rows except the last column
concepts = np.array(data.iloc[:, 0:-1])
target = np.array(data.iloc[:, -1])  # this gives only the target info


def learn(concepts, target):
    specific_h = concepts[0].copy()  # copy the first row
    general_h = [["?" for i in range(len(specific_h))]
                 for i in range(len(specific_h))]

    for i, h in enumerate(concepts):
        if target[i] == "Y":
            for x in range(len(specific_h)):
                if h[x] != specific_h[x]:
                    specific_h[x] = '?'
                    general_h[x] = '?'

        if target[i] == "N":
            for x in range(len(specific_h)):
                if h[x] != specific_h[x]:
                    general_h[x][x] = specific_h[x]
                else:
                    general_h[x][x] = '?'

    indices = [i for i, val in enumerate(general_h) if val == [
        '?', '?', '?', '?', '?', '?']]
    for i in indices:
        general_h.remove(['?', '?', '?', '?', '?', '?'])
    return specific_h, general_h


s_final, g_final = learn(concepts, target)
print("Final specific_h:", s_final)
print("Final general_h:", g_final)
