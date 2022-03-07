from sklearn import tree
from sklearn.preprocessing import LabelEncoder
import numpy as np
import pandas as pd

PlayTennis = pd.read_csv('PlayTennis.csv')
PlayTennis

Le = LabelEncoder()
PlayTennis['Outlook'] = Le.fit_transform(PlayTennis['Outlook'])
PlayTennis['Temperature'] = Le.fit_transform(PlayTennis['Temperature'])
PlayTennis['Humidity'] = Le.fit_transform(PlayTennis['Humidity'])
PlayTennis['Wind'] = Le.fit_transform(PlayTennis['Wind'])
PlayTennis['Play Tennis'] = Le.fit_transform(PlayTennis['Play Tennis'])
PlayTennis

y = PlayTennis['Play Tennis']
X = PlayTennis.drop(['Play Tennis'], axis=1)

clf = tree.DecisionTreeClassifier(criterion='entropy')
clf = clf.fit(X, y)

tree.plot_tree(clf)
X_pred = clf.predict(X)
print(X_pred == y)
