from sklearn.linear_model import LinearRegression
from sklearn.model_selection import train_test_split
from sklearn.compose import ColumnTransformer
from sklearn.preprocessing import LabelEncoder, OneHotEncoder
import seaborn as sns
import numpy as np
import matplotlib.pyplot as plt
import pandas as pd

dataset = pd.read_csv('50_companies.csv')
dataset.head()

sns.pairplot(dataset)
plt.show()

X = dataset.iloc[:, :-1].values
Y = dataset.iloc[:, 4].values

# print(X)
labelencoder = LabelEncoder()
print(X)
X[:, 3] = labelencoder.fit_transform(X[:, 3])
print(X)

ct = ColumnTransformer(
    [("Location", OneHotEncoder(), [3])], remainder='passthrough')
X = ct.fit_transform(X)

X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=0)

regressor = LinearRegression()
regressor.fit(X_train, Y_train)

y_pred = regressor.predict(X_test)
print(Y_test)
df = pd.DataFrame({'Actual': Y_test.flatten(), 'Predicted': y_pred.flatten()})
print(df)
df1 = df.head(10)
df1.plot(kind='bar', figsize=(16, 10))
plt.grid(which='major', linestyle='-', linewidth='0.5', color='green')
plt.grid(which='minor', linestyle=':', linewidth='0.5', color='black')
plt.show()
fig = plt.figure()
plt.scatter(Y_test, y_pred)
fig.suptitle('Y_test vs y_pred', fontsize=20)
plt.xlabel('y_test', fontsize=18)
plt.ylabel('y_pred', fontsize=16)
