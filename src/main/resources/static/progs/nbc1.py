from sklearn import datasets
from sklearn import metrics
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import GaussianNB

import pandas as pd
import numpy as np

dataset = pd.read_csv('diabetes.csv')
x = np.array(dataset.iloc[:, 0:-1])  # concepts # iris.data
y = np.array(dataset.iloc[:, -1])  # target # iris.target

xtrain, xtest, ytrain, ytest = train_test_split(
    x, y, test_size=0.25, random_state=1)

model = GaussianNB()
model.fit(xtrain, ytrain)
predicted = model.predict(xtest)
print(metrics.confusion_matrix(ytest, predicted))
print(metrics.classification_report(ytest, predicted))
print(metrics.accuracy_score(ytest, predicted))
